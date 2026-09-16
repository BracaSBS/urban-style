package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.entity.Talla;
import com.urbanstyle.urbanstyle.repository.TallaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TallaService {

    private final TallaRepository tallaRepository;

    public TallaService(TallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    public List<Talla> listarTodas() {
        return tallaRepository.findAll();
    }

    public Talla buscarPorId(Integer id) {
        return tallaRepository.findById(id)
                .orElse(null);
    }

    public Talla guardar(Talla talla) {
        return tallaRepository.save(talla);
    }
}