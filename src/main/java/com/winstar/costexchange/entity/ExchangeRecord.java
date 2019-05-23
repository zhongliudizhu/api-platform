package com.winstar.costexchange.entity;

import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.OilOrderUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2019/5/23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "communal_coupon_cost_exchange_record")
public class ExchangeRecord {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    private String id;

    /**
     * 订单编号
     */
    @Column(columnDefinition = "varchar(50) comment '订单编号'")
    private String orderNumber;

    /**
     * 用户id
     */
    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String accountId;

    /**
     * openId
     */
    @Column(columnDefinition = "varchar(50) comment 'openId'")
    private String openId;

    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(11) comment '手机号'")
    private String mobile;

    /**
     * 模板id
     */
    @Column(columnDefinition = "varchar(50) comment '模板id'")
    private String templateId;

    /**
     * 优惠券名称
     */
    @Column(columnDefinition = "varchar(100) comment '优惠券名称'")
    private String couponName;

    /**
     * 优惠券面值
     */
    @Column(columnDefinition = "double(10,2) comment '优惠券面值'")
    private Double couponAmount;

    /**
     * 扣除话费
     */
    @Column(columnDefinition = "double(10,2) comment '扣除话费'")
    private Double costAmount;

    /**
     * 兑换状态
     */
    @Column(columnDefinition = "varchar(50) comment '兑换状态: success/fail/inExchange'")
    private String state;

    /**
     * 兑换时间
     */
    @Column(columnDefinition = "datetime comment '兑换时间'")
    private Date createdAt;

    public ExchangeRecord(CostShop costShop, String accountId, String openId, String mobile){
        this.orderNumber = DateUtil.DateToString(new Date(), "yyyyMMddHHmmssSSS") + OilOrderUtil.getRandomNum(6);
        this.accountId = accountId;
        this.openId = openId;
        this.mobile = mobile;
        this.templateId = costShop.getTemplateId();
        this.couponName = costShop.getName();
        this.couponAmount = costShop.getAmount();
        this.costAmount = costShop.getCostAmount();
        this.state = "inExchange";
        this.createdAt = new Date();
    }

}
