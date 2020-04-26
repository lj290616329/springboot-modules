package com.tsingtec.tsingcore.entity.mp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsingtec.tsingcore.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author lj
 * @Date 2020/4/4 22:48
 * @Version 1.0
 */

@Data
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "wx_mp_user")
public class MpUser extends BaseEntity {

    private Boolean subscribe;

    @JsonIgnore
    private String openId;

    private String nickname;

    private String sexDesc;

    private Integer sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headImgUrl;

    private Long subscribeTime;

    @JsonIgnore
    private String unionId;

    private String remark;

    private Integer groupId;

    private String subscribeScene;

    private String qrScene;

    private String qrSceneStr;

    @JsonIgnore
    private String name;

    @JsonIgnore
    private String phone;

    public String getName() {
        if(StringUtils.isEmpty(name)) {
            return nickname;
        }
        return name;
    }
}
