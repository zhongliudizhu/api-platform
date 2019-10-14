package com.winstar.oilOutPlatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutNotifyVo {

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 油站id
     */
    private String gasStationId;

    /**
     * 油站名称
     */
    private String gasStationName;

    /**
     * 使用状态
     */
    private String useState;

    /**
     * 使用时间 时间戳
     */
    private Long useDate;

    public OutNotifyVo(String couponCode, String useState) {
        this.couponCode = couponCode;
        this.useState = useState;
    }

}
