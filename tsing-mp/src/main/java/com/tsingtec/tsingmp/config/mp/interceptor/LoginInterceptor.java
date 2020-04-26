package com.tsingtec.tsingmp.config.mp.interceptor;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import com.tsingtec.tsingcore.service.mp.MpUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private MpUserService mpUserService;

	// 这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
		HttpSession session = request.getSession();
		// 这里的User是登陆时放入session的
		MpUser user = (MpUser) session.getAttribute("mp_user");
		// 如果session中没有user，表示没登陆
		if (user == null) {
			/*user = mpUserService.findByOpenid("onYmVwjc8JGuK3xmZ7iuzMR_2daU");
			session.setAttribute("wx_user", user);
			return true;*/
			try {
				String requestPath = request.getRequestURI();
				if (StringUtils.isNotBlank(request.getQueryString())) {
					requestPath += "?" + request.getQueryString();
				}
				String contextPath = request.getContextPath();
				response.sendRedirect(contextPath + "/oathor/oa?resultUrl=" + requestPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else {
			return true; // 如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}
}
