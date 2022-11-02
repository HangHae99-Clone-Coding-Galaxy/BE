package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    List<Reviews> findAllByMember(Members Member);
    List<Reviews> findAllByCourse(Courses course);
    void deleteByCourse(Courses course);
    Long countByCourse(Courses courses);
}
