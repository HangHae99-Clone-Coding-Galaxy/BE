package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseListResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseResponseDto;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.Reviews;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final S3UploadService s3UploadService;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    public ResponseEntity<?> findSearchCourse(CourseListRequestDto courseListRequestDto) {

        List<Courses> findCourseList;

        List<CourseListResponseDto> courseList = new ArrayList<>();

        // 검색어
        String searchText = courseListRequestDto.getSearchText();
        // 추후 queryDsl? 사용!
        // 추후 paging?
        findCourseList = courseRepository.findAll();

        for (Courses course : findCourseList) {
            // Formula 사용해보기 ( for문 카운트는 너무 리소스 많이 잡아 먹음 )
            CourseListResponseDto courseListResponseDto = CourseListResponseDto.builder().
                    courseId(course.getCourseId()).
                    title(course.getTitle()).
                    content(course.getContent()).
                    thumbNail(course.getThumbNail()).
                    video(course.getVideo()).
                    build();

            courseList.add(courseListResponseDto);
        }

        return new ResponseEntity<>(courseList, HttpStatus.OK);

    }

    public ResponseEntity<?> findCourse(Long courseId) {

        Courses findCourse = courseRepository.findById(courseId).orElse(null);
        List<Reviews> reviewList = reviewRepository.findAllByCourse(findCourse);

        double starAverage = 0;

        for (Reviews reviews : reviewList) {
            starAverage += (double) reviews.getStar();
        }

        starAverage /= reviewList.size();

        // NULL 예외처리 추가!

        CourseResponseDto course = CourseResponseDto.builder().
                courseId(findCourse.getCourseId()).
                title(findCourse.getTitle()).
                content(findCourse.getContent()).
                thumbNail(findCourse.getThumbNail()).
                video(findCourse.getVideo()).
                reviewList(reviewList).
                starAverage(starAverage).
                nickname(findCourse.getKakaoMember().getNickname()).
                build();

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    public ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto, KakaoMembers kakaoMember) throws IOException {

        // NULL 체크 예외처리!

        // S3 이용 추가! image쪽
        Courses course = Courses.builder().
                title(courseRequestDto.getTitle()).
                content(courseRequestDto.getContent()).
                thumbNail(s3UploadService.upload(courseRequestDto.getThumbNail(), dir)).
                video(s3UploadService.upload(courseRequestDto.getVideo(), dir)).
                build();

        courseRepository.save(course);

        // HttpStatus.CREATED 201 성공?
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findEditCourse(Long courseId, KakaoMembers kakaoMember) {

        Courses findCourse = courseRepository.findById(courseId).orElse(null);

        // NULL 예외처리 추가!

        CourseResponseDto course = CourseResponseDto.builder().
                courseId(courseId).
                title(findCourse.getTitle()).
                content(findCourse.getContent()).
                thumbNail(findCourse.getThumbNail()).
                video(findCourse.getVideo()).
                kakaoMember(kakaoMember.getNickname()).
                build();

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editCourse(Long courseId, CourseRequestDto courseRequestDto, KakaoMembers kakaoMember) {

        Courses updateCourse = courseRepository.findById(courseId).orElseThrow(()
        -> new NullPointerException("msg"));

        // NULL 예외처리 추가!
        // 작성자와 로그인 사용자 일치여부 예외처리 추가!

        updateCourse.updateCourse(
                courseRequestDto.getTitle(),
                courseRequestDto.getContent());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> removeCourse(Long courseId, KakaoMembers kakaoMember) {

        // 작성자와 로그인 사용자 일치여부 예외처리 추가!
        // review, subscribe 삭제 추가!

        Courses course = courseRepository.findById(courseId).orElseThrow();

        if(reviewRepository.existsByCourse(course))
            reviewRepository.deleteByCourse(course);

        courseRepository.deleteById(courseId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
