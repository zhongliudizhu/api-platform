package com.winstar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winstar.interceptors.AuthInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;

/**
 * application
 *
 * @author zl
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableCaching
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
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ClientErrorHandler());
        return restTemplate;
    }

    @Bean
    public ObjectMapper ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

//    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
//    @Bean
//    public KafkaListenerContainerFactory kafkaListenerContainerFactory() {
//        return new ConcurrentKafkaListenerContainerFactory();
//    }
    // LISTENER 1
//    @Bean
//    @ConditionalOnMissingBean(name = "yourListenerFactory1")
//    public ConsumerFactory<String, SendCouponListener> yourConsumerFactory1() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.118.69:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "myGroup");
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
//                new JsonDeserializer<>(SendCouponListener.class));
//    }
//
//
//    @Bean(name = "yourListenerFactory1")
//    public ConcurrentKafkaListenerContainerFactory<String, SendCouponListener>
//    yourListenerFactory1() {
//        ConcurrentKafkaListenerContainerFactory<String, SendCouponListener> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(yourConsumerFactory1());
//        ContainerProperties containerProperties = factory.getContainerProperties();
//        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }
}
