package com.tsingtec.tsingcore.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsingtec.tsingcore.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * 用户账号表
 * @author lj
 *
 */
@Data
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "sys_admin")
@ToString(exclude = {"roles"})
@EqualsAndHashCode(exclude = {"roles"})
public class Admin extends BaseEntity {

    //0:禁止登录
    public static final Short UNVALID = -1;
    //1:有效
    public static final Short VALID = 0;

    /**名称*/
    private String name;

    /**登录帐号*/
    private String loginName;

    /**密码*/
    private String password;

    /**登录帐号*/
    private Integer unionId=0;

    /**盐*/
    private String salt;

    /**0:有效，-1:禁止登录*/
    private Short status = VALID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_admin_role",
            joinColumns = @JoinColumn(name = "aid"),
            inverseJoinColumns = @JoinColumn(name = "rid"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);

}
