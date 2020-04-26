package com.tsingtec.tsingcore.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsingtec.tsingcore.entity.BaseEntity;
import com.tsingtec.tsingcore.vo.resp.sys.MenuRespNodeRespVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色表
 * @author lj
 *
 */
@Data
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "sys_role")
@ToString(exclude = {"admins", "menus"})
@EqualsAndHashCode(exclude = {"admins", "menus"})
public class Role extends BaseEntity {
	private String name;

    private String description;

    private Integer status;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu",joinColumns = @JoinColumn(name = "rid"),inverseJoinColumns = @JoinColumn(name = "mid"))
    @JsonIgnore
    private Set<Menu> menus = new HashSet<>(0);

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private Set<Admin> admins = new HashSet<>(0);


    @Transient
    private List<MenuRespNodeRespVO> chilids;

}