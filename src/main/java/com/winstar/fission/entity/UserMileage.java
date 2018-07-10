package com.winstar.fission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dpw on 2018/07/6.
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_user_mileage")
public class UserMileage {
    public static int MILEAGE_SAFE_ANSWER = 10;
    public static int MILEAGE_INVITE_LEVEL_ONE = 10;
    public static int MILEAGE_INVITE_LEVEL_TWO = 20;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    @Column(length = 50)
    private String accountId;

    private Integer mileage;

    private Date createTime;

    private Date updateTime;
}
