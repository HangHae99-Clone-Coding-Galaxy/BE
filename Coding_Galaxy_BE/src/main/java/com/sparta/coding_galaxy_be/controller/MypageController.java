package com.sparta.coding_galaxy_be.controller;

import com.sparta.coding_galaxy_be.dto.requestDto.EditMyInfoRequestDto;
import com.sparta.coding_galaxy_be.entity.MemberDetailsImpl;
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
    public ResponseEntity<?> getMypage(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return mypageService.getMypage(memberDetails.getMember());
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editMyInfo(@ModelAttribute EditMyInfoRequestDto editMyInfoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return mypageService.editMyInfo(editMyInfoRequestDto,memberDetails.getMember());
    }

    @GetMapping("/payment")
    public ResponseEntity<?> getMyPayment(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return mypageService.getMyPayment(memberDetails.getMember());
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return mypageService.getMyReviews(memberDetails.getMember());
    }
}
