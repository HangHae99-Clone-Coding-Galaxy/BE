package com.sparta.coding_galaxy_be.repository;

import com.sparta.coding_galaxy_be.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {
}
