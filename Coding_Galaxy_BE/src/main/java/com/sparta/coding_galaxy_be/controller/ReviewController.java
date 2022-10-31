package com.sparta.coding_galaxy_be.controller;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        return reviewService.createReview(reviewRequestDto);
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
