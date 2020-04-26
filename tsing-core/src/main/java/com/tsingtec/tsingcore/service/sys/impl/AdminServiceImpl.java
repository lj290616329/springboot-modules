package com.tsingtec.tsingcore.service.sys.impl;

import com.tsingtec.tsingcore.entity.sys.Admin;
import com.tsingtec.tsingcore.entity.sys.Role;
import com.tsingtec.tsingcore.exception.BusinessException;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import com.tsingtec.tsingcore.repository.sys.AdminRepository;
import com.tsingtec.tsingcore.repository.sys.RoleRepository;
import com.tsingtec.tsingcore.service.sys.AdminService;
import com.tsingtec.tsingcore.vo.req.sys.AdminAddReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminPageReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminPwdReqVO;
import com.tsingtec.tsingcore.vo.req.sys.AdminUpdateReqVO;
import com.tsingtec.tsingcore.vo.resp.sys.AdminRoleRespVO;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lj
 * @Date 2020/3/29 13:54
 * @Version 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    public static String generateSalt() {
        int byteLen = SALT_SIZE >> 1;
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(byteLen).toHex();
    }

    /**
     * 获取加密后的密码，需要指定 hash迭代的次数
     * @param password       需要加密的密码
     * @param salt           盐
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        SimpleHash hash = new SimpleHash(HASH_ALGORITHM, password, salt, HASH_INTERATIONS);
        return hash.toString();
    }

    @Override
    public Admin findById(Integer id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public void save(Admin admin) {
        admin.setUpdateTime(new Date());
        adminRepository.save(admin);
    }

    @Override
    public Page<Admin> pageInfo(AdminPageReqVO vo) {
        Pageable pageable = PageRequest.of(vo.getPageNum()-1, vo.getPageSize(), Sort.Direction.DESC,"id");
        return adminRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (!StringUtils.isEmpty(vo.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"),"%"+vo.getName()+"%"));
            }

            if (!StringUtils.isEmpty(vo.getLoginName())){
                predicates.add(criteriaBuilder.like(root.get("loginName"),"%"+vo.getLoginName()+"%"));
            }

            if (null != vo.getStartTime()){
                predicates.add(criteriaBuilder.greaterThan(root.get("createTime"),vo.getStartTime()));
            }

            if (null !=vo.getEndTime()){
                predicates.add(criteriaBuilder.lessThan(root.get("createTime"),vo.getEndTime()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },pageable);
    }

    @Override
    public void insert(AdminAddReqVO vo) {
        Admin admin = findByLoginName(vo.getLoginName());
        if(admin!=null){
            throw new BusinessException(BaseExceptionType.USER_ERROR,"该登录账号已存在,请修改后再保存");
        }
        admin = new Admin();
        String salt = generateSalt();
        String password = encryptPassword(vo.getPassword(),salt);
        admin.setSalt(salt);
        admin.setName(vo.getName());
        admin.setLoginName(vo.getLoginName());
        admin.setPassword(password);
        admin.setStatus(vo.getStatus());
        admin.setCreateTime(new Date());
        admin.setUpdateTime(new Date());
        adminRepository.save(admin);
    }

    @Override
    public Admin findByLoginName(String loginName) {
        return adminRepository.findByLoginName(loginName);
    }

    @Override
    public void update(AdminUpdateReqVO vo) {
        Admin admin = findByLoginName(vo.getLoginName());
        //不是本身这个账号
        if(admin!=null && !admin.getId().equals(vo.getId())){
            throw new BusinessException(BaseExceptionType.USER_ERROR,"该登录账号已存在,请修改后再保存");
        }
        admin = adminRepository.getOne(vo.getId());
        String salt = generateSalt();
        String password = encryptPassword(vo.getPassword(),salt);
        admin.setSalt(salt);
        admin.setName(vo.getName());
        admin.setLoginName(vo.getLoginName());
        admin.setPassword(password);
        admin.setStatus(vo.getStatus());
        admin.setUpdateTime(new Date());
        adminRepository.save(admin);
    }

    @Override
    public void updatePwd(Integer id, AdminPwdReqVO vo) {
        Admin admin = adminRepository.getOne(id);
        String salt = admin.getSalt();
        if(admin.getPassword().equals(encryptPassword(vo.getOldPwd(),salt))){
            String password = encryptPassword(vo.getNewPwd(),salt);
            admin.setPassword(password);
            if(!StringUtils.isEmpty(vo.getName())){
                admin.setName(vo.getName());
            }
            adminRepository.save(admin);
        }else{
            throw new BusinessException(BaseExceptionType.USER_ERROR,"老密码不对");
        }
    }

    @Override
    public void deleteBatch(List<Integer> aids) {
        adminRepository.deleteBatch(aids);
    }

    private List<Integer>  getRidsByAid(Integer aid){
        Admin admin = findById(aid);
        Set<Role> roles = admin.getRoles();
        List<Integer> rids = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
        return rids;
    }

    @Override
    public AdminRoleRespVO getAdminRole(Integer aid) {
        List<Role> roles = roleRepository.findAll();
        List<Integer> rids = getRidsByAid(aid);
        AdminRoleRespVO adminRoleRespVO = new AdminRoleRespVO();
        adminRoleRespVO.setAllRole(roles);
        adminRoleRespVO.setOwnRoles(rids);
        return adminRoleRespVO;
    }

    @Override
    public void setAdminRole(Integer aid, List<Integer> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds);
        Admin admin = findById(aid);
        admin.setRoles(new HashSet<>(roles));
        admin.setUpdateTime(new Date());
        adminRepository.save(admin);
    }
}
