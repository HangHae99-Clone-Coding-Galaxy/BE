package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import com.sparta.coding_galaxy_be.entity.Payment;
import com.sparta.coding_galaxy_be.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByKakaoMember(KakaoMembers kakaoMember);
}
