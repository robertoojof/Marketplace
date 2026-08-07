package com.mps.shared.exception;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mps.anuncios.domain.exception.ValidacaoAnuncioException;
import com.mps.produtos.domain.exception.ValidacaoProdutoException;
import com.mps.users.domain.exception.ValidacaoUsuarioException;
import com.mps.users.infrastructure.InMemoryUserRepository;

/**
 * O CommandInvoker decide entre registrar aviso e registrar erro com pilha de execução
 * a partir de {@link NegocioException}. Estes testes fixam essa classificação.
 */
class ClassificacaoDeExcecoesTest {

    @Test
    void recusas_de_negocio_devem_derivar_de_NegocioException() {
        assertInstanceOf(NegocioException.class, new ValidacaoUsuarioException(List.of("x")));
        assertInstanceOf(NegocioException.class, new ValidacaoProdutoException(List.of("x")));
        assertInstanceOf(NegocioException.class, new ValidacaoAnuncioException(List.of("x")));
        assertInstanceOf(NegocioException.class, new AutorizacaoException("x"));
        assertInstanceOf(NegocioException.class, new DesfazerException("x"));
        assertInstanceOf(NegocioException.class, new RecursoNaoEncontradoException("x"));
    }

    @Test
    void falha_de_infraestrutura_nao_deve_derivar_de_NegocioException() {
        assertFalse(NegocioException.class.isAssignableFrom(RepositorioException.class));
    }

    @Test
    void recurso_inexistente_nao_deve_ser_confundido_com_falha_de_infraestrutura() {
        assertFalse(RepositorioException.class.isAssignableFrom(RecursoNaoEncontradoException.class));
    }

    @Test
    void repositorio_deve_sinalizar_entidade_inexistente_como_recusa_de_negocio() {
        InMemoryUserRepository repository = new InMemoryUserRepository();

        RuntimeException erro = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class, () -> repository.deletar(UUID.randomUUID()));

        assertInstanceOf(NegocioException.class, erro);
    }
}
