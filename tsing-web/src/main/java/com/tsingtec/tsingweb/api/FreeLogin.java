package com.tsingtec.tsingweb.api;

import com.google.common.collect.Maps;
import com.tsingtec.tsingcore.entity.sys.Admin;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import com.tsingtec.tsingcore.service.sys.AdminService;
import com.tsingtec.tsingcore.utils.DataResult;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/free")
public class FreeLogin {
	
	@Autowired
    private AdminService adminService;
	
	@GetMapping(value="verify")
	public DataResult verify(HttpServletRequest request, HttpServletResponse response) {
        DataResult result = DataResult.success();
	    Map<String, Object> map = Maps.newConcurrentMap();
    	String token = request.getParameter("token");
    	if(StringUtils.isEmpty(token)) {
    		return new DataResult(BaseExceptionType.USER_ERROR,"扫描登录失败,请重试!");
    	}
    	
    	String[] temp = token.split("##");
    	if(!request.getSession().getId().equals(temp[1])) {
            return new DataResult(BaseExceptionType.USER_ERROR,"授权失败!");
    	}
    	//{token=时间戳##sessionid##loginName }
    	Admin admin = adminService.findByLoginName(temp[2]);
    	if(admin==null) {
            return new DataResult(BaseExceptionType.USER_ERROR,"登录失败,账号不存在!");
    	}
    	PrincipalCollection principals = new SimplePrincipalCollection(admin, "FreeRealm");
		Builder builder = new Builder(request,response);
		builder.principals(principals);  
		builder.authenticated(true);
		builder.sessionId(request.getSession().getId());
		WebSubject subject = builder.buildWebSubject();
		ThreadContext.bind(subject);
		request.getSession().setAttribute("user", admin);
		return result;
    }
}
