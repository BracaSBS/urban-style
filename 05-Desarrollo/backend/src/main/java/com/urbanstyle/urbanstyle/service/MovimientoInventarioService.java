package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.MovimientoInventarioRequest;
import com.urbanstyle.urbanstyle.entity.Inventario;
import com.urbanstyle.urbanstyle.entity.MovimientoInventario;
import com.urbanstyle.urbanstyle.entity.Usuario;
import com.urbanstyle.urbanstyle.repository.InventarioRepository;
import com.urbanstyle.urbanstyle.repository.MovimientoInventarioRepository;
import com.urbanstyle.urbanstyle.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final InventarioRepository inventarioRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoInventarioService(
            MovimientoInventarioRepository movimientoInventarioRepository,
            InventarioRepository inventarioRepository,
            UsuarioRepository usuarioRepository) {

        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.inventarioRepository = inventarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<MovimientoInventario> listarTodos() {
        return movimientoInventarioRepository.findAll();
    }

    public List<MovimientoInventario> listarPorVariante(Integer idVariante) {
        return movimientoInventarioRepository
                .findByVariante_IdVariante(idVariante);
    }

    public List<MovimientoInventario> listarPorUsuario(Integer idUsuario) {
        return movimientoInventarioRepository
                .findByUsuario_IdUsuario(idUsuario);
    }

    @Transactional
    public MovimientoInventario registrarEntrada(
            MovimientoInventarioRequest request) {

        Inventario inventario = inventarioRepository
                .findByVariante_IdVariante(request.getIdVariante())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La variante no tiene inventario registrado"
                        ));

        Usuario usuario = usuarioRepository
                .findById(request.getIdUsuario())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario indicado no existe"
                        ));

        int stockAnterior = inventario.getStockActual();

        int stockPosterior =
                stockAnterior + request.getCantidad();

        inventario.setStockActual(stockPosterior);

        inventarioRepository.save(inventario);

        MovimientoInventario movimiento =
                new MovimientoInventario();

        movimiento.setVariante(inventario.getVariante());
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento("ENTRADA");
        movimiento.setCantidad(request.getCantidad());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockPosterior(stockPosterior);
        movimiento.setMotivo(request.getMotivo());

        return movimientoInventarioRepository.save(movimiento);
    }
}