package com.winstar.fission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dpw on 2018/07/6.
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_invitation_log")
public class InvitationLog {
    public static int LEVEL_DIRECT = 1;
    public static int LEVEL_INDIRECT = 2;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 邀请人accountId
     */
    @Column(length = 50)
    private String inviteAccountId;

    /**
     * 被邀请人accountId
     */
    @Column(length = 50)
    private String invitedAccountId;

    /**
     * 1 直接邀请  2 间接邀请
     */
    private Integer level;

    private Date createTime;
}
