package com.tsingtec.tsingcore.service.sys;

import com.tsingtec.tsingcore.entity.sys.Admin;
import com.tsingtec.tsingcore.vo.req.sys.AdminAddReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminPageReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminPwdReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminUpdateReqVO;
import com.tsingtec.tsingcore.vo.resp.sys.AdminRoleRespVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {

    Admin findById(Integer id);

    Page<Admin> pageInfo(AdminPageReqVO vo);

    void save(Admin admin);

    void insert(AdminAddReqVO vo);

    Admin findByLoginName(String loginName);

    void update(AdminUpdateReqVO vo);

    void updatePwd(Integer id, AdminPwdReqVO vo);

    void deleteBatch(List<Integer> aids);

    AdminRoleRespVO getAdminRole(Integer aid);

    void setAdminRole(Integer aid, List<Integer> roleIds);
}
