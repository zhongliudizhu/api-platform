package com.winstar.activityCenter.listener;


import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.TemplateRule;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.TemplateRuleRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.utils.RequestUtil;
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
import org.apache.commons.collections.MapUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
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
    public void SendCoupon(String openId , Acknowledgment ack) throws NotRuleException {
        log.info("openId is  : {} ", openId);
        String accountId;
        Account account = ServiceManager.accountRepository.findByOpenid(openId);
        if (ObjectUtils.isEmpty(account)) {
            log.info("用户{}不存在，正在创建...", openId);
            Fans fans = fansService.saveNewFans(openId);
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
            set.forEach(e -> sendCoupon(accountId, e.toString()));
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

    /**
     * 发放优惠券
     */
    private void sendCoupon(String accountId, String templateId) {
        log.info("给用户发放优惠券：accountId is {} and templateId is {}", accountId, templateId);
        ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, "1");
        Map map = responseEntity.getBody();
        if (MapUtils.getString(map, "code").equals("SUCCESS")) {
            log.info("违法赠送获取优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "yjx", accountId, null, null);
            accountCouponRepository.save(accountCoupons);
            log.info("违法赠送发放优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
        }
    }
}
