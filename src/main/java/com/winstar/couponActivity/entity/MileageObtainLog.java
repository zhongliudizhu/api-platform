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
 * @DESCRIPTION:裂变用户里程记录表
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_mileage_obtain")
public class MileageObtainLog {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    private String accountId; //用户id
    private Double mileageNum; //里程数
    private Integer optainType; //获取类型 0 间接邀请 1 直接邀请  2使用优惠券 3完成安全任务
    private Date createTime; //创建时间
    private Integer state; //状态
}
