package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * CareJuanList
 *
 * @author: liuyw
 * @create 2018-08-29 10:06
 * @DESCRIPTION: 裂变用户邀请记录列表
 **/
public class InviteTableList {
    private String name;
    private String mileage;
    private Date createTime;

    public InviteTableList() {
    }
    public InviteTableList(String name, String mileage, Date createTime) {
        this.name = name;
        this.mileage = mileage;
        this.createTime = createTime;
    }
    public String getName() {
        return name;
    }

    public String getMileage() {
        return mileage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
