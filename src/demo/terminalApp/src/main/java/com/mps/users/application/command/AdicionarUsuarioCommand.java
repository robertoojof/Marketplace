package com.mps.users.application.command;

import com.mps.shared.command.Command;
import com.mps.users.domain.User;
import com.mps.users.presentation.controller.UserController;

public class AdicionarUsuarioCommand implements Command<Void> {

    private final UserController receptor;
    private final User user;

    public AdicionarUsuarioCommand(UserController receptor, User user) {
        this.receptor = receptor;
        this.user = user;
    }

    @Override
    public Void executar() {
        receptor.adicionarUsuario(user);
        return null;
    }

    @Override
    public String descricao() {
        return "Adicionar usuário " + user.getLogin();
    }
}
