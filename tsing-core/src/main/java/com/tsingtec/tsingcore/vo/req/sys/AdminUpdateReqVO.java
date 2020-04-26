package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author lj
 * @Date 2020/3/8 15:05
 * @Version 1.0
 */
@Data
public class AdminUpdateReqVO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "显示名称")
    @NotBlank(message = "显示名称不能为空")
    private String name;

    @ApiModelProperty(value = "登录账号")
    @NotBlank(message = "账号不能为空")
    private String loginName;

    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "用户状态")
    @NotNull(message = "状态不能为空")
    private Short status;
}
