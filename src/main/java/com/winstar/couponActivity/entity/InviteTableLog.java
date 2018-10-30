package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * CareJuanList
 *
 * @author: liuyw
 * @create 2018-08-29 10:06
 * @DESCRIPTION: 裂变用户邀请记录表
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_invite_log")
public class InviteTableLog {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    private String accountId; //邀请人id
    private Integer inviteType; //邀请类型 0 直接邀请 1 间接邀请
    private String invitedUser; //被邀请人id
    private Integer invtiteState; //邀请状态 0 邀请成功 1 待确认 2 邀请失败
    private Date updateTime; //更新时间
    private Date createTime; //创建时间
    private Integer state; //状态
}
