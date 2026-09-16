package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.InventarioRequest;
import com.urbanstyle.urbanstyle.entity.Inventario;
import com.urbanstyle.urbanstyle.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/stock-bajo")
    public List<Inventario> listarStockBajo() {
        return inventarioService.listarStockBajo();
    }

    @GetMapping("/alertas")
    public List<Inventario> listarAlertas() {
        return inventarioService.listarStockBajo();
    }

    @GetMapping("/agotados")
    public List<Inventario> listarAgotados() {
        return inventarioService.listarAgotados();
    }

    @GetMapping("/variante/{idVariante}/stock")
    public boolean hayStock(
            @PathVariable Integer idVariante,
            @RequestParam Integer cantidad) {

        return inventarioService.hayStock(idVariante, cantidad);
    }

    @GetMapping("/variante/{idVariante}")
    public Inventario buscarPorVariante(
            @PathVariable Integer idVariante) {

        return inventarioService.buscarPorVariante(idVariante);
    }

    @GetMapping
    public List<Inventario> listarTodos() {
        return inventarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Inventario buscarPorId(@PathVariable Integer id) {
        return inventarioService.buscarPorId(id);
    }

    @PostMapping
    public Inventario guardar(
            @Valid @RequestBody InventarioRequest request) {

        return inventarioService.guardar(request);
    }
}