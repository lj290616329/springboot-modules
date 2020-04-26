package com.tsingtec.tsingcore.service.sys.impl;

import com.tsingtec.tsingcore.entity.sys.Log;
import com.tsingtec.tsingcore.repository.sys.LogRepository;
import com.tsingtec.tsingcore.service.sys.LogService;
import com.tsingtec.tsingcore.vo.req.sys.LogPageReqVO;
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

/**
 * @Author lj
 * @Date 2020/3/29 13:54
 * @Version 1.0
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;


    @Override
    public Page<Log> pageInfo(LogPageReqVO vo) {
        Pageable pageable = PageRequest.of(vo.getPageNum()-1, vo.getPageSize(),Sort.Direction.DESC,"id");
        return logRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (!StringUtils.isEmpty(vo.getOperation())){
                predicates.add(criteriaBuilder.like(root.get("operation"),"%"+vo.getOperation()+"%"));
            }

            if (!StringUtils.isEmpty(vo.getUsername())){
                predicates.add(criteriaBuilder.equal(root.get("username"),vo.getUsername()));
            }

            if (null != vo.getAid()){
                predicates.add(criteriaBuilder.equal(root.get("aid"),vo.getAid()));
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
    public void insert(Log sysLog) {
        sysLog.setCreateTime(new Date());
        logRepository.save(sysLog);
    }

    @Override
    public void delete(List<Integer> ids) {
        logRepository.deleteBatch(ids);
    }

}
