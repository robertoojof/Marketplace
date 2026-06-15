package com.mps.users.domain;

import java.util.List;

public interface IUserRepository {
    void salvar(User user);
    List<User> buscarTodos();
}
