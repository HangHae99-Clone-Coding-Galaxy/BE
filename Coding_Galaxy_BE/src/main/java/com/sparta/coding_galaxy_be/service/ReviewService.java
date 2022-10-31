package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.Reviews;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
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
    private final CourseRepository courseRepository;

    public ResponseEntity<?> createReview(Long courseId, ReviewRequestDto reviewRequestDto, KakaoMembers kakaoMembers) {

        Courses course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        Reviews review = Reviews.builder().
                star(reviewRequestDto.getStar()).
                comment(reviewRequestDto.getComment()).
                kakaoMember(kakaoMembers).
                course(course).
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
