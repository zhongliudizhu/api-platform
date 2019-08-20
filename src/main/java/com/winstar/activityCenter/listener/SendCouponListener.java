package com.winstar.activityCenter.listener;


import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.TemplateRule;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.TemplateRuleRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.entity.Account;
import com.winstar.user.entity.Fans;
import com.winstar.user.param.AccountParam;
import com.winstar.user.service.FansService;
import com.winstar.user.utils.ServiceManager;
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

    @KafkaListener(id = "illegal", topics = "illLegalTopic")
    public void SendCoupon(String openId, Acknowledgment ack) throws NotRuleException {
        log.info("openId is  : {} ", openId);
        String accountId;
        Account account = ServiceManager.accountRepository.findByOpenid(openId);
        if (ObjectUtils.isEmpty(account)) {
            log.info("用户{}不存在，正在创建...", openId);
            Fans fans = fansService.getByOpenId(openId);
            if (ObjectUtils.isEmpty(fans)) {
                ack.acknowledge();
                throw new NotRuleException("粉丝信息不存在!!! openId is " + openId);
            }
            Account accountSaved = ServiceManager.accountService.createAccount(new AccountParam(openId, fans.getNickname(), fans.getHeadImgUrl()));
            ServiceManager.accountService.createAccessToken(accountSaved);
            accountId = accountSaved.getId();
        } else {
            accountId = account.getId();
        }
        AccessToken accessToken = ServiceManager.accessTokenService.findByAccountId(accountId);
        if (null == accessToken) {
            ServiceManager.accountService.createAccessToken(account);
        }
        Set<Object> set = getTemplateIds();
        if (!ObjectUtils.isEmpty(set)) {
            set.forEach(e -> accountCouponService.sendCoupon(new SendCouponDomain(e.toString(), accountId, AccountCoupon.TYPE_YJX, "1", null, null), null));
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
