package com.tsingtec.tsingweb.controller.sys;

import com.tsingtec.tsingcore.entity.sys.Menu;
import com.tsingtec.tsingcore.service.sys.MenuService;
import com.tsingtec.tsingcore.utils.DataResult;
import com.tsingtec.tsingcore.vo.req.sys.MenuAddReqVO;
import com.tsingtec.tsingcore.vo.req.sys.MenuUpdateReqVO;
import com.tsingtec.tsingcore.vo.resp.sys.MenuRespNodeRespVO;
import com.tsingtec.tsingweb.aop.annotation.LogAnnotation;
import com.tsingtec.tsingweb.utils.HttpContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/manager")
@RestController
@Api(tags = "组织模块-菜单权限管理")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取用户的权限菜单
     * @return
     */
    @GetMapping("/home/menus")
    @ApiOperation(value ="获取首页数据接口")
    public DataResult<List<MenuRespNodeRespVO>> getHomeInfo(){
        Integer aid = HttpContextUtils.getAid();
        DataResult<List<MenuRespNodeRespVO>> result = DataResult.success();
        result.setData(menuService.menuTreeList(aid));
        return result;
    }

    @PostMapping("/menu")
    @ApiOperation(value = "新增菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "新增菜单权限")
    public DataResult<Menu> addMenu(@RequestBody @Valid MenuAddReqVO vo){
        DataResult<Menu> result=DataResult.success();
        menuService.insert(vo);
        return result;
    }

    @DeleteMapping("/menu/{id}")
    @ApiOperation(value = "删除菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "删除菜单权限")
    public DataResult deleted(@PathVariable("id") Integer id){
        DataResult result=DataResult.success();
        menuService.deleteById(id);
        return result;
    }

    @PutMapping("/menu")
    @ApiOperation(value = "更新菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "更新菜单权限")
    public DataResult updateMenu(@RequestBody @Valid MenuUpdateReqVO vo){
        DataResult result=DataResult.success();
        menuService.update(vo);
        return result;
    }

    @GetMapping("/menu/{id}")
    @ApiOperation(value = "查询菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "查询菜单权限")
    public DataResult<Menu> detailInfo(@PathVariable("id") Integer id){
        DataResult<Menu> result = DataResult.success();
        result.setData(menuService.findById(id));
        return result;
    }

    @GetMapping("/menus")
    @ApiOperation(value = "获取所有菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "获取所有菜单权限")
    public DataResult<List<Menu>> getAllMenusMenu(){
        DataResult<List<Menu>> result = DataResult.success();
        result.setData(menuService.findAll());
        return result;
    }

    @GetMapping("/menu/tree")
    @ApiOperation(value = "获取所有目录菜单树接口")
    @LogAnnotation(title = "菜单权限管理",action = "获取所有目录菜单树")
    public DataResult<List<MenuRespNodeRespVO>> getAllMenusMenuTree(@RequestParam(required = false) Integer menuId){
        DataResult<List<MenuRespNodeRespVO>> result=DataResult.success();
        result.setData(menuService.selectAllMenuByTree(menuId));
        return result;
    }

    @GetMapping("/menu/tree/all")
    @ApiOperation(value = "获取所有目录菜单树接口")
    @LogAnnotation(title = "菜单权限管理",action = "获取所有目录菜单树")
    public DataResult<List<MenuRespNodeRespVO>> getAllMenuTree(){
        DataResult<List<MenuRespNodeRespVO>> result=DataResult.success();
        result.setData(menuService.selectAllByTree());
        return result;
    }
}
