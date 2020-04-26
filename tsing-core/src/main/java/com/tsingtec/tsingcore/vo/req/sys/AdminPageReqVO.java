package com.tsingtec.tsingcore.vo.req.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author lj
 * @Date 2020/3/8 15:09
 * @Version 1.0
 */
@Data
public class AdminPageReqVO {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "账号")
    private String name;

    @ApiModelProperty(value = "昵称")
    private String loginName;

    @ApiModelProperty(value = "账户状态(0.正常 -1.锁定)")
    private Short status;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
