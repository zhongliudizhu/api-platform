package com.winstar.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shoo on 2018/2/2 17:22.
 *         --
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity@Table(name = "love_coupon_log")
public class CouponLog {
    /*
  *主键
  * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    @Column(length = 50)
    private String openId;
    @Column(length = 50)
    private String accountId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
