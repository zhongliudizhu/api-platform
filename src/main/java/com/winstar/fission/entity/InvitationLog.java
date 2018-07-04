package com.winstar.fission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qyc on 2017/12/19.
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "cbc_invitation_log")
public class InvitationLog {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 邀请人accountId
     */
    private String inviteAccountId;

    /**
     * 被邀请人accountId
     */
    @Column(length = 2000)
    private String invitedAccountId;

    /**
     * 创建时间
     */
    private Date createTime;
}
