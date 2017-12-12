package com.winstar.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/ccbActivity")
public class VisitStatisticController {



    @PostMapping("/pv")
    public String pv(HttpServletRequest request) {

        return "OK";
    }
}
