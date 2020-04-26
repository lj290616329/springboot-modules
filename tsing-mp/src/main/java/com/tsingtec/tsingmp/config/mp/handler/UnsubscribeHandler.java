package com.tsingtec.tsingmp.config.mp.handler;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import com.tsingtec.tsingcore.service.mp.MpUserService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class UnsubscribeHandler extends AbstractHandler {
	
	@Autowired
	private MpUserService mpUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {

    	String openId = wxMessage.getFromUser();

    	this.logger.info("取消关注用户 OPENID: " + openId);

    	MpUser mpUser = new MpUser();
		mpUser.setSubscribe(false);
		mpUser.setOpenId(openId);
    	mpUserService.save(mpUser);

    	return null;
    }

}
