package com.mps.users.application.command;

import java.util.UUID;

import com.mps.shared.command.Command;
import com.mps.users.presentation.controller.UserController;

public class ReativarUsuarioCommand implements Command<Void> {

    private final UserController receptor;
    private final UUID idAlvo;
    private final String loginAutorizador;
    private final String senhaAutorizador;

    public ReativarUsuarioCommand(UserController receptor, UUID idAlvo, String loginAutorizador,
            String senhaAutorizador) {
        this.receptor = receptor;
        this.idAlvo = idAlvo;
        this.loginAutorizador = loginAutorizador;
        this.senhaAutorizador = senhaAutorizador;
    }

    @Override
    public Void executar() {
        receptor.reativarUsuario(idAlvo, loginAutorizador, senhaAutorizador);
        return null;
    }

    @Override
    public String descricao() {
        return "Reativar usuário " + idAlvo + " (autorizado por " + loginAutorizador + ")";
    }
}
