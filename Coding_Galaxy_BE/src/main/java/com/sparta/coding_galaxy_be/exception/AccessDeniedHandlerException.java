package com.sparta.coding_galaxy_be.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.coding_galaxy_be.entity.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedHandlerException implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        new ResponseEntity<>(new Error("BAD_REQUEST", "로그인이 필요합니다."), HttpStatus.BAD_REQUEST)
                )
        );
    }
}
