package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.PrecioRequest;
import com.urbanstyle.urbanstyle.entity.HistorialPrecio;
import com.urbanstyle.urbanstyle.service.HistorialPrecioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-precios")
public class HistorialPrecioController {

    private final HistorialPrecioService historialPrecioService;

    public HistorialPrecioController(HistorialPrecioService historialPrecioService) {
        this.historialPrecioService = historialPrecioService;
    }

    @GetMapping
    public List<HistorialPrecio> listarTodos() {
        return historialPrecioService.listarTodos();
    }

    @GetMapping("/producto/{idProducto}")
    public List<HistorialPrecio> listarPorProducto(
            @PathVariable Integer idProducto) {

        return historialPrecioService.listarPorProducto(idProducto);
    }

    @PutMapping("/producto/{idProducto}")
    public ResponseEntity<HistorialPrecio> cambiarPrecio(
            @PathVariable Integer idProducto,
            @Valid @RequestBody PrecioRequest request) {

        return ResponseEntity.ok(
                historialPrecioService.cambiarPrecio(idProducto, request)
        );
    }
}