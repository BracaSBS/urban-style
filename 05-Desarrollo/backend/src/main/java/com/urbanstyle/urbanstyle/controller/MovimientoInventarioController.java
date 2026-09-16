package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.MovimientoInventarioRequest;
import com.urbanstyle.urbanstyle.entity.MovimientoInventario;
import com.urbanstyle.urbanstyle.service.MovimientoInventarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos-inventario")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioController(
            MovimientoInventarioService movimientoInventarioService) {

        this.movimientoInventarioService =
                movimientoInventarioService;
    }

    @GetMapping
    public List<MovimientoInventario> listarTodos() {
        return movimientoInventarioService.listarTodos();
    }

    @GetMapping("/variante/{idVariante}")
    public List<MovimientoInventario> listarPorVariante(
            @PathVariable Integer idVariante) {

        return movimientoInventarioService
                .listarPorVariante(idVariante);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<MovimientoInventario> listarPorUsuario(
            @PathVariable Integer idUsuario) {

        return movimientoInventarioService
                .listarPorUsuario(idUsuario);
    }

    @PostMapping("/entrada")
    public MovimientoInventario registrarEntrada(
            @Valid @RequestBody MovimientoInventarioRequest request) {

        return movimientoInventarioService
                .registrarEntrada(request);
    }
}