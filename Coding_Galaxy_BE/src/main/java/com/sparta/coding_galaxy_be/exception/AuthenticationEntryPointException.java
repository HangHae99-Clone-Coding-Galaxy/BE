package com.sparta.coding_galaxy_be.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.coding_galaxy_be.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationEntryPointException implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        new ResponseEntity<>(new ErrorMessage("BAD_REQUEST", "잘못된 접근입니다."), HttpStatus.BAD_REQUEST)
                )
        );
    }
}
