package com.reativaMVCIA.mvc1.Config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {


    @Bean
    @Qualifier("internalRestTemplate")
    @LoadBalanced
    public RestTemplate internalRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Qualifier("externalRestTemplate")
    public RestTemplate externalRestTemplate() {
        return new RestTemplate();
    }

}

/*
package com.ms1Service.ms1.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
 */