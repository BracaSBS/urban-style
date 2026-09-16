package com.urbanstyle.urbanstyle.config;

import com.urbanstyle.urbanstyle.entity.Usuario;
import com.urbanstyle.urbanstyle.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "No existe un usuario con el correo indicado"));

        if (!Boolean.TRUE.equals(usuario.getEstado())) {
            throw new UsernameNotFoundException(
                    "El usuario se encuentra inactivo");
        }

        String rol = usuario.getRol().getNombre();

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPasswordHash())
                .roles(rol)
                .build();
    }
}