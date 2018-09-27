package com.winstar.scheduler.config;

import com.winstar.job.core.executor.XxlJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
//@Configuration
//@ComponentScan(basePackages = "com.winstar.scheduler.controller")
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${wsd_job.admin_addresses}")
    private String adminAddresses;

   @Value("${wsd_job.executor_appname}")
    private String appName;

    @Value("${wsd_job.executor_ip}")
    private String ip;

    @Value("${wsd_job.executor_port}")
    private int port;

    @Value("${wsd_job.accessToken}")
    private String accessToken;

    @Value("${wsd_job.executor_logpath}")
    private String logPath;

    @Value("${wsd_job.executor_logretentiondays}")
    private int logRetentionDays;


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(adminAddresses);
        xxlJobExecutor.setAppName(appName);
        xxlJobExecutor.setIp(ip);
        xxlJobExecutor.setPort(port);
        xxlJobExecutor.setAccessToken(accessToken);
        xxlJobExecutor.setLogPath(logPath);
        xxlJobExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobExecutor;
    }

}