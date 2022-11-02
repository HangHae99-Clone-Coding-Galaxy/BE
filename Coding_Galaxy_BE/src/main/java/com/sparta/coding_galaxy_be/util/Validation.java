package com.sparta.coding_galaxy_be.util;

import com.sparta.coding_galaxy_be.entity.Authority;
import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.Reviews;
import com.sparta.coding_galaxy_be.exception.CustomExceptions;
import com.sparta.coding_galaxy_be.repository.CourseRepository;
import com.sparta.coding_galaxy_be.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validation {

    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;

    public void validateExistsCourse(Long courseId){
        if (!courseRepository.existsByCourseId(courseId)) {
            throw new CustomExceptions.NotFoundCourseException();
        }
    }

    public void validateWriterOfReview(Reviews review, Members member){
        if(!member.getEmail().equals(review.getMember().getEmail())){
            throw new CustomExceptions.NotValidWriterException();
        }
    }

    public void validateWriterOfCourse(Courses course, Members member){
        if (!member.getEmail().equals(course.getMember().getEmail())) {
            throw new CustomExceptions.NotValidWriterException();
        }
    }

    public void validateAdmin(Members member){
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN)){
            throw new CustomExceptions.NotValidAdminException();
        }
    }

    public Courses validateCourse(Long courseId){
        return courseRepository.findById(courseId).orElseThrow(
                CustomExceptions.NotFoundCourseException::new
        );
    }

    public Reviews validateReview(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(
                CustomExceptions.NotFoundReviewException::new
        );
    }
}
