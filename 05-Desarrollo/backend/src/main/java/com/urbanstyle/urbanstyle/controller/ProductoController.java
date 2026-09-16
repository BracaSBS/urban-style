package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.dto.ProductoRequest;
import com.urbanstyle.urbanstyle.entity.Producto;
import com.urbanstyle.urbanstyle.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarTodos() {
        return productoService.listarTodos();
    }

    @GetMapping("/activos")
    public List<Producto> listarActivos() {
        return productoService.listarActivos();
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    @GetMapping("/referencia/{referencia}")
    public Producto buscarPorReferencia(
            @PathVariable String referencia) {

        return productoService.buscarPorReferencia(referencia);
    }

    @PostMapping
    public Producto guardar(
            @Valid @RequestBody ProductoRequest request) {

        return productoService.guardar(request);
    }

    @PutMapping("/{id}/desactivar")
    public Producto desactivar(@PathVariable Integer id) {
        return productoService.desactivar(id);
    }

    @PutMapping("/{id}/activar")
    public Producto activar(@PathVariable Integer id) {
        return productoService.activar(id);
    }
}
