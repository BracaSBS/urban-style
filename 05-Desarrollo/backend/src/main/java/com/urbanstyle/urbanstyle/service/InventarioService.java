package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.InventarioRequest;
import com.urbanstyle.urbanstyle.entity.Inventario;
import com.urbanstyle.urbanstyle.entity.VarianteProducto;
import com.urbanstyle.urbanstyle.repository.InventarioRepository;
import com.urbanstyle.urbanstyle.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    public InventarioService(
            InventarioRepository inventarioRepository,
            VarianteProductoRepository varianteProductoRepository) {

        this.inventarioRepository = inventarioRepository;
        this.varianteProductoRepository = varianteProductoRepository;
    }

    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    public Inventario buscarPorId(Integer id) {
        return inventarioRepository.findById(id)
                .orElse(null);
    }

    public Inventario buscarPorVariante(Integer idVariante) {
        return inventarioRepository
                .findByVariante_IdVariante(idVariante)
                .orElse(null);
    }

    public boolean hayStock(Integer idVariante, Integer cantidad) {
        Inventario inventario = buscarPorVariante(idVariante);

        return inventario != null
                && cantidad != null
                && cantidad > 0
                && inventario.getStockActual() >= cantidad;
    }

    public Inventario guardar(InventarioRequest request) {

        VarianteProducto variante = varianteProductoRepository
                .findById(request.getIdVariante())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La variante indicada no existe"
                        ));

        if (buscarPorVariante(request.getIdVariante()) != null) {
            throw new IllegalArgumentException(
                    "La variante ya tiene un registro de inventario"
            );
        }

        Inventario inventario = new Inventario();

        inventario.setVariante(variante);
        inventario.setStockActual(request.getStockActual());
        inventario.setStockMinimo(request.getStockMinimo());

        return inventarioRepository.save(inventario);
    }

    public List<Inventario> listarStockBajo() {
        return inventarioRepository.findAll()
                .stream()
                .filter(inventario ->
                        inventario.getStockActual()
                                <= inventario.getStockMinimo())
                .toList();
    }

    public List<Inventario> listarAgotados() {
        return inventarioRepository.findAll()
                .stream()
                .filter(inventario ->
                        inventario.getStockActual() == 0)
                .toList();
    }
}