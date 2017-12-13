package com.winstar.coupon.entity;


import com.winstar.coupon.repository.CouponTemplateRepository;
import com.winstar.coupon.repository.MyCouponRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.ObjectUtils;


import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Coupon
 * <p>
 * 创建者: orange
 * 创建时间: 2017-09-04 下午3:48
 * 功能描述: 优惠券
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_my_coupon")
public class MyCoupon {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 编码
     */

    private String code;
    /**
     * 金额
     */

    private Double amount;
    /**
     * 有效期起始时间
     */

    private Date validBeginAt;
    /**
     * 有效期结束时间
     */

    private Date validEndAt;
    /**
     * 创建时间
     */

    private Date createdAt;
    /**
     * 使用时间
     */

    private Date useDate;
    /**
     * 显示状态 0 显示 1不显示
     */

    private Integer showStatus;
    /**
     * 状态 0 未使用 1 已使用 2 已失效
     */

    private Integer status;
    /**
     * 账户id
     */
    private String accountId;


    /**
     * 优惠券模板id
     */
    private String couponTemplateId;
    /**
     * 活动id
     */
    private String activityId;


    /**
     * 使用规则表达式 满多少元可用
     */
    private Double useRule;

    private String name;

    private String description;


    private Double discountRate;

    private Double limitDiscountAmount;


    @Override
    public String toString() {
        return "MyCoupon{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", amount=" + amount +
                ", validBeginAt=" + validBeginAt +
                ", validEndAt=" + validEndAt +
                ", createdAt=" + createdAt +
                ", useDate=" + useDate +
                ", showStatus=" + showStatus +
                ", status=" + status +
                ", accountId='" + accountId + '\'' +
                ", couponTemplateId='" + couponTemplateId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", useRule='" + useRule + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", discountRate=" + discountRate +
                ", limitDiscountAmount=" + limitDiscountAmount +
                '}';
    }
}
