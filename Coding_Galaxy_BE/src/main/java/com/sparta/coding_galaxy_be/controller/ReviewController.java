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
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{course_id}")
    public ResponseEntity<?> createReview(@PathVariable Long courseId, @RequestBody ReviewRequestDto reviewRequestDto,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetailsimpl) {
        return reviewService.createReview(courseId, reviewRequestDto, kakaoMemberDetailsimpl.getKakaoMember());
    }

    @PutMapping("/{review_id}")
    public ResponseEntity<?> editReview(@PathVariable Long reviewId,
                                        @RequestBody ReviewRequestDto reviewRequestDto) {
        return reviewService.editReview(reviewId, reviewRequestDto);
    }

    @DeleteMapping("/{review_id}")
    public ResponseEntity<?> removeReview(@PathVariable Long reviewId){
        return reviewService.removeReview(reviewId);
    }
}
