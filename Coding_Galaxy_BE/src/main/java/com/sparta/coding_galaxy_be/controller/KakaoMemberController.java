package com.sparta.coding_galaxy_be.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.coding_galaxy_be.service.KakaoMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class KakaoMemberController {

    private final KakaoMemberService kakaoMemberService;

    @GetMapping("/api/member/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse httpServletResponse) throws JsonProcessingException{
        return kakaoMemberService.kakaoLogin(code, httpServletResponse);
    }
}
