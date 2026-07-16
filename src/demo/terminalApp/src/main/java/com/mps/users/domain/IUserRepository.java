package com.mps.users.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
    void salvar(User user);
    List<User> buscarTodos();
    Optional<User> buscarPorId(UUID id);
    Optional<User> buscarPorLogin(String login);
    User atualizar(User user);
    void deletar(UUID id);
    void reativar(UUID id);
}
