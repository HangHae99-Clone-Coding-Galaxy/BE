package com.sparta.coding_galaxy_be.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.coding_galaxy_be.dto.requestDto.KakaoPayRequestDto;
import com.sparta.coding_galaxy_be.entity.MemberDetailsImpl;
import com.sparta.coding_galaxy_be.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/api/courses/{course_id}/order")
    public ResponseEntity<?> payment(@PathVariable(name = "course_id") Long courseId, @AuthenticationPrincipal MemberDetailsImpl memberDetailsimpl){
        return payService.payment(courseId, memberDetailsimpl.getMember());
    }

    @GetMapping("/api/order/{course_id}/kakao")
    public ResponseEntity<?> paymentReady(@PathVariable(name = "course_id") Long courseId, @AuthenticationPrincipal MemberDetailsImpl memberDetailsimpl) throws JsonProcessingException {
        return payService.paymentReady(courseId, memberDetailsimpl.getMember());
    }

    @GetMapping("/api/order/{order_id}/kakao/approval")
    public ResponseEntity<?> paymentRequest(@RequestBody KakaoPayRequestDto kakaoPayRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetailsimpl) throws JsonProcessingException {
        return payService.paymentRequest(kakaoPayRequestDto, memberDetailsimpl.getMember());
    }
}
