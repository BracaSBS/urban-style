package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.DevolucionRequest;
import com.urbanstyle.urbanstyle.entity.Devolucion;
import com.urbanstyle.urbanstyle.service.DevolucionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devoluciones")
public class DevolucionController {

    private final DevolucionService devolucionService;

    public DevolucionController(DevolucionService devolucionService) {
        this.devolucionService = devolucionService;
    }

    @GetMapping
    public List<Devolucion> listarTodas() {
        return devolucionService.listarTodas();
    }

    @GetMapping("/pedido/{idPedido}")
    public List<Devolucion> listarPorPedido(
            @PathVariable Integer idPedido) {

        return devolucionService.listarPorPedido(idPedido);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Devolucion> listarPorUsuario(
            @PathVariable Integer idUsuario) {

        return devolucionService.listarPorUsuario(idUsuario);
    }

    @PostMapping
    public Devolucion registrarDevolucion(
            @Valid @RequestBody DevolucionRequest request) {

        return devolucionService.registrarDevolucion(request);
    }
}