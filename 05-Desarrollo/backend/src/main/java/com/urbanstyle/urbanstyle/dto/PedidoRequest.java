package com.urbanstyle.urbanstyle.dto;

import jakarta.validation.constraints.NotNull;

public class PedidoRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Integer idCliente;

    private Integer idUsuarioVendedor;

    public PedidoRequest() {
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdUsuarioVendedor() {
        return idUsuarioVendedor;
    }

    public void setIdUsuarioVendedor(Integer idUsuarioVendedor) {
        this.idUsuarioVendedor = idUsuarioVendedor;
    }
}