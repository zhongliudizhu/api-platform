package com.winstar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winstar.interceptors.AuthInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * application
 * @author zl
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class Application extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new ApplicationPidFileWriter("pid/winstar-cbc-platform-api.pid"));
        application.run(args);
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor());
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ClientErrorHandler());
        return restTemplate;
    }
    @Bean
    public ObjectMapper ObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper;
    }

}
