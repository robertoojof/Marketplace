package com.mps.shared.factory;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import com.mps.anuncios.infrastructure.InMemoryAnuncioRepository;
import com.mps.produtos.infrastructure.InMemoryProdutoRepository;
import com.mps.users.infrastructure.InMemoryUserRepository;

class InMemoryRepositoryFactoryTest {

    private final RepositoryFactory factory = new InMemoryRepositoryFactory();

    @Test
    void deve_criar_repositorios_em_memoria() {
        assertInstanceOf(InMemoryUserRepository.class, factory.criarUserRepository());
        assertInstanceOf(InMemoryProdutoRepository.class, factory.criarProdutoRepository());
        assertInstanceOf(InMemoryAnuncioRepository.class, factory.criarAnuncioRepository());
    }
}
