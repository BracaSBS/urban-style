package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.VarianteProductoRequest;
import com.urbanstyle.urbanstyle.entity.VarianteProducto;
import com.urbanstyle.urbanstyle.service.VarianteProductoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variantes")
public class VarianteProductoController {

    private final VarianteProductoService varianteProductoService;

    public VarianteProductoController(
            VarianteProductoService varianteProductoService) {

        this.varianteProductoService = varianteProductoService;
    }

    @GetMapping
    public List<VarianteProducto> listarTodas() {
        return varianteProductoService.listarTodas();
    }

    @GetMapping("/{id}")
    public VarianteProducto buscarPorId(
            @PathVariable Integer id) {

        return varianteProductoService.buscarPorId(id);
    }

    @GetMapping("/sku/{sku}")
    public VarianteProducto buscarPorSku(
            @PathVariable String sku) {

        return varianteProductoService.buscarPorSku(sku);
    }

    @PostMapping
    public VarianteProducto guardar(
            @Valid @RequestBody VarianteProductoRequest request) {

        return varianteProductoService.guardar(request);
    }
}