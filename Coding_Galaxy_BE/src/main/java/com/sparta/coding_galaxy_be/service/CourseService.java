package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.AllCoursesResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseListResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.ReviewResponseDto;
import com.sparta.coding_galaxy_be.entity.*;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
import com.sparta.coding_galaxy_be.repository.PaymentRepository;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import com.sparta.coding_galaxy_be.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final S3UploadService s3UploadService;
    private final Validation validation;

    public ResponseEntity<?> getAllCourses() {

        List<Courses> coursesList = courseRepository.findAll();

        List<Reviews> reviewsList = reviewRepository.findTop5ByStarOrderByReviewIdDesc(5L);

        AllCoursesResponseDto allCoursesResponseDto = AllCoursesResponseDto.builder()
                .courseListResponseDtoList(getCourseListResponseDto(coursesList))
                .reviewList(getReviewResponseDtoList(reviewsList))
                .build();

        return new ResponseEntity<>(allCoursesResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> searchCourse(CourseListRequestDto courseListRequestDto) {

        List<Courses> coursesList = courseRepository.findAllByTitle(courseListRequestDto.getSearchText());

        return new ResponseEntity<>(getCourseListResponseDto(coursesList), HttpStatus.OK);
    }

    public ResponseEntity<?> getCourse(Long courseId, Members member) {

        Courses course = validation.validateCourse(courseId);

        List<Reviews> reviewsList = reviewRepository.findAllByCourseOrderByReviewIdDesc(course);

        Long totalReview = reviewRepository.countByCourse(course);

        CourseResponseDto courseResponseDto = CourseResponseDto.builder()
                .course_id(course.getCourseId())
                .title(course.getTitle())
                .content(course.getContent())
                .thumbNail(course.getThumbNail())
                .video(course.getVideo())
                .starAverage(getStarAverage(course, totalReview))
                .price(course.getPrice())
                .paycheck(validation.validatePaycheck(course, member))
                .reviewList(getReviewResponseDtoList(reviewsList))
                .reviewCount(totalReview)
                .build();

        return new ResponseEntity<>(courseResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto, Members member) {

//        validation.validateAdmin(member);

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

//        validation.validateAdmin(member);

        Courses course = validation.validateCourse(courseId);

        validation.validateWriterOfCourse(course, member);

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

//        validation.validateAdmin(member);

        Courses course = validation.validateCourse(courseId);

        validation.validateWriterOfCourse(course, member);

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

//        validation.validateAdmin(member);

        Courses course = validation.validateCourse(courseId);

        validation.validateWriterOfCourse(course, member);

        s3UploadService.delete(course.getThumbNail());
        s3UploadService.delete(course.getVideo());

        reviewRepository.deleteByCourse(course);

        courseRepository.deleteById(courseId);

        return new ResponseEntity<>("강의 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    public List<CourseListResponseDto> getCourseListResponseDto (List<Courses> coursesList){

        List<CourseListResponseDto> courseListResponseDtoList = new ArrayList<>();

        for (Courses course : coursesList) {

            Long totalReview = reviewRepository.countByCourse(course);

            CourseListResponseDto courseListResponseDto = CourseListResponseDto.builder()
                    .course_id(course.getCourseId())
                    .title(course.getTitle())
                    .thumbNail(course.getThumbNail())
                    .starAverage(getStarAverage(course, totalReview))
                    .reviewCount(totalReview)
                    .build();

            courseListResponseDtoList.add(courseListResponseDto);
        }
        return courseListResponseDtoList;
    }

    public List<ReviewResponseDto> getReviewResponseDtoList (List<Reviews> reviewsList){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (Reviews review : reviewsList){
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .review_id(review.getReviewId())
                            .nickname(review.getMember().getNickname())
                            .star(review.getStar())
                            .comment(review.getComment())
                            .build()
            );
        }
        return reviewResponseDtoList;
    }

    public Double getStarAverage(Courses course, Long totalReview){

        Long totalStar = 0L;

        List<Reviews> reviewsList = reviewRepository.findAllByCourse(course);
        for (Reviews review : reviewsList){
            totalStar = totalStar + review.getStar();
        }

        Double starAverage = ((double) totalStar / (double) totalReview);
        if (totalReview == 0) starAverage = 0.0;

        return starAverage;
    }
}