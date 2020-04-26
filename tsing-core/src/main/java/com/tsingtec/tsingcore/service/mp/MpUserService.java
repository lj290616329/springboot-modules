package com.tsingtec.tsingcore.service.mp;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import com.tsingtec.tsingcore.vo.req.mp.MpUserPageReqVO;
import org.springframework.data.domain.Page;

public interface MpUserService {

    MpUser findByOpenId(String openId);

    MpUser findByUnionId(String unionId);

    MpUser get(Integer id);

    Page<MpUser> pageInfo(MpUserPageReqVO mpUserPageReqVO);

    MpUser save(MpUser wxUser);
}
