package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.DevolucionRequest;
import com.urbanstyle.urbanstyle.entity.Devolucion;
import com.urbanstyle.urbanstyle.entity.DetallePedido;
import com.urbanstyle.urbanstyle.entity.Inventario;
import com.urbanstyle.urbanstyle.entity.MovimientoInventario;
import com.urbanstyle.urbanstyle.entity.Pedido;
import com.urbanstyle.urbanstyle.entity.Usuario;
import com.urbanstyle.urbanstyle.repository.DevolucionRepository;
import com.urbanstyle.urbanstyle.repository.DetallePedidoRepository;
import com.urbanstyle.urbanstyle.repository.InventarioRepository;
import com.urbanstyle.urbanstyle.repository.MovimientoInventarioRepository;
import com.urbanstyle.urbanstyle.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final UsuarioRepository usuarioRepository;

    public DevolucionService(
            DevolucionRepository devolucionRepository,
            DetallePedidoRepository detallePedidoRepository,
            InventarioRepository inventarioRepository,
            MovimientoInventarioRepository movimientoInventarioRepository,
            UsuarioRepository usuarioRepository) {

        this.devolucionRepository = devolucionRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Devolucion> listarTodas() {
        return devolucionRepository.findAll();
    }

    public List<Devolucion> listarPorPedido(Integer idPedido) {
        return devolucionRepository.findByPedido_IdPedido(idPedido);
    }

    public List<Devolucion> listarPorUsuario(Integer idUsuario) {
        return devolucionRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Transactional
    public Devolucion registrarDevolucion(DevolucionRequest request) {

        DetallePedido detalle = detallePedidoRepository.findById(request.getIdDetalle())
                .orElseThrow(() ->
                        new IllegalArgumentException("El detalle del pedido indicado no existe"));

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() ->
                        new IllegalArgumentException("El usuario indicado no existe"));

        Pedido pedido = detalle.getPedido();

        if (!"CONFIRMADO".equals(pedido.getEstado())
                && !"EN_PREPARACION".equals(pedido.getEstado())
                && !"ENVIADO".equals(pedido.getEstado())
                && !"ENTREGADO".equals(pedido.getEstado())) {

            throw new IllegalArgumentException(
                    "El pedido no se encuentra en un estado válido para devolución");
        }

        if (request.getCantidad() > detalle.getCantidad()) {
            throw new IllegalArgumentException(
                    "La cantidad devuelta no puede superar la cantidad comprada");
        }

        Devolucion devolucion = new Devolucion();

        devolucion.setPedido(pedido);
        devolucion.setDetalle(detalle);
        devolucion.setUsuario(usuario);
        devolucion.setCantidad(request.getCantidad());
        devolucion.setMotivo(request.getMotivo());
        devolucion.setEstadoProducto(request.getEstadoProducto());
        devolucion.setVuelveInventario(request.getVuelveInventario());
        devolucion.setObservaciones(request.getObservaciones());
        devolucion.setFecha(LocalDateTime.now());

        if (Boolean.TRUE.equals(request.getVuelveInventario())) {

            Inventario inventario = inventarioRepository
                    .findByVariante_IdVariante(
                            detalle.getVariante().getIdVariante())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "La variante no tiene inventario registrado"));

            int stockAnterior = inventario.getStockActual();
            int stockPosterior = stockAnterior + request.getCantidad();

            inventario.setStockActual(stockPosterior);
            inventarioRepository.save(inventario);

            MovimientoInventario movimiento = new MovimientoInventario();

            movimiento.setVariante(detalle.getVariante());
            movimiento.setUsuario(usuario);
            movimiento.setTipoMovimiento("DEVOLUCION");
            movimiento.setCantidad(request.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockPosterior(stockPosterior);
            movimiento.setMotivo(
                    "Devolución del pedido #" + pedido.getIdPedido());

            movimientoInventarioRepository.save(movimiento);
        }

        return devolucionRepository.save(devolucion);
    }
}