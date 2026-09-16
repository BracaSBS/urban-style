package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.DetallePedidoRequest;
import com.urbanstyle.urbanstyle.dto.PedidoRequest;
import com.urbanstyle.urbanstyle.entity.DetallePedido;
import com.urbanstyle.urbanstyle.entity.Pedido;
import com.urbanstyle.urbanstyle.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/estado/{estado}")
    public List<Pedido> listarPorEstado(@PathVariable String estado) {
        return pedidoService.listarPorEstado(estado);
    }

    @PostMapping
    public Pedido crearPedido(
            @Valid @RequestBody PedidoRequest request) {

        return pedidoService.crearPedido(request);
    }

    @PostMapping("/{idPedido}/detalles")
    public DetallePedido agregarDetalle(
            @PathVariable Integer idPedido,
            @Valid @RequestBody DetallePedidoRequest request) {

        return pedidoService.agregarDetalle(idPedido, request);
    }

    @GetMapping("/{idPedido}/detalles")
    public List<DetallePedido> listarDetalles(
            @PathVariable Integer idPedido) {

        return pedidoService.listarDetalles(idPedido);
    }

    @PutMapping("/{idPedido}/confirmar/{idUsuarioConfirmacion}")
    public Pedido confirmarPedido(
            @PathVariable Integer idPedido,
            @PathVariable Integer idUsuarioConfirmacion) {

        return pedidoService.confirmarPedido(
                idPedido,
                idUsuarioConfirmacion
        );
    }

    @PutMapping("/{idPedido}/cancelar/{idUsuario}")
    public Pedido cancelarPedido(
            @PathVariable Integer idPedido,
            @PathVariable Integer idUsuario) {

        return pedidoService.cancelarPedido(
                idPedido,
                idUsuario
        );
    }
}