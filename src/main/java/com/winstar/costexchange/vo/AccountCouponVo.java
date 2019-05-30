package com.winstar.costexchange.vo;

import com.winstar.communalCoupon.entity.AccountCoupon;
import lombok.*;

import java.util.List;

/**
 * Created by zl on 2019/5/28
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountCouponVo {

    private String type;

    private Integer number;

    private List<AccountCoupon> accountCoupons;

}
