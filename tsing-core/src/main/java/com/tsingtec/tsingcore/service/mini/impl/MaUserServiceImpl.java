package com.tsingtec.tsingcore.service.mini.impl;

import com.tsingtec.tsingcore.entity.mini.MaUser;
import com.tsingtec.tsingcore.repository.mini.MaUserRepository;
import com.tsingtec.tsingcore.service.mini.MaUserService;
import com.tsingtec.tsingcore.utils.BeanUtil;
import com.tsingtec.tsingcore.vo.req.mini.MaUserPageReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"shiro"})
public class MaUserServiceImpl implements MaUserService {

    @Autowired
    private MaUserRepository maUserRepository;

    @Override
    public MaUser findByOpenId(String openId) {
        return maUserRepository.findByOpenId(openId);
    }

    @Override
    @Cacheable(key="'maUser'+#id")
    public MaUser get(Integer id) {
        return maUserRepository.findById(id).orElse(null);
    }

    @Override
    public Page<MaUser> pageInfo(MaUserPageReqVO vo) {
        Pageable pageable = PageRequest.of(vo.getPageNum()-1, vo.getPageSize(), Sort.Direction.DESC,"id");
        return maUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (!StringUtils.isEmpty(vo.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"),"%"+vo.getName()+"%"));
            }

            if (!StringUtils.isEmpty(vo.getNickName())){
                predicates.add(criteriaBuilder.like(root.get("nickName"),"%"+vo.getNickName()+"%"));
            }

            if(!StringUtils.isEmpty(vo.getGender())){
                predicates.add(criteriaBuilder.equal(root.get("gender"),vo.getGender()));
            }
            if(!StringUtils.isEmpty(vo.getPhone())){
                predicates.add(criteriaBuilder.like(root.get("phone"),"%"+vo.getPhone()+"%"));
            }

            if (null != vo.getStartTime()){
                predicates.add(criteriaBuilder.greaterThan(root.get("updateTime"),vo.getStartTime()));
            }

            if (null !=vo.getEndTime()){
                predicates.add(criteriaBuilder.lessThan(root.get("updateTime"),vo.getEndTime()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },pageable);
    }

    @Override
    @CachePut(key="'maUser'+#maUser.id")
    public MaUser save(MaUser maUser) {

        MaUser saveUser = new MaUser();

        saveUser = (saveUser != null) ? saveUser : new MaUser();

        BeanUtil.copyPropertiesIgnoreNull(maUser,saveUser);

        if(saveUser.getCreateTime() == null){

            saveUser.setCreateTime(new Date());

        }

        saveUser.setUpdateTime(new Date());

        return maUserRepository.save(saveUser);
    }

}
