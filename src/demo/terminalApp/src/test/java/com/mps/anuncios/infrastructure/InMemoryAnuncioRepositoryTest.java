package com.mps.anuncios.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.Anuncio;
import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.RepositorioException;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;

class InMemoryAnuncioRepositoryTest {

    private InMemoryAnuncioRepository repository;
    private Produto produto;
    private User vendedor;
    private User outroVendedor;

    @BeforeEach
    void configurar() {
        repository = new InMemoryAnuncioRepository();
        produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        vendedor = new User(UUID.randomUUID(), "joaosilva", "123.456.789-00", "João Silva", "joao@email.com", "Senha@2024!", Role.USER, true);
        outroVendedor = new User(UUID.randomUUID(), "mariasouz", "111.222.333-44", "Maria Souza", "maria@email.com", "Senha@2024!", Role.USER, true);
    }

    private Anuncio novoAnuncio(User dono) {
        return new Anuncio(UUID.randomUUID(), produto, dono, new BigDecimal("12.90"), 10, true);
    }

    @Test
    void deve_salvar_e_retornar_anuncio() {
        Anuncio anuncio = novoAnuncio(vendedor);

        repository.salvar(anuncio);
        List<Anuncio> resultado = repository.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals(new BigDecimal("12.90"), resultado.get(0).getPreco());
    }

    @Test
    void buscarPorId_deve_retornar_anuncio_quando_existe() {
        Anuncio anuncio = novoAnuncio(vendedor);
        repository.salvar(anuncio);

        Optional<Anuncio> resultado = repository.buscarPorId(anuncio.getId());

        assertTrue(resultado.isPresent());
    }

    @Test
    void buscarPorVendedor_deve_retornar_apenas_anuncios_do_vendedor() {
        repository.salvar(novoAnuncio(vendedor));
        repository.salvar(novoAnuncio(vendedor));
        repository.salvar(novoAnuncio(outroVendedor));

        List<Anuncio> resultado = repository.buscarPorVendedor(vendedor.getId());

        assertEquals(2, resultado.size());
    }

    @Test
    void atualizar_deve_lancar_excecao_quando_anuncio_nao_existe() {
        Anuncio anuncio = novoAnuncio(vendedor);

        assertThrows(RepositorioException.class, () -> repository.atualizar(anuncio));
    }

    @Test
    void deletar_deve_marcar_anuncio_como_inativo() {
        Anuncio anuncio = novoAnuncio(vendedor);
        repository.salvar(anuncio);

        repository.deletar(anuncio.getId());

        assertFalse(repository.buscarPorId(anuncio.getId()).get().isAtivo());
    }

    @Test
    void reativar_deve_marcar_anuncio_como_ativo() {
        Anuncio anuncio = novoAnuncio(vendedor);
        repository.salvar(anuncio);
        repository.deletar(anuncio.getId());

        repository.reativar(anuncio.getId());

        assertTrue(repository.buscarPorId(anuncio.getId()).get().isAtivo());
    }

    @Test
    void desativarTodosDoVendedor_deve_desativar_apenas_anuncios_daquele_vendedor() {
        Anuncio anuncioVendedor1 = novoAnuncio(vendedor);
        Anuncio anuncioVendedor2 = novoAnuncio(vendedor);
        Anuncio anuncioOutroVendedor = novoAnuncio(outroVendedor);
        repository.salvar(anuncioVendedor1);
        repository.salvar(anuncioVendedor2);
        repository.salvar(anuncioOutroVendedor);

        repository.desativarTodosDoVendedor(vendedor.getId());

        assertFalse(repository.buscarPorId(anuncioVendedor1.getId()).get().isAtivo());
        assertFalse(repository.buscarPorId(anuncioVendedor2.getId()).get().isAtivo());
        assertTrue(repository.buscarPorId(anuncioOutroVendedor.getId()).get().isAtivo());
    }
}
