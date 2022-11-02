package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    List<Reviews> findAllByMemberOrderByPostedAtDesc(Members Member);
    List<Reviews> findAllByCourse(Courses course);
    List<Reviews> findAllByCourseOrderByReviewIdDesc(Courses course);
    void deleteByCourse(Courses course);
    Long countByCourse(Courses courses);
    List<Reviews> findTop5ByStarOrderByReviewIdDesc(Long star);
}
