package com.sparta.coding_galaxy_be.controller;


import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 검색엔진 사용?
    @GetMapping("")
    public ResponseEntity<?> findSearchCourse(@ModelAttribute CourseListRequestDto courseListRequestDto) {
        return courseService.findSearchCourse(courseListRequestDto);

    }

    @GetMapping("/{course_id}")
    public ResponseEntity<?> findCourse(@PathVariable(name = "course_id") Long courseId) {
        return courseService.findCourse(courseId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCoures(@ModelAttribute CourseRequestDto courseRequestDto,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) throws IOException {
        return courseService.createCourse(courseRequestDto, kakaoMemberDetails.getKakaoMember());
    }

    @GetMapping("/{course_id}/edit")
    public ResponseEntity<?> findEditCourse(@PathVariable(name = "course_id") Long courseId,
                                            @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) {
        return courseService.findEditCourse(courseId, kakaoMemberDetails.getKakaoMember());
    }

    @PutMapping("/{course_id}/edit")
    public ResponseEntity<?> editCourse(@PathVariable(name = "course_id") Long courseId,
                                        CourseRequestDto courseRequestDto,
                                        @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails){
        return courseService.editCourse(courseId, courseRequestDto, kakaoMemberDetails.getKakaoMember());
    }

    @DeleteMapping("/{course_id}")
    public ResponseEntity<?> removeCourse(@PathVariable(name = "course_id") Long courseId,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) {
        return courseService.removeCourse(courseId, kakaoMemberDetails.getKakaoMember());
    }
}
