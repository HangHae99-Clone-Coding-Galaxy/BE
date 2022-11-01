package com.sparta.coding_galaxy_be.controller;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{course_id}/reviews/create")
    public ResponseEntity<?> createReview(@PathVariable Long courseId, @RequestBody ReviewRequestDto reviewRequestDto,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetailsimpl) {
        return reviewService.createReview(courseId, reviewRequestDto, kakaoMemberDetailsimpl.getKakaoMember());
    }

    @PutMapping("/reviews/{review_id}/edit")
    public ResponseEntity<?> editReview(@PathVariable Long reviewId,
                                        @RequestBody ReviewRequestDto reviewRequestDto,
                                        @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetailsimpl) {
        return reviewService.editReview(reviewId, reviewRequestDto ,kakaoMemberDetailsimpl.getKakaoMember());
    }

    @DeleteMapping("/reviews/{review_id}/remove")
    public ResponseEntity<?> removeReview(@PathVariable Long reviewId,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetailsimpl){
        return reviewService.removeReview(reviewId, kakaoMemberDetailsimpl.getKakaoMember());
    }
}
