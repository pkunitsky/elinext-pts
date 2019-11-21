/*
 * Copyright (c) 2019. Elinext.
 */

package com.pts.gateway.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.net.HttpHeaders.*;
import static javax.ws.rs.HttpMethod.*;

/**
 * The type Cors filter.
 *
 * @author Denis Senchenko
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
    private static final String DELIMITER = ", ";
    private static final String ACCESS_AGE = "3600";
    private static final String ALLOW_ORIGIN = "http://localhost:8081";
    private final Environment environment;

    /**
     * Instantiates a new Cors filter.
     *
     * @param environment the environment
     */
    public CorsFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ALLOW_ORIGIN);
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, String.join(DELIMITER, POST, GET, PUT, OPTIONS, DELETE));
        response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString());
        response.setHeader(ACCESS_CONTROL_MAX_AGE, ACCESS_AGE);
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, String.join(DELIMITER, ORIGIN, AUTHORIZATION, X_REQUESTED_WITH, CONTENT_TYPE, ACCEPT));

        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
}
