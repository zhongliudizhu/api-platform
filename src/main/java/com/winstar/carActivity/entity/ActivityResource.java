package com.winstar.carActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity_resource")
@Data
@Entity
public class ActivityResource implements Serializable {

    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;

    /**
     * 图片url
     */
    @Column(columnDefinition = "varchar(100) COMMENT '图片url'")
    private String photoUrl;

    /**
     * 资源url
     */
    @Column(columnDefinition = "varchar(100) COMMENT '资源url'")
    private String resourceUrl;


}
