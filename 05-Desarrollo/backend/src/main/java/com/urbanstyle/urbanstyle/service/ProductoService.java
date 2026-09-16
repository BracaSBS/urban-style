package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.ProductoRequest;
import com.urbanstyle.urbanstyle.entity.Categoria;
import com.urbanstyle.urbanstyle.entity.Producto;
import com.urbanstyle.urbanstyle.repository.CategoriaRepository;
import com.urbanstyle.urbanstyle.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        return productoRepository.findAll()
                .stream()
                .filter(producto -> Boolean.TRUE.equals(producto.getEstado()))
                .toList();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElse(null);
    }

    public Producto buscarPorReferencia(String referencia) {
        return productoRepository.findByReferencia(referencia)
                .orElse(null);
    }

    public Producto guardar(ProductoRequest request) {

        Categoria categoria = categoriaRepository
                .findById(request.getIdCategoria())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La categoría indicada no existe"
                        ));

        Producto producto = new Producto();

        producto.setReferencia(request.getReferencia());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setEstado(request.getEstado());
        producto.setEsNuevo(request.getEsNuevo());
        producto.setEsOferta(request.getEsOferta());
        producto.setEsDestacado(request.getEsDestacado());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public Producto desactivar(Integer id) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return null;
        }

        producto.setEstado(false);

        return productoRepository.save(producto);
    }

    public Producto activar(Integer id) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return null;
        }

        producto.setEstado(true);

        return productoRepository.save(producto);
    }
}
