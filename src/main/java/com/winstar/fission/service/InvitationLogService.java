package com.winstar.fission.service;

import com.winstar.fission.repository.InvitationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 名称： InvitationLogService
 * 作者： dpw
 * 日期： 2018-07-02 14:45
 * 描述： 邀请日志
 **/
public class InvitationLogService {
    @Autowired
    InvitationLogRepository invitationLogRepository;
}
