package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.entity.Courses;
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

    public ResponseEntity<?> createReview(Long courseId, ReviewRequestDto reviewRequestDto, KakaoMembers kakaoMember) {

        Courses course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        Reviews review = Reviews.builder()
                .star(reviewRequestDto.getStar())
                .comment(reviewRequestDto.getComment())
                .kakaoMember(kakaoMember)
                .course(course)
                .build();

        reviewRepository.save(review);

        return new ResponseEntity<>("리뷰가 등록되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editReview(Long courseId, Long reviewId, ReviewRequestDto reviewRequestDto, KakaoMembers kakaoMember) {

        if (!courseRepository.existsByCourseId(courseId)) throw new RuntimeException("강의가 존재하지 않습니다.");

        Reviews review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("리뷰가 존재하지 않습니다.")
        );

        if(!kakaoMember.getName().equals(review.getKakaoMember().getName())){
            throw new RuntimeException("작성자가 아닙니다.");
        }

        review.updateReview(reviewRequestDto);

        return new ResponseEntity<>("리뷰 수정이 완료되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteReview(Long courseId, Long reviewId, KakaoMembers kakaoMember) {

        if (!courseRepository.existsByCourseId(courseId)) throw new RuntimeException("강의가 존재하지 않습니다.");

        Reviews review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("리뷰가 존재하지 않습니다.")
        );

        if(!kakaoMember.getName().equals(review.getKakaoMember().getName())){
            throw new RuntimeException("작성자가 아닙니다.");
        }

        reviewRepository.deleteById(reviewId);

        return new ResponseEntity<>("리뷰 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
