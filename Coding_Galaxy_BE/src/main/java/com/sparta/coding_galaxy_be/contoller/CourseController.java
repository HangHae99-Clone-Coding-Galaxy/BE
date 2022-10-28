package com.sparta.coding_galaxy_be.contoller;


import com.sparta.coding_galaxy_be.dto.requestDto.CourseListRequestDto;
import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 검색엔진 사용?
    @GetMapping("/")
    public ResponseEntity<?> findSearchCourse(@ModelAttribute CourseListRequestDto courseListRequestDto) {
        return courseService.findSearchCourse(courseListRequestDto);

    }

    @GetMapping("/{course_id}")
    public ResponseEntity<?> findCourse(@PathVariable(name = "course_id") Long courseId) {
        return courseService.findCourse(courseId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCoures(@ModelAttribute CourseRequestDto courseRequestDto) {
        return courseService.createCourse(courseRequestDto);
    }

    @GetMapping("/{course_id}/edit")
    public ResponseEntity<?> findEditCourse(@PathVariable(name = "course_id") Long courseId) {
        return courseService.findEditCourse(courseId);
    }

    @PutMapping("/{course_id}/edit")
    public ResponseEntity<?> editCourse(@PathVariable(name = "course_id") Long courseId,
                                        CourseRequestDto courseRequestDto){
        return courseService.editCourse(courseId, courseRequestDto);
    }

    @DeleteMapping("/{course_id}")
    public ResponseEntity<?> removeCourse(@PathVariable(name = "course_id") Long courseId) {
        return courseService.removeCourse(courseId);
    }
}
