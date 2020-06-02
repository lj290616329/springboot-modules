package com.tsingtec.tsingweb.controller;


import com.tsingtec.tsingcore.utils.DataResult;
import com.tsingtec.tsingcore.vo.req.sys.LoginReqVO;
import com.tsingtec.tsingweb.utils.RandomValidateCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 *
 * 无需授权就可以进入的页面
 */
@Slf4j
@Api(tags = "视图",description = "负责返回视图")
@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            return "redirect:/home/index";
        };
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            log.error("获取验证码失败>>>> ", e);
        }
    }

    @ResponseBody
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录接口")
    public DataResult login(@RequestBody @Valid LoginReqVO vo) {
        DataResult result = DataResult.success();
        Subject subject = SecurityUtils.getSubject();
        if(StringUtils.isEmpty(subject.getSession().getAttribute("vrifyCode"))||!subject.getSession().getAttribute("vrifyCode").equals(vo.getVercode())){
            result.setCode(400);
            result.setMsg("验证码错误,请重新输入");
            return result;
        }

        UsernamePasswordToken token = new UsernamePasswordToken(vo.getUsername(),vo.getPassword());
        try {
            subject.login(token);
            result.setCode(200);
            result.setMsg("登录成功");
        }catch (UnknownAccountException e) {
            result.setCode(400);
            result.setMsg("你被禁止登录了,不知道吗?");
        }catch(ExcessiveAttemptsException e1) {
            result.setCode(400);
            result.setMsg("尝试登录超过5次，账号已冻结，30分钟后再试");
        }catch(IncorrectCredentialsException e2) {
            result.setCode(400);
            result.setMsg("估计密码错误哦!");
        }catch(AccountException e0){
            result.setCode(400);
            result.setMsg("账号密码错误!");
        }
        return result;
    }

    @GetMapping("/403")
    public String error403(){
        return "error/403";
    }

    @GetMapping("/404")
    public String error404(){
        return "error/404";
    }

    @GetMapping("/500")
    public String error405(){
        return "error/500";
    }
}
