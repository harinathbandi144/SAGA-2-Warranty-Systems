package com.saga.claim_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
    @Component
    public class HttpStatusDebugFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain)
                throws ServletException, IOException {

            System.out.println("BEFORE: " + request.getRequestURI());

            filterChain.doFilter(request, response);

            System.out.println("AFTER: HTTP " + response.getStatus());
        }
    }

