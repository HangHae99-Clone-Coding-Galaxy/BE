package com.sparta.coding_galaxy_be.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.coding_galaxy_be.dto.requestDto.KakaoPayRequestDto;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    //tid 및 redirect_url 반환
    @GetMapping("/api/order/{course_id}")
    public ResponseEntity<?> paymentReady(@PathVariable Long course_id, @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetailsimpl) throws JsonProcessingException {
        return kakaoPayService.paymentReady(course_id, kakaoMemberDetailsimpl.getKakaoMember());
    }

    //orderList, tid, pg_token을 parameter 로 받아 결제 완료 정보 넘겨야함 (RequestBody가 좋을지 RequestParam이 좋을지 협의 필요)
    @GetMapping("/api/order/{orderList}/approval")
    public ResponseEntity<?> paymentRequest(@RequestBody KakaoPayRequestDto kakaoPayRequestDto, @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetailsimpl) throws JsonProcessingException {
        return kakaoPayService.paymentRequest(kakaoPayRequestDto, kakaoMemberDetailsimpl.getKakaoMember());
    }



}
