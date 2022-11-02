package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByKakaoMemberId(Long kakaoMemberId);
    Optional<Members> findByEmail(String email);
    boolean existsByEmail(String email);
}
