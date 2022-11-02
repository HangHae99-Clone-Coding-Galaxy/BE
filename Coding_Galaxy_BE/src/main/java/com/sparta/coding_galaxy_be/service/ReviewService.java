package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.Reviews;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import com.sparta.coding_galaxy_be.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final Validation validation;

    public ResponseEntity<?> createReview(Long courseId, ReviewRequestDto reviewRequestDto, Members member) {

        Courses course = validation.validateCourse(courseId);

        Reviews review = Reviews.builder()
                .star(reviewRequestDto.getStar())
                .comment(reviewRequestDto.getComment())
                .member(member)
                .course(course)
                .build();

        reviewRepository.save(review);

        return new ResponseEntity<>("리뷰가 등록되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editReview(Long courseId, Long reviewId, ReviewRequestDto reviewRequestDto, Members member) {

        validation.validateExistsCourse(courseId);

        Reviews review = validation.validateReview(reviewId);

        validation.validateWriterOfReview(review, member);

        review.updateReview(reviewRequestDto);

        return new ResponseEntity<>("리뷰 수정이 완료되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteReview(Long courseId, Long reviewId, Members member) {

        validation.validateExistsCourse(courseId);

        Reviews review = validation.validateReview(reviewId);

        validation.validateWriterOfReview(review, member);

        reviewRepository.deleteById(reviewId);

        return new ResponseEntity<>("리뷰 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
