package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.entity.Color;
import com.urbanstyle.urbanstyle.repository.ColorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public List<Color> listarTodos() {
        return colorRepository.findAll();
    }

    public Color buscarPorId(Integer id) {
        return colorRepository.findById(id)
                .orElse(null);
    }

    public Color guardar(Color color) {
        return colorRepository.save(color);
    }
}