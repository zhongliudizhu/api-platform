package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.InviteTableLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * CareJuanListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:用户邀请记录
 **/
public interface InviteTableLogRepository extends JpaRepository<InviteTableLog,String>,JpaSpecificationExecutor<InviteTableLog> {
    /**
     * 根据用户id和要去类型获取邀请记录
     * @param inviteUserId
     * @param i
     * @return
     */
    InviteTableLog findByInvitedUserAndInviteType(String inviteUserId, Integer i);
    /**
     * 根据被邀请用户id和状态和邀请状态获取邀请记录
     * @param inviteUserId
     * @param i
     * @return
     */
    List<InviteTableLog> findByInvitedUserAndStateAndInvtiteState(String inviteUserId, Integer i,Integer j);
    /**
     * 根据被邀请用户id和状态获取邀请记录
     * @param inviteUserId
     * @param i
     * @return
     */
    List<InviteTableLog> findByInvitedUserAndState(String inviteUserId, Integer i);

    /**
     * 根据邀请人id，邀请类型获取记录
     * @param accountId
     * @param i
     */
    List<InviteTableLog> findByAccountIdAndInviteType(String accountId, int i);

    /**
     * 获取用户邀请成功的人数
     */
    List<InviteTableLog> findByAccountIdAndInviteTypeAndInvtiteState(String accountId,int i,int j);

    /**
     * 获取邀请成功的人数
     * @param accountId
     * @param i
     * @return
     */
    List<InviteTableLog> findByAccountIdAndInvtiteState(String accountId,int i);
}
