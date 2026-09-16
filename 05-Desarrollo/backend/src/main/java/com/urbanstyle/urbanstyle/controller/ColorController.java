package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.entity.Color;
import com.urbanstyle.urbanstyle.service.ColorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colores")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public List<Color> listarTodos() {
        return colorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Color buscarPorId(@PathVariable Integer id) {
        return colorService.buscarPorId(id);
    }

    @PostMapping
    public Color guardar(@RequestBody Color color) {
        return colorService.guardar(color);
    }
}