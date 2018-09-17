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
 * @DESCRIPTION:裂变用户里程消费表
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_mileage_consum")
public class MileageConsumLog {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    private String accountId; //用户id
    private Double mileageNum; //消费里程数
    private Integer couponType; //优惠券类型
    private String mycouponId; //优惠券id
    private Date createTime; //创建时间
    private Integer state; //状态
    
}
