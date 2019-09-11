package com.winstar.order.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TimeController
 *
 * @author: Big BB
 * @create 2018-05-03 10:30
 * @DESCRIPTION:
 **/
@RestController
@RequestMapping("/api/v1/cbc/time")
public class TimeController {
    @RequestMapping(value = "nowTime", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> getNowTime(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("time",String.valueOf(new Date().getTime()));
        return map;
    }

}
