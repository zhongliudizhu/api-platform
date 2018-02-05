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
 * @author shoo on 2018/2/2 16:49.
 *         -- 赠送的保险
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "love_insurance")
public class Insurance {
    /*
   *主键
   * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    @Column(length = 10)
    private String personName;
    @Column(length = 20)
    private String phoneNumber;
    @Column(length = 30)
    private String identNumber;
    @Column(length = 50)
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @Column(length = 50)
    private String openId;
    @Column(length = 50)
    private String accountId;
    @Column(length = 2)
    private String isSend;// 0 未发货  1 已发货

}
