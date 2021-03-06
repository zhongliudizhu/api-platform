package com.winstar.drawActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zl on 2019/3/11
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prize {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 奖品类型  1:99元优惠券  2:999元优惠券
     */
    @Column(length = 1)
    private String type;

    /**
     * 是否抽走  YES/NO
     */
    @Column(length = 5)
    private String isGet;

    /**
     * 投放时间
     */
    private Date createdAt;

}
