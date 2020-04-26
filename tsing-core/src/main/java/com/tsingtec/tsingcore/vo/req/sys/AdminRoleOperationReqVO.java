package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author lj
 * @Date 2020/3/8 14:45
 * @Version 1.0
 */
@Data
public class AdminRoleOperationReqVO {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "用户id不能为空")
    private Integer aid;

    @ApiModelProperty(value = "角色id集合")
    @NotEmpty(message = "角色id集合不能为空")
    private List<Integer> rids;
}
