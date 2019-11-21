/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RestApp {
    public static void main(String[] args) {
        SpringApplication.run(RestApp.class, args);
    }
}