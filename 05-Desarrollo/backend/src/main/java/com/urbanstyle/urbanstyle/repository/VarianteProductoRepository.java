package com.urbanstyle.urbanstyle.repository;

import com.urbanstyle.urbanstyle.entity.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VarianteProductoRepository
        extends JpaRepository<VarianteProducto, Integer> {

    Optional<VarianteProducto> findBySku(String sku);

    Optional<VarianteProducto>
    findByProducto_IdProductoAndTalla_IdTallaAndColor_IdColor(
            Integer idProducto,
            Integer idTalla,
            Integer idColor
    );
}