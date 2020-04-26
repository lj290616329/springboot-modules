package com.tsingtec.tsingcore.repository.sys;

import com.tsingtec.tsingcore.entity.sys.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    @Modifying
    @Transactional
    @Query("delete from Role r where r.id in (?1)")
    void deleteBatch(List<Integer> ids);

    /**
     * 取消角色与用户之间的关系
     * @param ids 角色ID
     * @return 影响结果
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sys_user_role WHERE rid in ?1", nativeQuery = true)
    void cancelUserJoin(List<Integer> ids);

    /**
     * 取消角色与菜单之间的关系
     * @param ids 角色ID
     * @return 影响结果
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sys_role_menu WHERE rid in ?1", nativeQuery = true)
    void cancelMenuJoin(List<Integer> ids);
}
