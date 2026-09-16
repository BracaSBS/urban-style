package com.urbanstyle.urbanstyle.controller;

import com.urbanstyle.urbanstyle.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/ventas/dia")
    public Map<String, Object> ventasDelDia() {
        return reporteService.ventasDelDia();
    }

    @GetMapping("/ventas/semana")
    public Map<String, Object> ventasDeLaSemana() {
        return reporteService.ventasDeLaSemana();
    }

    @GetMapping("/ventas/mes")
    public Map<String, Object> ventasDelMes() {
        return reporteService.ventasDelMes();
    }

    @GetMapping("/pedidos/estados")
    public List<Map<String, Object>> pedidosPorEstado() {
        return reporteService.pedidosPorEstado();
    }

    @GetMapping("/ventas/vendedores")
    public List<Map<String, Object>> ventasPorVendedor() {
        return reporteService.ventasPorVendedor();
    }

    @GetMapping("/productos/mas-vendidos")
    public List<Map<String, Object>> productosMasVendidos() {
        return reporteService.productosMasVendidos();
    }

    @GetMapping("/tallas/mas-vendidas")
    public List<Map<String, Object>> tallasMasVendidas() {
        return reporteService.tallasMasVendidas();
    }

    @GetMapping("/colores/mas-vendidos")
    public List<Map<String, Object>> coloresMasVendidos() {
        return reporteService.coloresMasVendidos();
    }
}