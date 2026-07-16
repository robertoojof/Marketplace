package com.mps.users.presentation.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.users.application.UserService;
import com.mps.users.domain.User;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void adicionarUsuario(User user) {
        userService.adicionarUsuario(user);
    }

    public List<User> listarUsuarios() {
        return userService.listarUsuarios();
    }

    public Optional<User> buscarUsuarioPorId(UUID id) {
        return userService.buscarUsuarioPorId(id);
    }

    public User atualizarUsuario(User user) {
        return userService.atualizarUsuario(user);
    }

    public void removerUsuario(UUID id) {
        userService.removerUsuario(id);
    }
}
