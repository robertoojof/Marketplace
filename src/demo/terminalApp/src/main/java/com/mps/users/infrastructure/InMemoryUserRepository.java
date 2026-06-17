package com.mps.users.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mps.users.domain.IUserRepository;
import com.mps.users.domain.User;

public class InMemoryUserRepository implements IUserRepository {

    private final List<User> usuarios = new ArrayList<>();

    @Override
    public void salvar(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        usuarios.add(user);
    }

    @Override
    public List<User> buscarTodos() {
        return List.copyOf(usuarios);
    }
}
