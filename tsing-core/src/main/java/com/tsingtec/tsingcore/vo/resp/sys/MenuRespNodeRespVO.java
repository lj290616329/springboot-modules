package com.tsingtec.tsingcore.vo.resp.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lj
 * @Date 2020/3/8 15:46
 * @Version 1.0
 */
@Data
public class MenuRespNodeRespVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "菜单权限名称")
    private String title;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单权限标识，shiro 适配restful")
    private String perms;

    @ApiModelProperty(value = "接口地址")
    private String url;


    @ApiModelProperty(value = "父级id")
    private Integer pid;

    @ApiModelProperty(value = "父级名称")
    private String pidName;

    @ApiModelProperty(value = "菜单权限类型(1:目录;2:菜单;3:按钮)")
    private Integer type;


    @ApiModelProperty(value = "是否展开 默认不展开(false)")
    private boolean spread=true;

    @ApiModelProperty(value = "是否选中 默认false")
    private boolean checked;

    private List<?> children;
}
