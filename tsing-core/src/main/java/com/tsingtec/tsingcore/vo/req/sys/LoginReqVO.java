package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author lj
 * @Date 2020/3/8 16:23
 * @Version 1.0
 */
@Data
public class LoginReqVO {

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "图片验证码")
    @NotBlank(message = "图片验证码不能为空")
    private String vercode;
}
