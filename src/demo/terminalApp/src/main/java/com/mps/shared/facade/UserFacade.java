package com.mps.shared.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.users.application.UserService;
import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;
import com.mps.users.infrastructure.HibernateUserRepository;
import com.mps.users.infrastructure.InMemoryUserRepository;
import com.mps.users.presentation.controller.UserController;

public final class UserFacade {

    private static UserFacade instance;

    private final IUserRepository userRepository;
    private final UserController userController;

    private UserFacade(boolean usarBancoDeDados) {
        this.userRepository = usarBancoDeDados ? new HibernateUserRepository() : new InMemoryUserRepository();
        this.userController = new UserController(new UserService(userRepository));
    }

    public static synchronized UserFacade getInstance(boolean usarBancoDeDados) {
        if (instance == null) {
            instance = new UserFacade(usarBancoDeDados);
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
    }

    public List<User> listarUsuarios() {
        return userController.listarUsuarios();
    }

    public Optional<User> buscarUsuarioPorId(UUID id) {
        return userController.buscarUsuarioPorId(id);
    }

    public Optional<User> buscarUsuarioPorLogin(String login) {
        return userController.buscarUsuarioPorLogin(login);
    }

    public User atualizarUsuario(User user) {
        return userController.atualizarUsuario(user);
    }

    public void removerUsuario(UUID id) {
        userController.removerUsuario(id);
    }

    public void reativarUsuario(UUID idAlvo, String loginAutorizador, String senhaAutorizador) {
        userController.reativarUsuario(idAlvo, loginAutorizador, senhaAutorizador);
    }

    public int contarUsuarios() {
        return (int) listarUsuarios().stream().filter(User::isAtivo).count();
    }
}
