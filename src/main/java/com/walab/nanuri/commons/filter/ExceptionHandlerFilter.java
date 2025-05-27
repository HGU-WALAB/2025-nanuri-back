package com.walab.nanuri.commons.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

            filterChain.doFilter(request, response);
    }
}