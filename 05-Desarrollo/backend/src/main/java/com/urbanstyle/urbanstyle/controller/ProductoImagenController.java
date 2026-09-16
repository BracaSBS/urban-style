package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.entity.ProductoImagen;
import com.urbanstyle.urbanstyle.service.ProductoImagenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto-imagenes")
public class ProductoImagenController {

    private final ProductoImagenService productoImagenService;

    public ProductoImagenController(
            ProductoImagenService productoImagenService) {
        this.productoImagenService = productoImagenService;
    }

    @GetMapping
    public List<ProductoImagen> listarTodas() {
        return productoImagenService.listarTodas();
    }

    @GetMapping("/{id}")
    public ProductoImagen buscarPorId(@PathVariable Integer id) {
        return productoImagenService.buscarPorId(id);
    }

    @GetMapping("/producto/{idProducto}")
    public List<ProductoImagen> listarPorProducto(
            @PathVariable Integer idProducto) {

        return productoImagenService.listarPorProducto(idProducto);
    }

    @PostMapping
    public ProductoImagen guardar(
            @RequestBody ProductoImagen imagen) {

        return productoImagenService.guardar(imagen);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoImagenService.eliminar(id);
    }
}