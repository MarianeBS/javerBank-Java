package com.msouza.client.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var status = 0;
        if (JwtUtils.isTokenValid(request.getHeader("Authorization"))) {
            status= 403;
        } else {
            status = 401;
        }

        log.info("Http Status {} {}",status, authException.getMessage());
        response.setHeader("www-authenticate", "Bearer realm='/api/v1/auth'");
        response.sendError(status);
    }
}
