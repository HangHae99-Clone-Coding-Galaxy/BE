package com.sparta.coding_galaxy_be.controller;

import com.sparta.coding_galaxy_be.dto.requestDto.EditMyInfoRequestDto;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping
    public ResponseEntity<?> getMypage(@AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails){
        return mypageService.getMypage(kakaoMemberDetails.getKakaoMember());
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editMyInfo(@ModelAttribute EditMyInfoRequestDto editMyInfoRequestDto, @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) throws IOException {
        return mypageService.editMyInfo(editMyInfoRequestDto,kakaoMemberDetails.getKakaoMember());
    }

//    @GetMapping("/payment")
//    public ResponseEntity<?> getMyPayment(@AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails){
//        return mypageService.getMyPayment(kakaoMemberDetails.getKakaoMember());
//    }
//
//    @GetMapping("/reviews")
//    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails){
//        return mypageService.getMyReviews(kakaoMemberDetails.getKakaoMember());
//    }
}
