package com.mps.shared.security;

import java.util.Optional;
import java.util.UUID;

import com.mps.users.domain.Role;
import com.mps.users.domain.User;

public final class SessaoUsuario {

    private static final SessaoUsuario INSTANCIA = new SessaoUsuario();

    private User usuarioAutenticado;

    private SessaoUsuario() {
    }

    public static SessaoUsuario getInstance() {
        return INSTANCIA;
    }

    public void autenticar(User usuario) {
        this.usuarioAutenticado = usuario;
    }

    public void encerrar() {
        this.usuarioAutenticado = null;
    }

    public boolean estaAutenticado() {
        return usuarioAutenticado != null;
    }

    public Optional<User> getUsuario() {
        return Optional.ofNullable(usuarioAutenticado);
    }

    public Optional<UUID> getUsuarioId() {
        return getUsuario().map(User::getId);
    }

    public boolean ehAdmin() {
        return getUsuario().map(usuario -> usuario.getRole() == Role.ADMIN).orElse(false);
    }

    public boolean ehUsuario(UUID id) {
        return getUsuarioId().map(atual -> atual.equals(id)).orElse(false);
    }

    public String descricao() {
        return getUsuario()
                .map(usuario -> usuario.getLogin() + " (" + usuario.getRole() + ")")
                .orElse("não autenticado");
    }
}
