package com.urbanstyle.urbanstyle.dto;

public class UsuarioResponse {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private Boolean estado;
    private Integer idRol;
    private String nombreRol;

    public UsuarioResponse() {
    }

    public UsuarioResponse(
            Integer idUsuario,
            String nombre,
            String apellido,
            String correo,
            Boolean estado,
            Integer idRol,
            String nombreRol) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.estado = estado;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}