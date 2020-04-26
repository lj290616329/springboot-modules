package com.tsingtec.tsingcore.service.sys;

import com.tsingtec.tsingcore.entity.sys.Menu;
import com.tsingtec.tsingcore.vo.req.sys.MenuAddReqVO;
import com.tsingtec.tsingcore.vo.req.sys.MenuUpdateReqVO;
import com.tsingtec.tsingcore.vo.resp.sys.MenuRespNodeRespVO;

import java.util.List;

public interface MenuService {

    Menu findById(Integer id);

    void insert(MenuAddReqVO vo);

    void deleteById(Integer id);

    void update(MenuUpdateReqVO menu);

    List<Menu> findAll();

    List<MenuRespNodeRespVO> menuTreeList(Integer userId);

    List<MenuRespNodeRespVO> selectAllMenuByTree(Integer menuId);

    List<MenuRespNodeRespVO> selectAllByTree();

    List<Menu> findAllById(List<Integer> mids);
}
