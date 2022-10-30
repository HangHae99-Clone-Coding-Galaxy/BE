package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
