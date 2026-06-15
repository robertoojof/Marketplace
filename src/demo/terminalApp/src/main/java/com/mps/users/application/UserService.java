package com.mps.users.application;

import java.util.List;

import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;

public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void adicionarUsuario(User user) {
        userRepository.salvar(user);
    }

    public List<User> listarUsuarios() {
        return userRepository.buscarTodos();
    }
}
