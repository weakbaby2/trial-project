package com.example.demo.project.config;

import java.time.Duration;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.example.demo.project.utils.PartialListUtil;


@Configuration
public class ApplicationConfig {
	
	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
	    return new ForwardedHeaderFilter();
	}
	
	@Bean
	public PartialListUtil getPartialListUtil() {
		return new PartialListUtil();
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofMillis(30000))
            .setReadTimeout(Duration.ofMillis(30000))
        .build();
    }
	
	@Bean
	public SecurityUtil getSecurityUtil() {
		return new SecurityUtil();
	}

}
