package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author lj
 * @Date 2020/3/8 15:50
 * @Version 1.0
 */
@Data
public class RoleAddReqVO {

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "状态(0:正常-1:弃用)")
    private Integer status;

    @ApiModelProperty(value = "所拥有的菜单权限")
    private List<String> permissions;
}
