package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.entity.ProductoImagen;
import com.urbanstyle.urbanstyle.repository.ProductoImagenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoImagenService {

    private final ProductoImagenRepository productoImagenRepository;

    public ProductoImagenService(
            ProductoImagenRepository productoImagenRepository) {
        this.productoImagenRepository = productoImagenRepository;
    }

    public List<ProductoImagen> listarTodas() {
        return productoImagenRepository.findAll();
    }

    public ProductoImagen buscarPorId(Integer id) {
        return productoImagenRepository.findById(id)
                .orElse(null);
    }

    public List<ProductoImagen> listarPorProducto(Integer idProducto) {
        return productoImagenRepository
                .findByProducto_IdProducto(idProducto);
    }

    public ProductoImagen guardar(ProductoImagen imagen) {
        return productoImagenRepository.save(imagen);
    }

    public void eliminar(Integer id) {
        productoImagenRepository.deleteById(id);
    }
}