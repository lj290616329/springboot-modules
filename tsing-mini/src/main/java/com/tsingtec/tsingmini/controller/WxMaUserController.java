package com.tsingtec.tsingmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.tsingtec.tsingcore.entity.mini.MaUser;
import com.tsingtec.tsingcore.service.mini.MaUserService;
import com.tsingtec.tsingcore.utils.DataResult;
import com.tsingtec.tsingcore.vo.req.mini.LoginReqVO;
import com.tsingtec.tsingcore.vo.resp.mini.InformationRespVO;
import com.tsingtec.tsingmini.aop.annotation.LoginToken;
import com.tsingtec.tsingmini.config.jwt.JwtUtil;
import com.tsingtec.tsingmini.config.mini.WxMaConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@RestController
@Api(tags = "小程序接口")
@RequestMapping("/wx")
public class WxMaUserController {

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 授权 获取身份信息
     * @param wxLoginVo
     * @return
     */
    @PostMapping("/auth")
    @ApiOperation(value = "用户授权接口")
    public DataResult<String> sign(@RequestBody LoginReqVO wxLoginVo){
        final WxMaService wxService = WxMaConfiguration.getMaService();
        DataResult result = DataResult.success();
        log.info("登录信息为:{}",wxLoginVo);
        String code = wxLoginVo.getCode();
        if(StringUtils.isBlank(code)){
            result.setCode(-1);
            result.setMsg("授权信息不全,请重新进行授权");
            return result;
        }
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            if (!wxService.getUserService().checkUserInfo(session.getSessionKey(), wxLoginVo.getRawData(), wxLoginVo.getSignature())) {
                result.setCode(-1);
                result.setMsg("user check failed");
                return result;
            }
            // 解密用户信息
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(session.getSessionKey(), wxLoginVo.getEncryptedData(), wxLoginVo.getIv());
            log.info(userInfo.toString());
            MaUser maUser = new MaUser();
            BeanUtils.copyProperties(userInfo, maUser);
            maUser = maUserService.save(maUser);
            String token = jwtUtil.getToken(maUser);
            result.setData(token);
        }catch (WxErrorException e) {
            result.setCode(-1);
            result.setMsg("授权失败,请稍后再试!");
            return result;
        }
        return result;
    }

    @LoginToken
    @GetMapping("user/{id}")
    @ApiOperation(value="用户信息")
    public DataResult<MaUser> info(@PathVariable("id") Integer id){
        DataResult result = DataResult.success();
        MaUser maUser = maUserService.get(id);
        result.setData(maUser);
        return result;
    }

    @GetMapping("update")
    @ApiOperation(value="用户信息")
    public DataResult<String> update(){
        DataResult result = DataResult.success();
        MaUser maUser = maUserService.get(1);
        maUserService.save(maUser);
        System.out.println(maUserService.get(1));
        return result;
    }

    @GetMapping("token/{id}")
    @ApiOperation(value="用户信息")
    public DataResult<String> token(@PathVariable("id") Integer id){
        DataResult result = DataResult.success();
        MaUser maUser = maUserService.get(1);
        maUser.setId(id);
        System.out.println(maUser);
        String token = jwtUtil.getToken(maUser);
        result.setData(token);
        return result;
    }

    /**
     * 登录接口
     */
    @GetMapping("/login")
    @ApiOperation(value = "用户登录接口")
    public DataResult<InformationRespVO> login(String code) {
        InformationRespVO informationRespVO = new InformationRespVO();
        DataResult result = DataResult.success();
        result.setData(informationRespVO);
        if (StringUtils.isBlank(code)) {
            result.setCode(-1);
            result.setMsg("授权信息不全,请重新进行授权");
            return result;
        }
        final WxMaService wxService = WxMaConfiguration.getMaService();
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            log.info(session.getUnionid());
            MaUser maUser = maUserService.findByOpenId(session.getOpenid());
            if(null != maUser){
                maUser.setUnionId(session.getUnionid());
                maUserService.save(maUser);
                result.setCode(0);
                result.setMsg("登录成功");
            }else{
                result.setCode(-1);
                result.setMsg("登录成功");
            }
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            result.setCode(-1);
            result.setMsg("登录失败");
        }
        return result;
    }




    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public DataResult<MaUser> info(String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        DataResult result = DataResult.success();
        final WxMaService wxService = WxMaConfiguration.getMaService();
        //用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            result.setCode(-1);
            result.setMsg("登录失败");
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        MaUser maUser = new MaUser();
        BeanUtils.copyProperties(userInfo,maUser);
        maUser = maUserService.save(maUser);
        result.setData(maUser);
        return result;
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public DataResult phone(String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        DataResult result = DataResult.success();
        final WxMaService wxService = WxMaConfiguration.getMaService();
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return result;
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return result;
    }

}