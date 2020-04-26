package com.tsingtec.tsingcore.repository.sys;

import com.tsingtec.tsingcore.entity.sys.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer>,JpaSpecificationExecutor<Log> {

    @Modifying
    @Transactional
    @Query("delete from Log s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
}
