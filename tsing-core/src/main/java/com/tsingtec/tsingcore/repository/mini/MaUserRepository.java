package com.tsingtec.tsingcore.repository.mini;

import com.tsingtec.tsingcore.entity.mini.MaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MaUserRepository extends JpaRepository<MaUser, Integer>, JpaSpecificationExecutor<MaUser> {

    MaUser findByOpenId(String openid);

    MaUser findByUnionId(String unionid);
}
