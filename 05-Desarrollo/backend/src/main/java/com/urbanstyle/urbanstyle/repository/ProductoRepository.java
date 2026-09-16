package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByReferencia(String referencia);
}