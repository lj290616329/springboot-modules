package com.tsingtec.tsingweb.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CSRFFilter implements Filter {

	private List<String> referers = new ArrayList<>();

	private boolean enabled = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String strExcludes = filterConfig.getInitParameter("referers");
		String strEnabled = filterConfig.getInitParameter("enabled");
		//将不需要xss过滤的接口添加到列表中
		if(!StringUtils.isEmpty(strExcludes)){
			String[] urls = strExcludes.split(",");
			for(String url:urls){
				referers.add(url);
			}
		}
		if(!StringUtils.isEmpty(strEnabled)){
			enabled = Boolean.valueOf(strEnabled);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("==进入CSRF过滤器===");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// 从http头中获取Referer
		String referer = req.getHeader("Referer");
		log.error(referer);
		if(isExcludeUrl(referer)){
			chain.doFilter(request, response);
			return;
		}else {
			log.info("CSRF--跨站请求伪造。");
			String header = req.getHeader("X-Requested-With");
			boolean isAjax = "XMLHttpRequest".equals(header) ;
			if (isAjax) {//如果是ajax返回指定格式数据
				//返回禁止访问json字符串
				resp.setContentType("application/json; charset=utf-8");
				//返回禁止访问json字符串
				resp.getWriter().write("{\"code\":403,\"msg\":\"CSRF--跨站请求伪造。\"}");
				return;
			} else {//如果是普通请求进行重定向
				resp.sendRedirect("/index/login");
				return;
			}
		}
	}

	private boolean isExcludeUrl(String urlPath) {
		if (!enabled) {
			//如果CSRF开关关闭了，则所有url都不拦截
			return true;
		}
		if (referers == null || referers.isEmpty()) {
			return false;
		}
		String url = urlPath;
		for (String pattern : referers) {
			Pattern p = Pattern.compile("^" + pattern);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}
}
