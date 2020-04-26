package com.tsingtec.tsingcore.service.sys;

import com.tsingtec.tsingcore.entity.sys.Role;
import com.tsingtec.tsingcore.vo.req.sys.RoleAddReqVO;
import com.tsingtec.tsingcore.vo.req.sys.RolePageReqVO;
import com.tsingtec.tsingcore.vo.req.sys.RoleUpdateReqVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {

    Role addRole(RoleAddReqVO vo);

    void updateRole(RoleUpdateReqVO vo);

    Role findById(Integer id);

    Page<Role> pageInfo(RolePageReqVO vo);

    void deleteBatch(List<Integer> rids);
}
