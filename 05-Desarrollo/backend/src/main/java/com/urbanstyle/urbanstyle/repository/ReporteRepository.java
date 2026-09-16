package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Pedido, Integer> {

    @Query("""
        SELECT COALESCE(SUM(p.total), 0)
        FROM Pedido p
        WHERE p.estado = 'CONFIRMADO'
        AND p.fechaConfirmacion >= :inicio
        AND p.fechaConfirmacion < :fin
    """)
    BigDecimal ventasConfirmadasEntre(LocalDateTime inicio, LocalDateTime fin);

    @Query("""
        SELECT COUNT(p)
        FROM Pedido p
        WHERE p.estado = 'CONFIRMADO'
        AND p.fechaConfirmacion >= :inicio
        AND p.fechaConfirmacion < :fin
    """)
    Long pedidosConfirmadosEntre(LocalDateTime inicio, LocalDateTime fin);

    @Query("""
        SELECT p.estado, COUNT(p)
        FROM Pedido p
        GROUP BY p.estado
    """)
    List<Object[]> pedidosPorEstado();

    @Query("""
        SELECT p.vendedor.idUsuario, p.vendedor.nombre,
               COUNT(p), COALESCE(SUM(p.total), 0)
        FROM Pedido p
        WHERE p.estado = 'CONFIRMADO'
        GROUP BY p.vendedor.idUsuario, p.vendedor.nombre
        ORDER BY SUM(p.total) DESC
    """)
    List<Object[]> ventasPorVendedor();

    @Query("""
        SELECT d.variante.producto.idProducto,
               d.variante.producto.nombre,
               SUM(d.cantidad)
        FROM DetallePedido d
        WHERE d.pedido.estado = 'CONFIRMADO'
        GROUP BY d.variante.producto.idProducto,
                 d.variante.producto.nombre
        ORDER BY SUM(d.cantidad) DESC
    """)
    List<Object[]> productosMasVendidos();

    @Query("""
        SELECT d.variante.talla.idTalla,
               d.variante.talla.nombre,
               SUM(d.cantidad)
        FROM DetallePedido d
        WHERE d.pedido.estado = 'CONFIRMADO'
        GROUP BY d.variante.talla.idTalla,
                 d.variante.talla.nombre
        ORDER BY SUM(d.cantidad) DESC
    """)
    List<Object[]> tallasMasVendidas();

    @Query("""
        SELECT d.variante.color.idColor,
               d.variante.color.nombre,
               SUM(d.cantidad)
        FROM DetallePedido d
        WHERE d.pedido.estado = 'CONFIRMADO'
        GROUP BY d.variante.color.idColor,
                 d.variante.color.nombre
        ORDER BY SUM(d.cantidad) DESC
    """)
    List<Object[]> coloresMasVendidos();
}