package com.sparta.coding_galaxy_be.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.coding_galaxy_be.dto.requestDto.MemberRequestDto;
import com.sparta.coding_galaxy_be.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto){
        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/api/member/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse httpServletResponse){
        return memberService.login(memberRequestDto, httpServletResponse);
    }

    @GetMapping("/api/member/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse httpServletResponse) throws JsonProcessingException{
        return memberService.kakaoLogin(code, httpServletResponse);
    }
}
