package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author lj
 * @Date 2020/3/8 15:36
 * @Version 1.0
 */
@Data
public class MenuUpdateReqVO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id 不能为空")
    private Integer id;

    @ApiModelProperty(value = "状态1:正常 0：禁用")
    private Integer status;

    @ApiModelProperty(value = "菜单权限名称")
    @NotBlank(message = "菜单权限名称不能为空")
    private String name;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单权限标识")
    private String perms;

    @ApiModelProperty(value = "请求方式")
    private String method;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "父级id")
    @NotNull(message = "所属菜单不能为空")
    private Integer pid;

    @ApiModelProperty(value = "菜单权限类型(1:目录;2:菜单;3:按钮)")
    @NotNull(message = "菜单权限类型不能为空")
    private Integer type;
}
