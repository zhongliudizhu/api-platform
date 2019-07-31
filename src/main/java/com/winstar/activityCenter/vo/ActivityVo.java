package com.winstar.activityCenter.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author UU
 * @Classname ActivityVo
 * @Description TODO
 * @Date 2019/7/2 13:34
 */
@Data
public class ActivityVo {

    /**
     * 活动ID
     */
    private String activityId;
    /**
     * 活动名称
     */
    private String name;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 发放类型
     */
    private String type;

    /**
     * 发放总量
     */
    private Integer totalNum;

    /**
     * 已领张数
     */
    private Integer receivedNum;

    /**
     * 每人限领张数
     */
    private Integer perLimitNum;

    /**
     * 活动状态
     */
    private String status;

    /**
     * 领取开始日期
     */
    private Date startDate;

    /**
     * 领取结束日期
     */
    private Date endDate;
    /**
     * 优惠券金额
     */
    private String amount;
    /**
     * 卡包Id
     */
    private String wxCardId;

    /**
     * 优惠券条件
     */
    private Integer doorSkill;

    /**
     * 优惠券副标题
     */
    private String subTitle;

    /**
     * 是否首页展示 (yes/no)
     */
    private String showHome;

    /**
     * 是否只有新用户才能领取 (yes/no)
     */
    private String onlyNew;

    /**
     * 优惠券模板ID
     */
    @JsonIgnore
    private String templateId;
}
