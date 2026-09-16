package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.entity.Talla;
import com.urbanstyle.urbanstyle.service.TallaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tallas")
public class TallaController {

    private final TallaService tallaService;

    public TallaController(TallaService tallaService) {
        this.tallaService = tallaService;
    }

    @GetMapping
    public List<Talla> listarTodas() {
        return tallaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Talla buscarPorId(@PathVariable Integer id) {
        return tallaService.buscarPorId(id);
    }

    @PostMapping
    public Talla guardar(@RequestBody Talla talla) {
        return tallaService.guardar(talla);
    }
}