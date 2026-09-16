package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.PrecioRequest;
import com.urbanstyle.urbanstyle.entity.HistorialPrecio;
import com.urbanstyle.urbanstyle.entity.Producto;
import com.urbanstyle.urbanstyle.entity.Usuario;
import com.urbanstyle.urbanstyle.repository.HistorialPrecioRepository;
import com.urbanstyle.urbanstyle.repository.ProductoRepository;
import com.urbanstyle.urbanstyle.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HistorialPrecioService {

    private final HistorialPrecioRepository historialPrecioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public HistorialPrecioService(
            HistorialPrecioRepository historialPrecioRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {

        this.historialPrecioRepository = historialPrecioRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<HistorialPrecio> listarTodos() {
        return historialPrecioRepository.findAll();
    }

    public List<HistorialPrecio> listarPorProducto(Integer idProducto) {
        return historialPrecioRepository.findByProducto_IdProducto(idProducto);
    }

    @Transactional
    public HistorialPrecio cambiarPrecio(Integer idProducto, PrecioRequest request) {

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new IllegalArgumentException("El producto indicado no existe"));

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() ->
                        new IllegalArgumentException("El usuario indicado no existe"));

        BigDecimal precioAnterior = producto.getPrecio();
        BigDecimal precioNuevo = request.getPrecio();

        if (precioNuevo.compareTo(precioAnterior) == 0) {
            throw new IllegalArgumentException(
                    "El nuevo precio debe ser diferente al precio actual");
        }

        HistorialPrecio historial = new HistorialPrecio();

        historial.setProducto(producto);
        historial.setUsuario(usuario);
        historial.setPrecioAnterior(precioAnterior);
        historial.setPrecioNuevo(precioNuevo);

        producto.setPrecio(precioNuevo);
        productoRepository.save(producto);

        return historialPrecioRepository.save(historial);
    }
}