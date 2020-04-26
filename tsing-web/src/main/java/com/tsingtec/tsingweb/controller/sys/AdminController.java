package com.tsingtec.tsingweb.controller.sys;

import com.tsingtec.tsingcore.entity.sys.Admin;
import com.tsingtec.tsingcore.service.sys.AdminService;
import com.tsingtec.tsingcore.utils.DataResult;
import com.tsingtec.tsingcore.vo.req.sys.AdminAddReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminPageReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminPwdReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminUpdateReqVO;
import com.tsingtec.tsingcore.vo.resp.sys.AdminRoleRespVO;
import com.tsingtec.tsingweb.aop.annotation.LogAnnotation;
import com.tsingtec.tsingweb.utils.HttpContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manager")
@Api(tags = "组织模块-用户管理")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins")
    @RequiresPermissions("sys:admin:list")
    @ApiOperation(value = "分页获取用户列表接口")
    @LogAnnotation(title = "用户管理", action = "分页获取用户列表")
    public DataResult<Page<Admin>> pageInfo(AdminPageReqVO vo) {
        DataResult<Page<Admin>> result = DataResult.success();
        result.setData(adminService.pageInfo(vo));
        return result;
    }

    @PostMapping("/admin")
    @ApiOperation(value = "新增用户接口")
    @RequiresPermissions("sys:admin:add")
    @LogAnnotation(title = "用户管理",action = "新增用户")
    public DataResult addUser(@RequestBody @Valid AdminAddReqVO vo){
        adminService.insert(vo);
        return DataResult.success();
    }


    @PutMapping("/admin")
    @ApiOperation(value = "修改用户接口")
    @RequiresPermissions("sys:admin:update")
    @LogAnnotation(title = "用户管理",action = "修改用户")
    public DataResult update(@RequestBody AdminUpdateReqVO vo){
        adminService.update(vo);
        return DataResult.success();
    }

    @PutMapping("/admin/psd")
    @ApiOperation(value = "修改用户密码接口")
    @LogAnnotation(title = "用户管理",action = "修改用户密码")
    public DataResult updatePwd(@RequestBody AdminPwdReqVO vo){
        Admin admin = HttpContextUtils.getAdmin();
        adminService.updatePwd(admin.getId(),vo);
        return DataResult.success();
    }

    @DeleteMapping("/admin")
    @ApiOperation(value = "删除用户接口")
    @RequiresPermissions("sys:admin:delete")
    @LogAnnotation(title = "用户管理",action = "删除用户")
    public DataResult deletedUser(@RequestBody @ApiParam(value = "用户id集合") List<Integer> aids){
        adminService.deleteBatch(aids);
        return DataResult.success();
    }

    @GetMapping("/admin/roles/{aid}")
    @RequiresPermissions("sys:admin:getrole")
    @ApiOperation(value = "赋予角色-获取所有角色接口")
    @LogAnnotation(title = "用户管理",action = "赋予角色-获取所有角色接口")
    public DataResult<AdminRoleRespVO> getUserOwnRole(@PathVariable("aid")Integer aid){
        DataResult<AdminRoleRespVO> result = DataResult.success();
        result.setData(adminService.getAdminRole(aid));
        return result;
    }

    @PutMapping("/admin/roles/{userId}")
    @RequiresPermissions("sys:admin:setrole")
    @ApiOperation(value = "赋予角色-用户赋予角色接口")
    @LogAnnotation(title = "用户管理",action = "赋予角色-用户赋予角色接口")
    public DataResult setUserOwnRole(@PathVariable("userId")Integer userId, @RequestBody List<Integer> roleIds){
        DataResult result= DataResult.success();
        adminService.setAdminRole(userId,roleIds);
        return result;
    }
}
