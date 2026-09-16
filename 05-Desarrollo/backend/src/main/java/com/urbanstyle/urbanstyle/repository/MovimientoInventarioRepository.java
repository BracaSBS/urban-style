package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario> findByVariante_IdVariante(Integer idVariante);

    List<MovimientoInventario> findByUsuario_IdUsuario(Integer idUsuario);
}