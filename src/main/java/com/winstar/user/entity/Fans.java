package com.winstar.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author UU
 * @Classname Fans
 * @Description TODO
 * @Date 2019/6/19 10:05
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cbc_fans")
public class Fans {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    private String id;

    /**
     * 用户是否订阅该公众号标识，值为 0 时，代表此用户没有关注该公众号
     */
    @Column(columnDefinition = "varchar(10) comment '是否订阅'")
    private String subscribe;

    /**
     * 用户的标识，对当前公众号唯一
     */
    @Column(columnDefinition = "varchar(50) comment '标识'")
    private String openid;

    /**
     * 昵称
     */
    @Column(columnDefinition = "varchar(100) comment '昵称'")
    private String nickname;

    /**
     * 用户的性别，值为 1 时是男性，值为 2 时是女性，值为 0 时是未知
     */
    @Column(columnDefinition = "varchar(10) comment '性别'")
    private String sex;

    /**
     * 用户所在城市
     */
    @Column(columnDefinition = "varchar(50) comment '所在城市'")
    private String city;

    /**
     * 用户所在省份
     */
    @Column(columnDefinition = "varchar(50) comment '所在省份'")
    private String province;

    /**
     * 用户所在国家
     */
    @Column(columnDefinition = "varchar(50) comment '所在国家'")
    private String country;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。
     */
    @Column(columnDefinition = "varchar(255) comment '用户头像'")
    private String headImgUrl;

    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    @Column(columnDefinition = "varchar(50) comment '关注时间'")
    private Date subscribeTime;

    /**
     * 用户所在的分组 ID
     */
    @Column(columnDefinition = "varchar(50) comment '所在分组ID'")
    private String groupId;

    /**
     * 用户被打上的标签 ID 列表
     */
    @Column(columnDefinition = "varchar(50) comment '标签ID列表'")
    private String tagIdList;

    /**
     * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
     */
    @Column(columnDefinition = "varchar(50) comment '关注渠道'")
    private String subScribeScene;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private Date createdAt;

    /**
     * 搜索开始时间
     */
    @Transient
    private String searchSubscribeTimeStart;
    /**
     * 搜索结束时间
     */
    @Transient
    private String searchSubscribeTimeEnd;

}
