package com.winstar.couponActivity.entity;

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
 * VehicleInfo
 *
 * @author: Big BB
 * @create 2018-04-26 17:47
 * @DESCRIPTION:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_vehicle_info")
public class VehicleInfo {


    /**
     * 唯一标识
     */
    @Id
    private String hphm;

    private String clpp1;//中文品牌

    private String gcjk;//是否国产

    private String pl;//排量

    private String dybj;//

    private Date yxqz;//审验有效期止

    private Date bxzzrq;//保险中止日期

    private Date ccdjrq;//初次登记日期

    private String clly;//

    private String hbdbqk;//环保标准

}
