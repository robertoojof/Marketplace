package com.mps.acessos.domain;

import java.util.List;

public interface IAcessoLogRepository {
    void salvar(AcessoLog acessoLog);
    List<AcessoLog> buscarTodos();
}
