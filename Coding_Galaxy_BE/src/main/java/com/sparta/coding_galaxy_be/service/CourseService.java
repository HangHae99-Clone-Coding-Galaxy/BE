package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseListResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.ReviewResponseDto;
import com.sparta.coding_galaxy_be.entity.*;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
import com.sparta.coding_galaxy_be.repository.PaymentRepository;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final S3UploadService s3UploadService;

    public ResponseEntity<?> getAllCourses() {

        List<Courses> coursesList = courseRepository.findAll();
        List<CourseListResponseDto> courseListResponseDtoList = new ArrayList<>();

        for (Courses course : coursesList) {

            Long totalStar = 0L;

            Long totalReview = reviewRepository.countByCourse(course);

            List<Reviews> reviewsList = reviewRepository.findAllByCourse(course);
            for (Reviews review : reviewsList) {
                totalStar = totalStar + review.getStar();
            }

            System.out.println("total star" + totalStar);
            System.out.println("total Review" + totalReview);

            Double starAverage = ((double) totalStar / (double) totalReview);
            if (totalReview == 0) starAverage = 0.0;

            CourseListResponseDto courseListResponseDto = CourseListResponseDto.builder()
                    .course_id(course.getCourseId())
                    .title(course.getTitle())
                    .content(course.getContent())
                    .thumbNail(course.getThumbNail())
                    .starAverage(starAverage)
                    .reviewCount(totalReview)
                    .build();

            courseListResponseDtoList.add(courseListResponseDto);
        }

        return new ResponseEntity<>(courseListResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> searchCourse(CourseListRequestDto courseListRequestDto) {

        List<Courses> coursesList = courseRepository.findAllByTitle(courseListRequestDto.getSearchText());
        List<CourseListResponseDto> courseListResponseDtoList = new ArrayList<>();

        Long totalStar = 0L;

        for (Courses course : coursesList) {

            Long totalReview = reviewRepository.countByCourse(course);

            List<Reviews> reviewsList = reviewRepository.findAllByCourse(course);
            for (Reviews review : reviewsList) {
                totalStar = totalStar + review.getStar();
            }

            Double starAverage = ((double) totalStar / (double) totalReview);
            if (totalReview == 0) starAverage = 0.0;

            CourseListResponseDto courseListResponseDto = CourseListResponseDto.builder()
                    .course_id(course.getCourseId())
                    .title(course.getTitle())
                    .thumbNail(course.getThumbNail())
                    .starAverage(starAverage)
                    .reviewCount(totalReview)
                    .build();

            courseListResponseDtoList.add(courseListResponseDto);
        }

        return new ResponseEntity<>(courseListResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getCourse(Long courseId, Members member) {

        Courses course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        Long totalStar = 0L;

        List<Reviews> reviewsList = reviewRepository.findAllByCourse(course);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (Reviews review : reviewsList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .review_id(review.getReviewId())
                            .nickname(review.getMember().getNickname())
                            .star(review.getStar())
                            .comment(review.getComment())
                            .build()
            );
            totalStar = totalStar + review.getStar();
        }

        Long totalReview = reviewRepository.countByCourse(course);
        Double starAverage = ((double) totalStar / (double) totalReview);
        if (totalReview == 0) starAverage = 0.0;

        CourseResponseDto courseResponseDto = CourseResponseDto.builder()
                .course_id(course.getCourseId())
                .title(course.getTitle())
                .content(course.getContent())
                .thumbNail(course.getThumbNail())
                .video(course.getVideo())
                .starAverage(starAverage)
                .price(course.getPrice())
                .paycheck(paymentRepository.existsByCourseAndMember(course, member))
                .reviewList(reviewResponseDtoList)
                .reviewCount(totalReview)
                .build();

        return new ResponseEntity<>(courseResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto, Members member) throws IOException {

        Courses course = Courses.builder()
                .title(courseRequestDto.getTitle())
                .content(courseRequestDto.getContent())
                .thumbNail(s3UploadService.uploadImage(courseRequestDto.getThumbNail()))
                .video(s3UploadService.uploadVideo(courseRequestDto.getVideo()))
                .price(courseRequestDto.getPrice())
                .member(member)
                .build();

        courseRepository.save(course);

        return new ResponseEntity<>("강의 등록이 완료되었습니다", HttpStatus.OK);
    }

    public ResponseEntity<?> getCourseForEdit(Long courseId, Members member) {

        Courses course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        if (!member.getEmail().equals(course.getMember().getEmail())) {
            throw new RuntimeException("작성자가 아닙니다.");
        }

        CourseResponseDto courseResponseDto = CourseResponseDto.builder()
                .course_id(courseId)
                .title(course.getTitle())
                .content(course.getContent())
                .thumbNail(course.getThumbNail())
                .video(course.getVideo())
                .build();

        return new ResponseEntity<>(courseResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editCourse(Long courseId, CourseRequestDto courseRequestDto, Members member){

        Courses course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        if (!member.getEmail().equals(course.getMember().getEmail())) {
            throw new RuntimeException("작성자가 아닙니다.");
        }

        if (courseRequestDto.getThumbNail() != null && courseRequestDto.getVideo() != null) {

            String thumbnailUrl = s3UploadService.uploadImage(courseRequestDto.getThumbNail());
            String videoUrl = s3UploadService.uploadVideo(courseRequestDto.getVideo());

            s3UploadService.delete(course.getThumbNail());
            s3UploadService.delete(course.getVideo());

            course.editCourseMedia(thumbnailUrl, videoUrl);
        }

        if (courseRequestDto.getTitle() != null && courseRequestDto.getContent() != null && courseRequestDto.getPrice() != 0) {
            course.editCourseDetail(courseRequestDto);
        }

        courseRepository.save(course);

        return new ResponseEntity<>("강의 수정이 완료되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteCourse(Long courseId, Members member) {

        Courses course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("강의가 존재하지 않습니다.")
        );

        if (!member.getEmail().equals(course.getMember().getEmail())) {
            throw new RuntimeException("작성자가 아닙니다.");
        }

        s3UploadService.delete(course.getThumbNail());
        s3UploadService.delete(course.getVideo());

        reviewRepository.deleteByCourse(course);

        courseRepository.deleteById(courseId);

        return new ResponseEntity<>("강의 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}