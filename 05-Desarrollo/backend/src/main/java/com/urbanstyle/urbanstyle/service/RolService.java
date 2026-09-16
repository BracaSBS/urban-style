package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.entity.Rol;
import com.urbanstyle.urbanstyle.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    public Rol buscarPorId(Integer id) {
        return rolRepository.findById(id)
                .orElse(null);
    }

    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }
}