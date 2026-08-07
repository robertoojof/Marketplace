package com.mps.users.application.command;

import java.util.UUID;

import com.mps.shared.command.Command;
import com.mps.users.presentation.controller.UserController;

public class RemoverUsuarioCommand implements Command<Void> {

    private final UserController receptor;
    private final UUID id;

    public RemoverUsuarioCommand(UserController receptor, UUID id) {
        this.receptor = receptor;
        this.id = id;
    }

    @Override
    public Void executar() {
        receptor.removerUsuario(id);
        return null;
    }

    @Override
    public String descricao() {
        return "Remover usuário " + id;
    }
}
