package com.winstar.costexchange.service;

import com.winstar.costexchange.entity.MoveBusinessRecord;
import com.winstar.costexchange.repository.MoveBusinessRecordRepository;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MoveBusinessService {
    MoveBusinessRecordRepository moveBusinessRecordRepository;
    OilOrderRepository oilOrderRepository;

    public int check(String accountId) {
        log.info("开始查看用户 {} 资格", accountId);
        List<OilOrder> orders = oilOrderRepository.findByAccountIdAndActivityIdAndStatus(accountId, "", "3");
        if (ObjectUtils.isEmpty(orders)) {
            log.info("用户 {} 未下单，没有资格");
            return -1;
        }
        List<MoveBusinessRecord> records = moveBusinessRecordRepository.findByAccountId(accountId);
        if (ObjectUtils.isEmpty(records)) {
            log.info("用户 {} 已下单{}次，未参加活动", accountId, orders.size());
            return orders.size();
        }
        int num = orders.size() - records.size();
        log.info("用户 {} 下单{}次，参与活动{}次，剩余参与次数：{}次", accountId, orders.size(), records.size(), num);
        num = num < 0 ? 0 : num;
        return num;
    }


}
