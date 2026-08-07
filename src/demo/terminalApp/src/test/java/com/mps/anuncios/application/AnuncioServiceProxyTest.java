package com.mps.anuncios.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.infrastructure.InMemoryAnuncioRepository;
import com.mps.produtos.domain.Produto;
import com.mps.produtos.infrastructure.InMemoryProdutoRepository;
import com.mps.shared.exception.AutorizacaoException;
import com.mps.shared.security.SessaoUsuario;
import com.mps.users.domain.Role;
import com.mps.users.domain.User;
import com.mps.users.infrastructure.InMemoryUserRepository;

class AnuncioServiceProxyTest {

    private final SessaoUsuario sessao = SessaoUsuario.getInstance();

    private InMemoryAnuncioRepository anuncioRepository;
    private AnuncioServiceProxy proxy;
    private Produto produto;
    private User dono;
    private User intruso;
    private User admin;

    private static User usuario(String login, Role papel) {
        return new User(UUID.randomUUID(), login, "123.456.789-00", "Fulano",
                "fulano@email.com", "Senha@2024!", papel, true);
    }

    @BeforeEach
    void preparar() {
        anuncioRepository = new InMemoryAnuncioRepository();
        InMemoryProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        InMemoryUserRepository userRepository = new InMemoryUserRepository();

        produto = new Produto(UUID.randomUUID(), "Sabonete Dove", "Sabonete hidratante", true);
        produtoRepository.salvar(produto);

        dono = usuario("donoanuncio", Role.USER);
        intruso = usuario("intrusouser", Role.USER);
        admin = usuario("adminmaster", Role.ADMIN);
        userRepository.salvar(dono);
        userRepository.salvar(intruso);
        userRepository.salvar(admin);

        proxy = new AnuncioServiceProxy(
                new AnuncioService(anuncioRepository, produtoRepository, userRepository), sessao);
    }

    @AfterEach
    void limpar() {
        sessao.encerrar();
    }

    private Anuncio anuncioDe(User vendedor) {
        return new Anuncio(UUID.randomUUID(), produto, vendedor, new BigDecimal("12.90"), 10, true);
    }

    private Anuncio anuncioJaCadastradoDe(User vendedor) {
        Anuncio anuncio = anuncioDe(vendedor);
        anuncioRepository.salvar(anuncio);
        return anuncio;
    }

    @Test
    void deve_recusar_criacao_quando_nao_ha_sessao() {
        AutorizacaoException erro = assertThrows(AutorizacaoException.class,
                () -> proxy.adicionarAnuncio(anuncioDe(dono)));

        assertEquals("É necessário estar autenticado para executar esta operação", erro.getMessage());
    }

    @Test
    void deve_recusar_criacao_de_anuncio_em_nome_de_outro_vendedor() {
        sessao.autenticar(intruso);

        assertThrows(AutorizacaoException.class, () -> proxy.adicionarAnuncio(anuncioDe(dono)));
    }

    @Test
    void deve_permitir_criacao_do_proprio_anuncio() {
        sessao.autenticar(dono);

        assertDoesNotThrow(() -> proxy.adicionarAnuncio(anuncioDe(dono)));
        assertEquals(1, proxy.listarAnuncios().size());
    }

    @Test
    void deve_recusar_atualizacao_de_anuncio_de_outro_vendedor() {
        Anuncio anuncio = anuncioJaCadastradoDe(dono);
        sessao.autenticar(intruso);

        AutorizacaoException erro = assertThrows(AutorizacaoException.class,
                () -> proxy.atualizarAnuncio(anuncio));

        assertEquals("Apenas o vendedor dono do anúncio pode alterá-lo", erro.getMessage());
    }

    @Test
    void deve_recusar_remocao_de_anuncio_de_outro_vendedor() {
        Anuncio anuncio = anuncioJaCadastradoDe(dono);
        sessao.autenticar(intruso);

        assertThrows(AutorizacaoException.class, () -> proxy.removerAnuncio(anuncio.getId()));
    }

    @Test
    void deve_permitir_que_o_dono_remova_o_proprio_anuncio() {
        Anuncio anuncio = anuncioJaCadastradoDe(dono);
        sessao.autenticar(dono);

        assertDoesNotThrow(() -> proxy.removerAnuncio(anuncio.getId()));
    }

    @Test
    void admin_deve_poder_alterar_anuncio_de_qualquer_vendedor() {
        Anuncio anuncio = anuncioJaCadastradoDe(dono);
        sessao.autenticar(admin);

        assertDoesNotThrow(() -> proxy.reativarAnuncio(anuncio.getId()));
        assertDoesNotThrow(() -> proxy.removerAnuncio(anuncio.getId()));
    }

    @Test
    void desativacao_em_cascata_deve_exigir_administrador() {
        sessao.autenticar(dono);

        assertThrows(AutorizacaoException.class,
                () -> proxy.desativarAnunciosDoVendedor(dono.getId()));

        sessao.autenticar(admin);
        assertDoesNotThrow(() -> proxy.desativarAnunciosDoVendedor(dono.getId()));
    }

    @Test
    void consultas_devem_permanecer_liberadas_sem_sessao() {
        anuncioJaCadastradoDe(dono);

        assertDoesNotThrow(() -> proxy.listarAnuncios());
        assertEquals(1, proxy.listarAnuncios().size());
        assertEquals(1, proxy.buscarAnunciosPorVendedor(dono.getId()).size());
    }
}
