package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.acessos.application.AcessoLogObserver;
import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.IAcessoLogRepository;
import com.mps.acessos.domain.TipoAcesso;
import com.mps.shared.command.CommandInvoker;
import com.mps.shared.factory.RepositoryFactory;
import com.mps.shared.observer.AcessoObserver;
import com.mps.shared.observer.AcessoSubject;
import com.mps.shared.observer.EventoDeAcesso;
import com.mps.users.application.NotificacaoVendedorObserver;
import com.mps.users.application.UserService;
import com.mps.users.application.command.AdicionarUsuarioCommand;
import com.mps.users.application.command.AtualizarUsuarioCommand;
import com.mps.users.application.command.ReativarUsuarioCommand;
import com.mps.users.application.command.RemoverUsuarioCommand;
import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;
import com.mps.users.presentation.controller.UserController;

public final class UserFacade {

    private static UserFacade instance;

    private final IUserRepository userRepository;
    private final IAcessoLogRepository acessoLogRepository;
    private final UserController userController;
    private final CommandInvoker invoker = new CommandInvoker();
    private final AcessoSubject acessos = new AcessoSubject();

    private UserFacade(RepositoryFactory factory) {
        this.userRepository = factory.criarUserRepository();
        this.acessoLogRepository = factory.criarAcessoLogRepository();
        this.userController = new UserController(new UserService(userRepository));
        this.acessos.registrar(new AcessoLogObserver(acessoLogRepository));
        this.acessos.registrar(new NotificacaoVendedorObserver());
    }

    public void registrarObservador(AcessoObserver observador) {
        acessos.registrar(observador);
    }

    public void removerObservador(AcessoObserver observador) {
        acessos.remover(observador);
    }

    public static synchronized UserFacade getInstance(RepositoryFactory factory) {
        if (instance == null) {
            instance = new UserFacade(factory);
        }
        return instance;
    }

    public static synchronized UserFacade getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserFacade ainda não foi inicializada");
        }
        return instance;
    }

    IUserRepository getRepository() {
        return userRepository;
    }

    CommandInvoker getInvoker() {
        return invoker;
    }

    static synchronized void reset() {
        instance = null;
    }

    public void adicionarUsuario(User user) {
        invoker.executar(new AdicionarUsuarioCommand(userController, user));
        registrarAcesso(user.getId(), TipoAcesso.CRIACAO);
    }

    public List<User> listarUsuarios() {
        return userController.listarUsuarios();
    }

    public Optional<User> buscarUsuarioPorId(UUID id) {
        Optional<User> usuario = userController.buscarUsuarioPorId(id);
        usuario.ifPresent(u -> registrarAcesso(u.getId(), TipoAcesso.BUSCA));
        return usuario;
    }

    public Optional<User> buscarUsuarioPorLogin(String login) {
        return userController.buscarUsuarioPorLogin(login);
    }

    public User atualizarUsuario(User user) {
        User atualizado = invoker.executar(new AtualizarUsuarioCommand(userController, user));
        registrarAcesso(atualizado.getId(), TipoAcesso.ATUALIZACAO);
        return atualizado;
    }

    public void removerUsuario(UUID id) {
        invoker.executar(new RemoverUsuarioCommand(userController, id));
        registrarAcesso(id, TipoAcesso.REMOCAO);
    }

    public void reativarUsuario(UUID idAlvo, String loginAutorizador, String senhaAutorizador) {
        invoker.executar(new ReativarUsuarioCommand(userController, idAlvo, loginAutorizador, senhaAutorizador));
        registrarAcesso(idAlvo, TipoAcesso.REATIVACAO);
    }

    public int contarUsuarios() {
        return (int) listarUsuarios().stream().filter(User::isAtivo).count();
    }

    public List<AcessoLog> listarAcessos() {
        return acessoLogRepository.buscarTodos();
    }

    private void registrarAcesso(UUID usuarioId, TipoAcesso acao) {
        acessos.notificar(EventoDeAcesso.agora(usuarioId, acao));
    }
}
