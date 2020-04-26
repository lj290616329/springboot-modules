package com.tsingtec.tsingcore.repository.mp;

import com.tsingtec.tsingcore.entity.mp.MpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MpUserRepository extends JpaRepository<MpUser, Integer>, JpaSpecificationExecutor<MpUser> {

    MpUser findByOpenId(String openId);

    MpUser findByUnionId(String unionId);
}
