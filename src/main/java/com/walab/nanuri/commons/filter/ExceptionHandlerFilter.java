package com.walab.nanuri.commons.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walab.nanuri.commons.exception.DoNotLoginException;
import com.walab.nanuri.commons.exception.WrongTokenException;
import com.walab.nanuri.commons.exception.dto.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (DoNotLoginException e) {
            // 토큰의 유효기간 만료
            setErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (WrongTokenException e) {
            // 유효하지 않은 토큰
            setErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response, String message, HttpStatus httpStatus) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ExceptionResponse exceptionResponse =
                ExceptionResponse.builder().error(httpStatus.getReasonPhrase()).message(message).build();
        try {
            response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}