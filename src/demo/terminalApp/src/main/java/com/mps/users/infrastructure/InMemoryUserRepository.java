package com.mps.users.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;

public class InMemoryUserRepository implements IUserRepository {

    private final List<User> usuarios = new ArrayList<>();

    @Override
    public void salvar(User user) {
        usuarios.add(user);
    }

    @Override
    public List<User> buscarTodos() {
        return List.copyOf(usuarios);
    }
}
