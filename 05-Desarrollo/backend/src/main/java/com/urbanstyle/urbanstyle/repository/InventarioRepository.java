package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    Optional<Inventario> findByVariante_IdVariante(Integer idVariante);
}