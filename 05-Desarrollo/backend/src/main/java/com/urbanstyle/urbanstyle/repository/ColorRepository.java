package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
}