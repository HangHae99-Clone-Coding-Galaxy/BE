package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.entity.Reviews;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ResponseEntity<?> createReview(ReviewRequestDto reviewRequestDto) {

        Reviews review = Reviews.builder().
                star(reviewRequestDto.getStar()).
                comment(reviewRequestDto.getComment()).
                build();

        reviewRepository.save(review);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editReview(Long reviewId, ReviewRequestDto reviewRequestDto) {

        Reviews updateReview = reviewRepository.findById(reviewId).orElseThrow(()
            -> new NullPointerException("리뷰가 없습니다."));

        // 작성자와 로그인 사용자 비교 로직
        updateReview.updateReview(
                reviewRequestDto.getStar(),
                reviewRequestDto.getComment()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> removeReview(Long reviewId) {

        reviewRepository.deleteById(reviewId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
