package com.winstar.drawActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zl on 2019/3/11
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRecord {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 用户Id
     */
    @Column(length = 50)
    private String accountId;

    /**
     * 答案
     */
    @Column(length = 50)
    private String answer;

    /**
     * 答题时间
     */
    private Date createdAt;

}
