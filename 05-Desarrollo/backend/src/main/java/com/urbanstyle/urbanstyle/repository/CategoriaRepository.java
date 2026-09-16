package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}