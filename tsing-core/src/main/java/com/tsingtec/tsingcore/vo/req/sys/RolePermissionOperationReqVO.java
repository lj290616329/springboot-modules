package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class RolePermissionOperationReqVO {

    @ApiModelProperty(value = "角色id")
    @NotBlank(message = "角色id不能为空")
    private Integer rid;

    @ApiModelProperty(value = "菜单权限集合")
    @NotEmpty(message = "菜单权限集合不能为空")
    private List<Integer> permissionIds;

}
