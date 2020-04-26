package com.tsingtec.tsingcore.service.sys;

import com.tsingtec.tsingcore.entity.sys.Log;
import com.tsingtec.tsingcore.vo.req.sys.LogPageReqVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LogService {

    Page<Log> pageInfo(LogPageReqVO vo);

    void insert(Log log);

    void delete(List<Integer> logIds);
}
