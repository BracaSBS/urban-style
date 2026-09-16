package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByEstado(String estado);

    List<Pedido> findByVendedor_IdUsuario(Integer idUsuario);
}