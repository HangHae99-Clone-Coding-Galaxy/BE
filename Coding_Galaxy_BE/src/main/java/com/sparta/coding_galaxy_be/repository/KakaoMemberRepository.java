package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.KakaoMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoMemberRepository extends JpaRepository<KakaoMembers, Long> {

    Optional<KakaoMembers> findByNickname(String nickname);
}
