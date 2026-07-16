package com.mps.users.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.shared.exception.RepositorioException;
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

    @Override
    public Optional<User> buscarPorId(UUID id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public User atualizar(User user) {
        int index = indiceDoUsuario(user.getId());
        usuarios.set(index, user);
        return user;
    }

    @Override
    public Optional<User> buscarPorLogin(String login) {
        return usuarios.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public void deletar(UUID id) {
        User usuario = usuarios.get(indiceDoUsuario(id));
        usuario.setAtivo(false);
    }

    @Override
    public void reativar(UUID id) {
        User usuario = usuarios.get(indiceDoUsuario(id));
        usuario.setAtivo(true);
    }

    private int indiceDoUsuario(UUID id) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new RepositorioException("Usuário não encontrado");
    }
}
