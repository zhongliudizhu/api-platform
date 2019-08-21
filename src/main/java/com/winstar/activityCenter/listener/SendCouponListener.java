package com.winstar.activityCenter.listener;


import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.TemplateRule;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.TemplateRuleRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.entity.Fans;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.FansService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 违法处理完成后发券
 */
@Slf4j
@Service
@AllArgsConstructor
public class SendCouponListener {
    FansService fansService;
    RedisTools redisTools;
    AccountCouponService accountCouponService;
    AccountCouponRepository accountCouponRepository;
    TemplateRuleRepository templateRuleRepository;
    AccountService accountService;

    @KafkaListener(id = "illegal", topics = "illLegalTopic")
    public void SendCoupon(String openId, Acknowledgment ack) throws NotRuleException {
        log.info("openId is  : {} ", openId);
        Fans fans = fansService.getByOpenId(openId);
        if (ObjectUtils.isEmpty(fans)) {
            ack.acknowledge();
            throw new NotRuleException("粉丝信息不存在!!! openId is " + openId);
        }
        Account account = accountService.getAccountOrCreateByOpenId(fans.getOpenid(), fans.getNickname(), fans.getHeadImgUrl());
        Set<Object> set = getTemplateIds();
        if (!ObjectUtils.isEmpty(set)) {
            set.forEach(e -> accountCouponService.sendCoupon(new SendCouponDomain(e.toString(), account.getId(), AccountCoupon.TYPE_YJX, "1", null, null), null));
        } else {
            log.warn("没有支持违法赠送的优惠券！！！");
        }
        ack.acknowledge();
    }

    /**
     * 获取模板Id
     */
    private Set<Object> getTemplateIds() {
        Set<Object> set = null;
        if (redisTools.exists("illegalTemplate")) {
            set = redisTools.setMembers("illegalTemplate");
        } else {
            List<TemplateRule> templateRules = templateRuleRepository.findByIllegalStatus("yes");
            if (!ObjectUtils.isEmpty(templateRules)) {
                set = templateRules.stream().map(TemplateRule::getTemplateId).collect(Collectors.toSet());
            }
        }
        return set;
    }

}
