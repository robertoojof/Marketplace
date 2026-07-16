package com.mps.shared.factory;

import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.users.domain.IUserRepository;

public interface RepositoryFactory {
    IUserRepository criarUserRepository();
    IProdutoRepository criarProdutoRepository();
    IAnuncioRepository criarAnuncioRepository();
}
