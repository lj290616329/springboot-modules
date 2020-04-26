package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author lj
 * @Date 2020/3/8 15:51
 * @Version 1.0
 */
@Data
public class RoleUpdateReqVO {

    @ApiModelProperty(value = "角色id")
    @NotNull(message = "角色 id 不能为空")
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "状态(1:正常0:弃用)")
    private Integer status;

    @ApiModelProperty(value = "所拥有的菜单权限")
    private List<Integer> mids;
}
