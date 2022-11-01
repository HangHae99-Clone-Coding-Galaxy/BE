package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    List<Reviews> findAllByKakaoMember(KakaoMembers kakaoMember);
    List<Reviews> findAllByCourse(Courses course);
    void deleteByCourse(Courses course);
    Long countByCourse(Courses courses);
}
