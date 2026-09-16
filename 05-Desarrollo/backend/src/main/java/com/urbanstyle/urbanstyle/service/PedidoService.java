package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.DetallePedidoRequest;
import com.urbanstyle.urbanstyle.dto.PedidoRequest;
import com.urbanstyle.urbanstyle.entity.Cliente;
import com.urbanstyle.urbanstyle.entity.DetallePedido;
import com.urbanstyle.urbanstyle.entity.Devolucion;
import com.urbanstyle.urbanstyle.entity.Inventario;
import com.urbanstyle.urbanstyle.entity.MovimientoInventario;
import com.urbanstyle.urbanstyle.entity.Pedido;
import com.urbanstyle.urbanstyle.entity.Usuario;
import com.urbanstyle.urbanstyle.entity.VarianteProducto;
import com.urbanstyle.urbanstyle.repository.ClienteRepository;
import com.urbanstyle.urbanstyle.repository.DetallePedidoRepository;
import com.urbanstyle.urbanstyle.repository.DevolucionRepository;
import com.urbanstyle.urbanstyle.repository.InventarioRepository;
import com.urbanstyle.urbanstyle.repository.MovimientoInventarioRepository;
import com.urbanstyle.urbanstyle.repository.PedidoRepository;
import com.urbanstyle.urbanstyle.repository.UsuarioRepository;
import com.urbanstyle.urbanstyle.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final VarianteProductoRepository varianteProductoRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final DevolucionRepository devolucionRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            VarianteProductoRepository varianteProductoRepository,
            InventarioRepository inventarioRepository,
            MovimientoInventarioRepository movimientoInventarioRepository,
            DevolucionRepository devolucionRepository) {

        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.varianteProductoRepository = varianteProductoRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.devolucionRepository = devolucionRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElse(null);
    }

    public List<Pedido> listarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public Pedido crearPedido(PedidoRequest request) {

        Cliente cliente = clienteRepository
                .findById(request.getIdCliente())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El cliente indicado no existe"
                        ));

        Pedido pedido = new Pedido();

        pedido.setCliente(cliente);
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(BigDecimal.ZERO);

        if (request.getIdUsuarioVendedor() != null) {

            Usuario vendedor = usuarioRepository
                    .findById(request.getIdUsuarioVendedor())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "El vendedor indicado no existe"
                            ));

            pedido.setVendedor(vendedor);
        }

        return pedidoRepository.save(pedido);
    }

    public DetallePedido agregarDetalle(
            Integer idPedido,
            DetallePedidoRequest request) {

        Pedido pedido = buscarPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido indicado no existe"
            );
        }

        if (!"PENDIENTE".equals(pedido.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden modificar pedidos pendientes"
            );
        }

        VarianteProducto variante = varianteProductoRepository
                .findById(request.getIdVariante())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La variante indicada no existe"
                        ));

        BigDecimal precioUnitario =
                variante.getProducto().getPrecio();

        BigDecimal subtotal = precioUnitario.multiply(
                BigDecimal.valueOf(request.getCantidad())
        );

        DetallePedido detalle = new DetallePedido();

        detalle.setPedido(pedido);
        detalle.setVariante(variante);
        detalle.setCantidad(request.getCantidad());
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);

        DetallePedido detalleGuardado =
                detallePedidoRepository.save(detalle);

        BigDecimal nuevoTotal = pedido.getTotal().add(subtotal);

        pedido.setTotal(nuevoTotal);
        pedidoRepository.save(pedido);

        return detalleGuardado;
    }

    public List<DetallePedido> listarDetalles(Integer idPedido) {

        if (!pedidoRepository.existsById(idPedido)) {
            throw new IllegalArgumentException(
                    "El pedido indicado no existe"
            );
        }

        return detallePedidoRepository
                .findByPedido_IdPedido(idPedido);
    }

    @Transactional
    public Pedido confirmarPedido(
            Integer idPedido,
            Integer idUsuarioConfirmacion) {

        Pedido pedido = buscarPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido indicado no existe"
            );
        }

        if (!"PENDIENTE".equals(pedido.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden confirmar pedidos pendientes"
            );
        }

        Usuario usuarioConfirmacion = usuarioRepository
                .findById(idUsuarioConfirmacion)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario de confirmación no existe"
                        ));

        List<DetallePedido> detalles =
                detallePedidoRepository.findByPedido_IdPedido(idPedido);

        if (detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede confirmar un pedido sin productos"
            );
        }

        for (DetallePedido detalle : detalles) {

            Inventario inventario = inventarioRepository
                    .findByVariante_IdVariante(
                            detalle.getVariante().getIdVariante()
                    )
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "La variante no tiene inventario registrado"
                            ));

            if (inventario.getStockActual()
                    < detalle.getCantidad()) {

                throw new IllegalArgumentException(
                        "Stock insuficiente para la variante "
                                + detalle.getVariante().getSku()
                );
            }
        }

        for (DetallePedido detalle : detalles) {

            Inventario inventario = inventarioRepository
                    .findByVariante_IdVariante(
                            detalle.getVariante().getIdVariante()
                    )
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "La variante no tiene inventario registrado"
                            ));

            int stockAnterior = inventario.getStockActual();

            int stockPosterior =
                    stockAnterior - detalle.getCantidad();

            inventario.setStockActual(stockPosterior);
            inventarioRepository.save(inventario);

            MovimientoInventario movimiento =
                    new MovimientoInventario();

            movimiento.setVariante(detalle.getVariante());
            movimiento.setUsuario(usuarioConfirmacion);
            movimiento.setTipoMovimiento("SALIDA");
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockPosterior(stockPosterior);
            movimiento.setMotivo(
                    "Salida por confirmación del pedido #"
                            + pedido.getIdPedido()
            );

            movimientoInventarioRepository.save(movimiento);
        }

        pedido.setEstado("CONFIRMADO");
        pedido.setUsuarioConfirmacion(usuarioConfirmacion);
        pedido.setFechaConfirmacion(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelarPedido(
            Integer idPedido,
            Integer idUsuario) {

        Pedido pedido = buscarPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido indicado no existe"
            );
        }

        if ("ENVIADO".equals(pedido.getEstado())
                || "ENTREGADO".equals(pedido.getEstado())) {

            throw new IllegalArgumentException(
                    "No se puede cancelar un pedido enviado o entregado"
            );
        }

        if ("CANCELADO".equals(pedido.getEstado())) {

            throw new IllegalArgumentException(
                    "El pedido ya está cancelado"
            );
        }

        Usuario usuario = usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario indicado no existe"
                        ));

        List<DetallePedido> detalles =
                detallePedidoRepository.findByPedido_IdPedido(idPedido);

        /*
         * Si el pedido estaba confirmado, el inventario
         * debe regresar al stock.
         */
        if ("CONFIRMADO".equals(pedido.getEstado())) {

            for (DetallePedido detalle : detalles) {

                Inventario inventario = inventarioRepository
                        .findByVariante_IdVariante(
                                detalle.getVariante().getIdVariante()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La variante no tiene inventario registrado"
                                ));

                int stockAnterior = inventario.getStockActual();

                int stockPosterior =
                        stockAnterior + detalle.getCantidad();

                inventario.setStockActual(stockPosterior);

                inventarioRepository.save(inventario);

                MovimientoInventario movimiento =
                        new MovimientoInventario();

                movimiento.setVariante(detalle.getVariante());
                movimiento.setUsuario(usuario);
                movimiento.setTipoMovimiento("DEVOLUCION");
                movimiento.setCantidad(detalle.getCantidad());
                movimiento.setStockAnterior(stockAnterior);
                movimiento.setStockPosterior(stockPosterior);
                movimiento.setMotivo(
                        "Devolución por cancelación del pedido #"
                                + pedido.getIdPedido()
                );

                movimientoInventarioRepository.save(movimiento);

                Devolucion devolucion = new Devolucion();

                devolucion.setPedido(pedido);
                devolucion.setDetalle(detalle);
                devolucion.setUsuario(usuario);
                devolucion.setCantidad(detalle.getCantidad());
                devolucion.setMotivo("Cancelación del pedido");
                devolucion.setEstadoProducto("RESALABLE");
                devolucion.setVuelveInventario(true);
                devolucion.setObservaciones(
                        "Producto devuelto al inventario por cancelación"
                );
                devolucion.setFecha(LocalDateTime.now());

                devolucionRepository.save(devolucion);
            }
        }

        pedido.setEstado("CANCELADO");

        return pedidoRepository.save(pedido);
    }
}