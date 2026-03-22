package com.epam.gym.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Slf4j
public class RestLoggingFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request,1024 * 1024    );

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String requestBody = new String(requestWrapper.getContentAsByteArray());
        String responseBody = new String(responseWrapper.getContentAsByteArray());

        log.info("Request: {} {} Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                requestBody);

        log.info("Response: Status: {} Body: {}",
                response.getStatus(),
                responseBody);

        responseWrapper.copyBodyToResponse();
    }
}