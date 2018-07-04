package com.winstar.fission.repository;

import com.winstar.fission.entity.InvitationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名称： InvitationLogRepository
 * 作者： dpw
 * 日期： 2018-07-02 14:41
 * 描述： 邀请
 **/
public interface InvitationLogRepository extends JpaRepository<InvitationLog, String> {
    InvitationLog findByInvitedAccountId(String invitedAccountId);
}
