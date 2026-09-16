package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {

    List<Devolucion> findByPedido_IdPedido(Integer idPedido);

    List<Devolucion> findByUsuario_IdUsuario(Integer idUsuario);
}