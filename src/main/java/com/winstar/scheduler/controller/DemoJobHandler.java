package com.winstar.scheduler.controller;

import com.winstar.job.core.biz.model.ReturnT;
import com.winstar.job.core.handler.IJobHandler;
import com.winstar.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

//@JobHandler(value="demoJobHandler")
//@Component
public class DemoJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.print("执行成功！！！！----------------------------------------------------");
        return SUCCESS;
    }
}
