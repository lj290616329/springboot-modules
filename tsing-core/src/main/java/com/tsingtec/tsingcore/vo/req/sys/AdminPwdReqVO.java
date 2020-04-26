package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author lj
 * @Date 2020/3/8 15:38
 * @Version 1.0
 */
@Data
public class AdminPwdReqVO {

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "显示名称不能为空")
    private String name;

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPwd;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPwd;
}
