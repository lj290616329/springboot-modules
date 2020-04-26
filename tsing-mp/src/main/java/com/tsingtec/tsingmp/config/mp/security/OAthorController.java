package com.tsingtec.tsingmp.config.mp.security;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import com.tsingtec.tsingcore.service.mp.MpUserService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/oathor")
public class OAthorController {
	
	@Value("${tsingcloud.weixin.domain}")
    private String domain;

	@Autowired
	private WxMpService wxService;

	@Autowired
	private MpUserService mpUserService;
	
	/**
	 * 构造参数并将请求重定向到微信API获取登录信息
	 * @return
	 */
	@RequestMapping(value = { "/oa" })
	public String Oauth2API(HttpServletRequest request) {
		// 此处可以添加获取持久化的数据，如企业号id等相关信息
		String redirectUrl = "";
		String lastUrl = request.getQueryString();
		try {
			if (lastUrl != null && lastUrl.contains("resultUrl=")) {
				lastUrl = lastUrl.replace("resultUrl=", "");
				redirectUrl = oAuth2Url(domain
						+ "/oathor/oauth2url?oauth2url="+domain
						+ lastUrl);
			}
		} catch (Exception e) {
			System.out.println("oa have a error");
		}
		return "redirect:" + redirectUrl;
	}

	/**
	 * 根据code获取Userid后跳转到需要带用户信息的最终页面
	 * 
	 * @param request
	 * @param code
	 *            获取微信重定向到自己设置的URL中code参数
	 *            跳转到最终页面的地址
	 * @return
	 */
	@RequestMapping(value = { "/oauth2url" })
	public String Oauth2MeUrl(HttpServletRequest request,@RequestParam String code) {
		HttpSession session = request.getSession();
		String lastUrl =  request.getQueryString();
		if(lastUrl != null && lastUrl.contains("oauth2url=")) {
			lastUrl = lastUrl.replace("oauth2url=", "");
			if(!lastUrl.contains("?")){
				lastUrl = lastUrl.replace("&code=", "?code=");
			}
		}
		try {

			WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);

			WxMpUser wxMpUser = wxService.getUserService().userInfo(accessToken.getOpenId(), null);

			System.out.println(wxMpUser.toString());

			MpUser mpUser = new MpUser();

			BeanUtils.copyProperties(wxMpUser,mpUser);

			mpUser = mpUserService.save(mpUser);

			session.setAttribute("mp_user", mpUser);

		} catch (WxErrorException e) {

            e.printStackTrace();

            return "/error";
        }
		return "redirect:" + lastUrl;
	}

	/**
	 * 组装request参数
	 * @param redirect_uri
	 * @return
	 */
	private String oAuth2Url(String redirect_uri) {
		String oauth2Url = wxService.oauth2buildAuthorizationUrl(redirect_uri,WxConsts.OAuth2Scope.SNSAPI_USERINFO,null);
		return oauth2Url;
	}
}

