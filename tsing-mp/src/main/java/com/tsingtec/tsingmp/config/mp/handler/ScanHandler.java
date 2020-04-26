package com.tsingtec.tsingmp.config.mp.handler;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import com.tsingtec.tsingcore.service.mp.MpUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class ScanHandler extends AbstractHandler {

	@Autowired
	private MpUserService mpUserService;
	
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map,
                                    WxMpService weixinService, WxSessionManager wxSessionManager) throws WxErrorException {
    	this.logger.info("老用户扫描二维码: " + wxMessage.getFromUser());
    	//先确认下用户数据是否保存在数据库
    	try {
            WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                MpUser mpUser = new MpUser();
                BeanUtils.copyProperties(userWxInfo, mpUser);
                mpUserService.save(mpUser);
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        }
		return null;
    }
    
    
    
}
