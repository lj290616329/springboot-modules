package com.tsingtec.tsingweb.filter;

import com.tsingtec.tsingcore.entity.sys.Admin;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

@Slf4j
public class KickoutSessionFilter extends AccessControlFilter {

	private String kickoutUrl; // 踢出后到的地址

	private boolean kickoutAfter = false; // 踢出之前登录的/之后登录的用户 默认false踢出之前登录的用户

	private int maxSession = 1; // 同一个帐号最大会话数 默认1

	private SessionManager sessionManager;

	private Cache<String, Deque<Serializable>> cache;

	public void setKickoutUrl(String kickoutUrl) {
		this.kickoutUrl = kickoutUrl;
	}

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	// 设置Cache的key的前缀
	public void setCacheManager(CacheManager cacheManager) {
		//必须和ehcache缓存配置中的缓存name一致
		this.cache = cacheManager.getCache("shiro-activeSessionCache");
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		log.debug("come kickout");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String header = httpServletRequest.getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equals(header) ;
		Subject subject = getSubject(request, response);
		// 没有登录授权
		if (!subject.isAuthenticated()) {
			// 如果没有登录，直接进行之后的流程
			//判断是不是Ajax请求，异步请求，直接响应返回未登录
			if (isAjax) {
				httpServletResponse.setCharacterEncoding("UTF-8");
				log.debug(getClass().getName()+ "当前用户已经在其他地方登录，并且是Ajax请求！");
				httpServletResponse.getWriter().write("{\"code\":-1,\"msg\":\"您已在别处登录，请您修改密码或重新登录！\"}");
				return false;
			}else{
				return true;
			}
		}
		// 获得用户请求的URI
		HttpServletRequest req=(HttpServletRequest) request;
		String path = req.getRequestURI();
		log.debug("===当前请求的uri：==" + path);
		//String contextPath = req.getContextPath();
		//log.debug("===当前请求的域名或ip+端口：==" + contextPath);
		Session session = subject.getSession();
		log.debug("==session时间设置：" + String.valueOf(session.getTimeout())
				+ "===========");
		try {
			// 当前用户
			Admin user = (Admin) subject.getPrincipal();
			String loginName = user.getLoginName();
			log.debug("===当前用户username：==" + loginName);
			Serializable sessionId = session.getId();
			log.debug("===当前用户sessionId：==" + sessionId);
			// 读取缓存用户 没有就存入
			Deque<Serializable> deque = cache.get(loginName);
			log.debug("===当前deque：==" + deque);
			if (deque == null) {
				// 初始化队列
				deque = new ArrayDeque<Serializable>();
			}
			// 如果队列里没有此sessionId，且用户没有被踢出；放入队列
			if (!deque.contains(sessionId)
					&& session.getAttribute("kickout") == null) {
				// 将sessionId存入队列
				deque.push(sessionId);
				// 将用户的sessionId队列缓存
				cache.put(loginName, deque);
			}
			// 如果队列里的sessionId数超出最大会话数，开始踢人
			while (deque.size() > maxSession) {
				log.debug("===deque队列长度：==" + deque.size());
				Serializable kickoutSessionId = null;
				// 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
				if (kickoutAfter) { // 如果踢出后者
					kickoutSessionId = deque.removeFirst();
				} else { // 否则踢出前者
					kickoutSessionId = deque.removeLast();
				}
				// 踢出后再更新下缓存队列
				cache.put(loginName, deque);
				try {
					// 获取被踢出的sessionId的session对象
					Session kickoutSession = sessionManager
							.getSession(new DefaultSessionKey(kickoutSessionId));
					if (kickoutSession != null) {
						// 设置会话的kickout属性表示踢出了
						kickoutSession.setAttribute("kickout", true);
					}
				} catch (Exception e) {// ignore exception
				}
			}

			// 如果被踢出了，(前者或后者)直接退出，重定向到踢出后的地址
			if ((Boolean) session.getAttribute("kickout") != null
					&& (Boolean) session.getAttribute("kickout") == true) {
				// 会话被踢出了
				try {
					// 退出登录
					subject.logout();
				} catch (Exception e) { // ignore
				}
				saveRequest(request);
				log.debug("==踢出后用户重定向的路径kickoutUrl:" + kickoutUrl);
				// ajax请求
				// 重定向
				//WebUtils.issueRedirect(request, response, kickoutUrl);
				return isAjaxResponse(request,response);
			}
			return true;
		} catch (Exception e) { // ignore
			log.debug("控制用户在线数量【lyd-admin-->KickoutSessionFilter.onAccessDenied】异常！",e);
			// 重启后，ajax请求，报错：java.lang.ClassCastException:
			// com.lyd.admin.pojo.AdminUser cannot be cast to
			// com.lyd.admin.pojo.AdminUser
			// 处理 ajax请求
			return isAjaxResponse(request,response);
		}
	}

	private boolean isAjaxResponse(ServletRequest request,
			ServletResponse response) throws IOException {
		// ajax请求
		/**
		 * 判断是否已经踢出
		 * 1.如果是Ajax 访问，那么给予json返回值提示。
		 * 2.如果是普通请求，直接跳转到登录页
		 */
		//判断是不是Ajax请求
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String header = httpServletRequest.getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equals(header) ;

		if (isAjax) {
			httpServletResponse.setCharacterEncoding("UTF-8");
			log.debug(getClass().getName()+ "当前用户已经在其他地方登录，并且是Ajax请求！");
			httpServletResponse.getWriter().write("{\"code\":-1,\"msg\":\"您已在别处登录，请您修改密码或重新登录！\"}");
		}else{
			// 重定向
			WebUtils.issueRedirect(request, response, kickoutUrl);
		}
		return false;
	}

}