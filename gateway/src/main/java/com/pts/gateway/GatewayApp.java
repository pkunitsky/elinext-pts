package com.pts.gateway;

import com.pts.gateway.security.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * GatewayApp is a class router.
 *
 * @author Natallia Paklonskaya
 * @since  2019-05-27
 *
 */

@EnableZuulProxy
@SpringBootApplication
@EnableEurekaClient
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

}
