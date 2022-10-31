package com.sparta.coding_galaxy_be.service;

import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseListResponseDto;
import com.sparta.coding_galaxy_be.dto.responseDto.CourseResponseDto;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
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

    public ResponseEntity<?> findSearchCourse(CourseListRequestDto courseListRequestDto) {

        List<Courses> findCourseList;

        List<CourseListResponseDto> courseList = new ArrayList<>();

        // 추후 queryDsl? 사용!
        // 추후 paging?
        if(courseListRequestDto.getSearchText().equals(""))
            findCourseList = courseRepository.findAll();
        else
            findCourseList = courseRepository.findAllByTitle(courseListRequestDto.getSearchText());

        for (Courses course : findCourseList) {
            CourseListResponseDto courseListResponseDto = CourseListResponseDto.builder().
                    courseId(course.getCourseId()).
                    title(course.getTitle()).
                    content(course.getContent()).
                    imageList(course.getImage()).
                    video(course.getVideo()).
                    build();

            courseList.add(courseListResponseDto);
        }

        return new ResponseEntity<>(courseList, HttpStatus.OK);

    }

    public ResponseEntity<?> findCourse(Long courseId) {

        Courses findCourse = courseRepository.findById(courseId).orElse(null);

        // NULL 예외처리 추가!

        CourseResponseDto course = CourseResponseDto.builder().
                courseId(findCourse.getCourseId()).
                title(findCourse.getTitle()).
                content(findCourse.getContent()).
                image(findCourse.getImage()).
                video(findCourse.getVideo()).
                // reviewList 추가!
                subscribeCount(1L).
                // 구독 추가!
                nickname("admin").
                // 작성자 추가!
                build();

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    public ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto) {

        // NULL 체크 예외처리!

        // S3 이용 추가! image쪽
        Courses course = Courses.builder().
                title(courseRequestDto.getTitle()).
                content(courseRequestDto.getContent()).
                image(courseRequestDto.getImage()).
                video(courseRequestDto.getVideo()).
                build();

        courseRepository.save(course);

        // HttpStatus.CREATED 201 성공?
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findEditCourse(Long courseId) {

        Courses findCourse = courseRepository.findById(courseId).orElse(null);

        // NULL 예외처리 추가!

        CourseResponseDto course = CourseResponseDto.builder().
                courseId(courseId).
                title(findCourse.getTitle()).
                content(findCourse.getContent()).
                image(findCourse.getImage()).
                video(findCourse.getVideo()).
                build();

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editCourse(Long courseId, CourseRequestDto courseRequestDto) {

        Courses updateCourse = courseRepository.findById(courseId).orElseThrow(()
        -> new NullPointerException("msg"));

        // NULL 예외처리 추가!
        // 작성자와 로그인 사용자 일치여부 예외처리 추가!

        updateCourse.updateCourse(
                courseRequestDto.getTitle(),
                courseRequestDto.getContent(), 
                courseRequestDto.getImage(), 
                courseRequestDto.getVideo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> removeCourse(Long courseId) {

        // 작성자와 로그인 사용자 일치여부 예외처리 추가!
        // review, subscribe 삭제 추가!

        courseRepository.deleteById(courseId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
