package com.mps.acessos.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.mps.acessos.domain.AcessoLog;
import com.mps.acessos.domain.IAcessoLogRepository;

public class InMemoryAcessoLogRepository implements IAcessoLogRepository {

    private final List<AcessoLog> acessos = new ArrayList<>();

    @Override
    public void salvar(AcessoLog acessoLog) {
        acessos.add(acessoLog);
    }

    @Override
    public List<AcessoLog> buscarTodos() {
        return List.copyOf(acessos);
    }
}
