package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.VarianteProductoRequest;
import com.urbanstyle.urbanstyle.entity.Color;
import com.urbanstyle.urbanstyle.entity.Producto;
import com.urbanstyle.urbanstyle.entity.Talla;
import com.urbanstyle.urbanstyle.entity.VarianteProducto;
import com.urbanstyle.urbanstyle.repository.ColorRepository;
import com.urbanstyle.urbanstyle.repository.ProductoRepository;
import com.urbanstyle.urbanstyle.repository.TallaRepository;
import com.urbanstyle.urbanstyle.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VarianteProductoService {

    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;
    private final TallaRepository tallaRepository;
    private final ColorRepository colorRepository;

    public VarianteProductoService(
            VarianteProductoRepository varianteProductoRepository,
            ProductoRepository productoRepository,
            TallaRepository tallaRepository,
            ColorRepository colorRepository) {

        this.varianteProductoRepository = varianteProductoRepository;
        this.productoRepository = productoRepository;
        this.tallaRepository = tallaRepository;
        this.colorRepository = colorRepository;
    }

    public List<VarianteProducto> listarTodas() {
        return varianteProductoRepository.findAll();
    }

    public VarianteProducto buscarPorId(Integer id) {
        return varianteProductoRepository.findById(id)
                .orElse(null);
    }

    public VarianteProducto buscarPorSku(String sku) {
        return varianteProductoRepository.findBySku(sku)
                .orElse(null);
    }

    public VarianteProducto guardar(VarianteProductoRequest request) {

        Producto producto = productoRepository
                .findById(request.getIdProducto())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El producto indicado no existe"
                        ));

        Talla talla = tallaRepository
                .findById(request.getIdTalla())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La talla indicada no existe"
                        ));

        Color color = colorRepository
                .findById(request.getIdColor())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El color indicado no existe"
                        ));

        if (varianteProductoRepository
                .findBySku(request.getSku())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "El SKU indicado ya existe"
            );
        }

        if (varianteProductoRepository
                .findByProducto_IdProductoAndTalla_IdTallaAndColor_IdColor(
                        request.getIdProducto(),
                        request.getIdTalla(),
                        request.getIdColor()
                )
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Ya existe una variante para este producto, talla y color"
            );
        }

        VarianteProducto variante = new VarianteProducto();

        variante.setProducto(producto);
        variante.setTalla(talla);
        variante.setColor(color);
        variante.setSku(request.getSku());
        variante.setEstado(request.getEstado());

        return varianteProductoRepository.save(variante);
    }
}