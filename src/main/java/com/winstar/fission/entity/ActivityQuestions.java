package com.winstar.fission.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_questions")
public class ActivityQuestions {

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue
    private Integer id;
    /**
     * 活动ID
     */
    @Column(length = 35)
    private String activityId;
    /**
     * 题目
     */
    @Column(length = 255)
    private String title;

    /**
     * 图片地址 多图以 "," 分割
     */
    @Column(length = 255)
    private String image;
    /**
     * 题目类型 0 单选 1 多选
     */
    private Integer type;
    /**
     * 选项A
     */
    @Column(length = 255)
    private String optionA;
    /**
     * 选项B
     */
    @Column(length = 255)
    private String optionB;
    /**
     * 选项C
     */
    @Column(length = 255)
    private String optionC;
    /**
     * 选项D
     */
    @Column(length = 255)
    private String optionD;
    /**
     * 选项E
     */
    @Column(length = 255)
    private String optionE;
    /**
     * 选项F
     */
    @Column(length = 255)
    private String optionF;
    /**
     * 正确答案
     */
    @Column(length = 10)
    private String answer;
    /**
     * 备注
     */
    @Column(length = 30)
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 启用状态 0 启用 1 不启用
     */
    private Integer useStatus;

}
