package com.sparta.coding_galaxy_be.controller;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.entity.MemberDetailsImpl;
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
    public ResponseEntity<?> createReview(@PathVariable(name = "course_id") Long courseId, @RequestBody ReviewRequestDto reviewRequestDto,
                                          @AuthenticationPrincipal MemberDetailsImpl memberDetailsimpl) {
        return reviewService.createReview(courseId, reviewRequestDto, memberDetailsimpl.getMember());
    }

    @PutMapping("/{course_id}/reviews/edit")
    public ResponseEntity<?> editReview(@PathVariable(name = "course_id") Long courseId, @RequestParam(value = "review-id") Long reviewId,
                                        @RequestBody ReviewRequestDto reviewRequestDto,
                                        @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return reviewService.editReview(courseId, reviewId, reviewRequestDto , memberDetails.getMember());
    }

    @DeleteMapping("/{course_id}/reviews/remove")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "course_id") Long courseId, @RequestParam(value = "review-id") Long reviewId,
                                          @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return reviewService.deleteReview(courseId, reviewId, memberDetails.getMember());
    }
}
