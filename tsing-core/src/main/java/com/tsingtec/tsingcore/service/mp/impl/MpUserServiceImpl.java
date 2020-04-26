package com.tsingtec.tsingcore.service.mp.impl;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import com.tsingtec.tsingcore.exception.BusinessException;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import com.tsingtec.tsingcore.repository.mp.MpUserRepository;
import com.tsingtec.tsingcore.service.mp.MpUserService;
import com.tsingtec.tsingcore.utils.BeanUtil;
import com.tsingtec.tsingcore.vo.req.mp.MpUserPageReqVO;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MpUserServiceImpl implements MpUserService {

    @Autowired
    private MpUserRepository mpUserRepository;

    @Override
    public MpUser findByOpenId(String openId) {
        if(StringUtils.isEmpty(openId)){
            throw new BusinessException(BaseExceptionType.USER_ERROR,"非法openid");
        }
        return mpUserRepository.findByOpenId(openId);
    }

    @Override
    public MpUser findByUnionId(String unionId) {
        return mpUserRepository.findByUnionId(unionId);
    }

    @Override
    public MpUser get(Integer id) {
        return mpUserRepository.findById(id).orElse(null);
    }

    @Override
    public Page<MpUser> pageInfo(MpUserPageReqVO vo) {
        Pageable pageable = PageRequest.of(vo.getPageNum()-1, vo.getPageSize(), Sort.Direction.DESC,"id");
        return mpUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
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
    public MpUser save(MpUser mpUser) {
        MpUser saveUser = findByOpenId(mpUser.getOpenId());
        saveUser = (saveUser != null) ? saveUser : new MpUser();
        BeanUtil.copyPropertiesIgnoreNull(mpUser,saveUser);
        if(saveUser.getCreateTime() == null){
            saveUser.setCreateTime(new Date());
        }
        saveUser.setUpdateTime(new Date());
        return mpUserRepository.save(saveUser);
    }
}
