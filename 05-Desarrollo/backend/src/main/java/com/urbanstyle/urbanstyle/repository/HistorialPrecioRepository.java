package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.HistorialPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialPrecioRepository extends JpaRepository<HistorialPrecio, Integer> {

    List<HistorialPrecio> findByProducto_IdProducto(Integer idProducto);

    List<HistorialPrecio> findByUsuario_IdUsuario(Integer idUsuario);
}