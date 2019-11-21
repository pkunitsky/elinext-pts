package com.pts.gateway.security;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public class AuthFilter extends ZuulFilter {

    private static final String jwkAddress = "https://auth.elinext.com/.well-known/openid-configuration/jwks";
    private static final String KEY_ROLE = "http://schemas.microsoft.com/ws/2008/06/identity/claims/role";
    private static final String KEY_EMAIL = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress";
    private static final String ADMIN = "ADMIN";
    private static final String MANAGER = "MANAGER";
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String ROLE = "Role";
    private static final String EMAIL = "Email";
    private static final String AUTHORIZATION = "Authorization";
    private JwtDecoder decoder = new NimbusJwtDecoderJwkSupport(jwkAddress);

    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    public boolean shouldFilter() {
        return true;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String accessTokenValue = request.getHeader(AUTHORIZATION).substring(7);
        Jwt accessToken = decoder.decode(accessTokenValue);
        Map<String, Object> claims = accessToken.getClaims();
        fillInRole(ctx, claims);
        String email = (String) claims.get(KEY_EMAIL);
        ctx.getZuulRequestHeaders().put(EMAIL, email);
        return null;
    }

    private void fillInRole(RequestContext ctx, Map<String, Object> claims) {
        Map<String, String> headers = ctx.getZuulRequestHeaders();
        if (claims.containsKey(KEY_ROLE)) {
            String[] roles = claims.get(KEY_ROLE).toString().split(",");
            if (roles.length > 0) {
                for (String s : roles) {
                    if (s.contains(ADMIN)) {
                        headers.put(ROLE, ADMIN);
                        break;
                    } else if (s.contains(MANAGER)) {
                        headers.put(ROLE, MANAGER);
                    }
                }
            }
        } else {
             headers.put(ROLE, EMPLOYEE);
        }
    }
}

