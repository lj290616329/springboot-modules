package com.tsingtec.tsingcore.service.mini;

import com.tsingtec.tsingcore.entity.mini.MaUser;
import com.tsingtec.tsingcore.vo.req.mini.MaUserPageReqVO;
import org.springframework.data.domain.Page;

public interface MaUserService {

    MaUser findByOpenId(String openId);

    MaUser get(Integer id);

    Page<MaUser> pageInfo(MaUserPageReqVO wxUserReqVO);

    MaUser save(MaUser wxUser);

}
