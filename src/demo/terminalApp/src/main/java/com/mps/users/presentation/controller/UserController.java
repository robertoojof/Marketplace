package com.mps.users.presentation.controller;

import java.util.List;

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
}
