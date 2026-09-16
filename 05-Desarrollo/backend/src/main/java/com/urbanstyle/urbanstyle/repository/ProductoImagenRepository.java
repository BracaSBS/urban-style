package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.ProductoImagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoImagenRepository
        extends JpaRepository<ProductoImagen, Integer> {

    List<ProductoImagen> findByProducto_IdProducto(Integer idProducto);
}