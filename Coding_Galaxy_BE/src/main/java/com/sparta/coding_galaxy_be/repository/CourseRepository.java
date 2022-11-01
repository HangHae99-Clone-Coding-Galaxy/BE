package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Courses, Long> {

    List<Courses> findAllByTitle(String searchText);
    boolean existsByCourseId(Long courseId);
}
