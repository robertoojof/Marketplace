package com.mps.shared.facade;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.IAcessoLogRepository;
import com.mps.acessos.domain.TipoAcesso;
import com.mps.shared.factory.RepositoryFactory;
import com.mps.users.application.UserService;
import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;
import com.mps.users.presentation.controller.UserController;

public final class UserFacade {

    private static UserFacade instance;

    private final IUserRepository userRepository;
    private final IAcessoLogRepository acessoLogRepository;
    private final UserController userController;

    private UserFacade(RepositoryFactory factory) {
        this.userRepository = factory.criarUserRepository();
        this.acessoLogRepository = factory.criarAcessoLogRepository();
        this.userController = new UserController(new UserService(userRepository));
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

    static synchronized void reset() {
        instance = null;
    }

    public void adicionarUsuario(User user) {
        userController.adicionarUsuario(user);
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
        User atualizado = userController.atualizarUsuario(user);
        registrarAcesso(atualizado.getId(), TipoAcesso.ATUALIZACAO);
        return atualizado;
    }

    public void removerUsuario(UUID id) {
        userController.removerUsuario(id);
        registrarAcesso(id, TipoAcesso.REMOCAO);
    }

    public void reativarUsuario(UUID idAlvo, String loginAutorizador, String senhaAutorizador) {
        userController.reativarUsuario(idAlvo, loginAutorizador, senhaAutorizador);
        registrarAcesso(idAlvo, TipoAcesso.REATIVACAO);
    }

    public int contarUsuarios() {
        return (int) listarUsuarios().stream().filter(User::isAtivo).count();
    }

    public List<AcessoLog> listarAcessos() {
        return acessoLogRepository.buscarTodos();
    }

    private void registrarAcesso(UUID usuarioId, TipoAcesso acao) {
        acessoLogRepository.salvar(new AcessoLog(UUID.randomUUID(), usuarioId, acao, Instant.now()));
    }
}
