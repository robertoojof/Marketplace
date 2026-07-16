package com.mps.shared.factory;

import com.mps.acessos.domain.IAcessoLogRepository;
import com.mps.acessos.infrastructure.InMemoryAcessoLogRepository;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.infrastructure.InMemoryAnuncioRepository;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.infrastructure.InMemoryProdutoRepository;
import com.mps.users.domain.IUserRepository;
import com.mps.users.infrastructure.InMemoryUserRepository;

public class InMemoryRepositoryFactory implements RepositoryFactory {

    @Override
    public IUserRepository criarUserRepository() {
        return new InMemoryUserRepository();
    }

    @Override
    public IProdutoRepository criarProdutoRepository() {
        return new InMemoryProdutoRepository();
    }

    @Override
    public IAnuncioRepository criarAnuncioRepository() {
        return new InMemoryAnuncioRepository();
    }

    @Override
    public IAcessoLogRepository criarAcessoLogRepository() {
        return new InMemoryAcessoLogRepository();
    }
}
