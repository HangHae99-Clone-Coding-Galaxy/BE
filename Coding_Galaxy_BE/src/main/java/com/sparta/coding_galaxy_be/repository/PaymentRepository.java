package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Courses;
import com.sparta.coding_galaxy_be.entity.Members;
import com.sparta.coding_galaxy_be.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payments, String> {

    List<Payments> findAllByMemberOrderByCreatedAtDesc(Members member);
    List<Payments> findAllByCourseAndMember(Courses course, Members member);
}
