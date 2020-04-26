package com.tsingtec.tsingcore.entity.sys;

import com.tsingtec.tsingcore.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sys_log")
public class Log extends BaseEntity {

    private Integer aid;

    private String username;

    private String operation;

    private Integer time;

    private String method;

    private String params;

    private String ip;

}