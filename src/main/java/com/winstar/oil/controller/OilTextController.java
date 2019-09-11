package com.winstar.oil.controller;

import com.winstar.oil.entity.OilText;
import com.winstar.oil.repository.OilTextRepository;
import com.winstar.utils.WsdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qyc on 2017/12/19.
 */
@RestController
@RequestMapping("/api/v1/cbc/oilText")
public class OilTextController {
    @Autowired
    OilTextRepository oilTextRepository;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity oilTextList(@RequestParam String type){

        OilText list = oilTextRepository.findByType(type);
        if(WsdUtils.isNotEmpty(list)){
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
