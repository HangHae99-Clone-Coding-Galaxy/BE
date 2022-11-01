package com.sparta.coding_galaxy_be.controller;


import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.entity.KakaoMemberDetailsImpl;
import com.sparta.coding_galaxy_be.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 검색엔진 사용?
    @GetMapping()
    public ResponseEntity<?> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCourse(@RequestBody CourseListRequestDto courseListRequestDto) {
        return courseService.searchCourse(courseListRequestDto);
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<?> getCourse(@PathVariable(name = "course_id") Long courseId, @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) {
        return courseService.getCourse(courseId, kakaoMemberDetails.getKakaoMember());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCoures(@ModelAttribute CourseRequestDto courseRequestDto,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) throws IOException {
        return courseService.createCourse(courseRequestDto, kakaoMemberDetails.getKakaoMember());
    }

    @GetMapping("/{course_id}/edit")
    public ResponseEntity<?> getCourseForEdit(@PathVariable(name = "course_id") Long courseId,
                                            @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) {
        return courseService.getCourseForEdit(courseId, kakaoMemberDetails.getKakaoMember());
    }

    @PutMapping("/{course_id}/edit")
    public ResponseEntity<?> editCourse(@PathVariable(name = "course_id") Long courseId,
                                        CourseRequestDto courseRequestDto,
                                        @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) throws IOException {
        return courseService.editCourse(courseId, courseRequestDto, kakaoMemberDetails.getKakaoMember());
    }

    @DeleteMapping("/{course_id}/remove")
    public ResponseEntity<?> deleteCourse(@PathVariable(name = "course_id") Long courseId,
                                          @AuthenticationPrincipal KakaoMemberDetailsImpl kakaoMemberDetails) {
        return courseService.deleteCourse(courseId, kakaoMemberDetails.getKakaoMember());
    }
}
