package com.winstar.fission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户答题有效记录，记录最高分
 *
 * @author
 * @date 2017/11/17 10:00
 * @desc
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_fission_answer_question_record")
public class AnswerQuestionRecord {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 35)
    private String id;

    /**
     * 用户ID
     */
    @Column(length = 35)
    private String accountId;

    /**
     * 1 优驾行 2 西安交警
     */
    private Integer source;

    /**
     * 活动ID
     */
    @Column(length = 35)
    private String activityId;

    /**
     * 最高分
     */
    private Integer highestScore;

    /**
     * 花费时间
     */
    private Long seconds;

    /**
     * 开始答题时间
     */
    private Date startTime;
    /**
     * 结束答题时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否领取里程数 1 未领取 2已领取
     */
    @Column(columnDefinition = "int default 1")
    private Integer isReceived;

}
