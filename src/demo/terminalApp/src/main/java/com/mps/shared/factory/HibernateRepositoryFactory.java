package com.mps.shared.factory;

import com.mps.acessos.domain.IAcessoLogRepository;
import com.mps.acessos.infrastructure.HibernateAcessoLogRepository;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.infrastructure.HibernateAnuncioRepository;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.infrastructure.HibernateProdutoRepository;
import com.mps.users.domain.IUserRepository;
import com.mps.users.infrastructure.HibernateUserRepository;

public class HibernateRepositoryFactory implements RepositoryFactory {

    @Override
    public IUserRepository criarUserRepository() {
        return new HibernateUserRepository();
    }

    @Override
    public IProdutoRepository criarProdutoRepository() {
        return new HibernateProdutoRepository();
    }

    @Override
    public IAnuncioRepository criarAnuncioRepository() {
        return new HibernateAnuncioRepository();
    }

    @Override
    public IAcessoLogRepository criarAcessoLogRepository() {
        return new HibernateAcessoLogRepository();
    }
}
