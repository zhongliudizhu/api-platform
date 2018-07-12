package com.winstar.obu.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ObuDot
 *
 * @author: Big BB
 * @create 2018-07-02 10:47
 * @DESCRIPTION:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_obu_dot")
public class ObuDot {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String dotName;

    private String dotAddress;

    private String dotNum;

    private Date createTime;

    private Date modifyTime;

    private String phone;
}
