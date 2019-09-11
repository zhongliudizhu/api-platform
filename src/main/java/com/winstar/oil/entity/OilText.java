package com.winstar.oil.entity;

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
@Table(name = "cbc_oil_text")
public class OilText {
        @Id
        @GenericGenerator(name = "idGenerator", strategy = "uuid")
        @GeneratedValue(generator = "idGenerator")
        @Column(length = 32)
        private String id;

        /**
         * 标题
         */
        private String title;

        /**
         * 内容
         */
        @Column(length = 2000)
        private String content;

        /**
         * 类型	1：注意事项 2：购买协议
         */
        @Column(length = 1)
        private String type;

        /**
         * 创建时间
         */
        private Date createTime;
}
