package com.amarispay.notificationservices.instanceconfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
        ignoreResourceNotFound = false,
        value = "classpath:usercodemessaging.properties")
public class UserCodeConfiguration {
    @Value("${usercode.from}")
    private String from;
    @Value("${usercode.subject}")
    private String subject;
    @Value("${usercode.beginMessage}")
    private String beginMessage;
    @Value("${usercode.endMessage}")
    private String endMessage;

    @Bean
    public String userCodeFrom(){
        return from;
    }
    @Bean
    public String userCodeSubject(){
        return subject;
    }
    @Bean
    public String userCodeBeginMessage(){
        return beginMessage;
    }
    @Bean
    public String userCodeEndMessage(){
        return endMessage;
    }
}
