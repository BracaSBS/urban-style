package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public Map<String, Object> ventasDelDia() {
        LocalDate hoy = LocalDate.now();

        return ventasEntre(
                hoy.atStartOfDay(),
                hoy.plusDays(1).atStartOfDay()
        );
    }

    public Map<String, Object> ventasDelMes() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.withDayOfMonth(1);

        return ventasEntre(
                inicio.atStartOfDay(),
                inicio.plusMonths(1).atStartOfDay()
        );
    }

    public Map<String, Object> ventasDeLaSemana() {
        LocalDate hoy = LocalDate.now();

        LocalDate inicio = hoy.minusDays(
                hoy.getDayOfWeek().getValue() - 1
        );

        return ventasEntre(
                inicio.atStartOfDay(),
                inicio.plusDays(7).atStartOfDay()
        );
    }

    public Map<String, Object> ventasEntre(
            LocalDateTime inicio,
            LocalDateTime fin) {

        BigDecimal total =
                reporteRepository.ventasConfirmadasEntre(inicio, fin);

        Long cantidadPedidos =
                reporteRepository.pedidosConfirmadosEntre(inicio, fin);

        Map<String, Object> resultado = new LinkedHashMap<>();

        resultado.put("inicio", inicio);
        resultado.put("fin", fin);
        resultado.put("ventasTotales", total);
        resultado.put("cantidadPedidos", cantidadPedidos);

        return resultado;
    }

    public List<Map<String, Object>> pedidosPorEstado() {

        return reporteRepository.pedidosPorEstado()
                .stream()
                .map(fila -> {

                    Map<String, Object> resultado =
                            new LinkedHashMap<>();

                    resultado.put("estado", fila[0]);
                    resultado.put("cantidad", fila[1]);

                    return resultado;
                })
                .toList();
    }

    public List<Map<String, Object>> ventasPorVendedor() {

        return reporteRepository.ventasPorVendedor()
                .stream()
                .map(fila -> {

                    Map<String, Object> resultado =
                            new LinkedHashMap<>();

                    resultado.put("idUsuario", fila[0]);
                    resultado.put("nombreVendedor", fila[1]);
                    resultado.put("cantidadPedidos", fila[2]);
                    resultado.put("ventasTotales", fila[3]);

                    return resultado;
                })
                .toList();
    }

    public List<Map<String, Object>> productosMasVendidos() {

        return reporteRepository.productosMasVendidos()
                .stream()
                .map(fila -> {

                    Map<String, Object> resultado =
                            new LinkedHashMap<>();

                    resultado.put("idProducto", fila[0]);
                    resultado.put("producto", fila[1]);
                    resultado.put("cantidadVendida", fila[2]);

                    return resultado;
                })
                .toList();
    }

    public List<Map<String, Object>> tallasMasVendidas() {

        return reporteRepository.tallasMasVendidas()
                .stream()
                .map(fila -> {

                    Map<String, Object> resultado =
                            new LinkedHashMap<>();

                    resultado.put("idTalla", fila[0]);
                    resultado.put("talla", fila[1]);
                    resultado.put("cantidadVendida", fila[2]);

                    return resultado;
                })
                .toList();
    }

    public List<Map<String, Object>> coloresMasVendidos() {

        return reporteRepository.coloresMasVendidos()
                .stream()
                .map(fila -> {

                    Map<String, Object> resultado =
                            new LinkedHashMap<>();

                    resultado.put("idColor", fila[0]);
                    resultado.put("color", fila[1]);
                    resultado.put("cantidadVendida", fila[2]);

                    return resultado;
                })
                .toList();
    }
}