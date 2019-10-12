package com.winstar.oilOutPlatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutNotifyVo {

    private String pan;

    private String tId;

    private String tName;

    private String useState;

    private Date useDate;

    public OutNotifyVo(String pan, String useState) {
        this.pan = pan;
        this.useState = useState;
    }

}
