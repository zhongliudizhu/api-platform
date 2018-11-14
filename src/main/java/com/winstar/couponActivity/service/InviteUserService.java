package com.winstar.couponActivity.service;

import com.winstar.couponActivity.entity.InviteTableList;
import com.winstar.couponActivity.entity.InviteTableLog;
import com.winstar.couponActivity.entity.MileageObtainLog;
import com.winstar.couponActivity.repository.InviteTableLogRepository;
import com.winstar.couponActivity.repository.MileageObtainLogRepository;
import com.winstar.couponActivity.utils.UtilConstants;
import com.winstar.exception.NotFoundException;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * Created by zl on 2018/3/14
 */
@Service
public class InviteUserService {

    @Autowired
    MileageObtainLogRepository mileageObtainLogRepository;
    @Autowired
    InviteTableLogRepository inviteTableLogRepository;
    @Autowired
    private AccountService accountService;

    /**
     * 获取该用户的里程获取列表
     * @param accountId
     * @return
     * @throws NotFoundException
     */
    public List<InviteTableList> getInviteList(String accountId)throws NotFoundException {
        if (StringUtils.isEmpty(accountId)){
            new NotFoundException("用户id为空！！！");
        }
        List inviteList=new ArrayList();
        List<MileageObtainLog> mileages=mileageObtainLogRepository.findByAccountIdAndState(accountId,1);
        List<InviteTableLog> inviteTableLogs=inviteTableLogRepository.findByAccountIdAndInvtiteState(accountId,0);
        InviteTableList inviteTableList;
        Account account;
        if (mileages.size()>0) {
            for (int i = 0; i < mileages.size(); i++) {
                if (mileages.get(i).getOptainType() == 2) {
                    inviteTableList = new InviteTableList("使用优惠券", UtilConstants.FissionActivityConstants.COUPON_MILEAFE.toString(), mileages.get(i).getCreateTime());
                    inviteList.add(inviteTableList);
                } else if (mileages.get(i).getOptainType() == 3) {
                    inviteTableList = new InviteTableList("安全任务", UtilConstants.FissionActivityConstants.SAFE_TASK_MILEAFE.toString(), mileages.get(i).getCreateTime());
                    inviteList.add(inviteTableList);
                }
            }
        }
        if (inviteTableLogs.size()>0) {
            for (int j = 0; j < inviteTableLogs.size(); j++) {
                account = accountService.findOne(inviteTableLogs.get(j).getInvitedUser());
                if (inviteTableLogs.get(j).getInviteType() == 0) {
                    inviteTableList = new InviteTableList("邀请" + account.getNickName(), UtilConstants.FissionActivityConstants.DIRECT_INVTIE_MILEAFE.toString(), inviteTableLogs.get(j).getUpdateTime());
                    inviteList.add(inviteTableList);
                } else {
                    inviteTableList = new InviteTableList("邀请" + account.getNickName(), UtilConstants.FissionActivityConstants.INDIRECT_INVTIE_MILEAFE.toString(), inviteTableLogs.get(j).getUpdateTime());
                    inviteList.add(inviteTableList);
                }
            }
        }
        inviteList.sort(Comparator.comparing(InviteTableList::getCreateTime));
        return inviteList;
    }
}
