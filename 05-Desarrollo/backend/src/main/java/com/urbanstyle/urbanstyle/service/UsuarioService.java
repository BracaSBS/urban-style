package com.urbanstyle.urbanstyle.service;

import com.urbanstyle.urbanstyle.dto.UsuarioResponse;
import com.urbanstyle.urbanstyle.entity.Rol;
import com.urbanstyle.urbanstyle.entity.Usuario;
import com.urbanstyle.urbanstyle.repository.RolRepository;
import com.urbanstyle.urbanstyle.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertirResponse)
                .orElse(null);
    }

    public Usuario guardar(Usuario usuario) {

        if (usuario.getRol() == null || usuario.getRol().getIdRol() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }

        Rol rol = rolRepository.findById(usuario.getRol().getIdRol())
                .orElseThrow(() ->
                        new IllegalArgumentException("El rol indicado no existe"));

        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    private UsuarioResponse convertirResponse(Usuario usuario) {

        Integer idRol = null;
        String nombreRol = null;

        if (usuario.getRol() != null) {
            idRol = usuario.getRol().getIdRol();
            nombreRol = usuario.getRol().getNombre();
        }

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getEstado(),
                idRol,
                nombreRol
        );
    }
}