package com.mps.users.application.command;

import com.mps.shared.command.Command;
import com.mps.users.domain.User;
import com.mps.users.presentation.controller.UserController;

public class AtualizarUsuarioCommand implements Command<User> {

    private final UserController receptor;
    private final User user;

    public AtualizarUsuarioCommand(UserController receptor, User user) {
        this.receptor = receptor;
        this.user = user;
    }

    @Override
    public User executar() {
        return receptor.atualizarUsuario(user);
    }

    @Override
    public String descricao() {
        return "Atualizar usuário " + user.getLogin();
    }
}
