package com.mps.anuncios.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.anuncios.domain.exception.ValidacaoAnuncioException;
import com.mps.produtos.domain.IProdutoRepository;
import com.mps.users.domain.IUserRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AnuncioService {

    private final IAnuncioRepository anuncioRepository;
    private final IProdutoRepository produtoRepository;
    private final IUserRepository userRepository;
    private final Validator validator;

    public AnuncioService(IAnuncioRepository anuncioRepository, IProdutoRepository produtoRepository,
            IUserRepository userRepository) {
        this.anuncioRepository = anuncioRepository;
        this.produtoRepository = produtoRepository;
        this.userRepository = userRepository;
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void adicionarAnuncio(Anuncio anuncio) {
        validar(anuncio);
        anuncioRepository.salvar(anuncio);
    }

    public List<Anuncio> listarAnuncios() {
        return anuncioRepository.buscarTodos();
    }

    public Optional<Anuncio> buscarAnuncioPorId(UUID id) {
        return anuncioRepository.buscarPorId(id);
    }

    public List<Anuncio> buscarAnunciosPorVendedor(UUID vendedorId) {
        return anuncioRepository.buscarPorVendedor(vendedorId);
    }

    public Anuncio atualizarAnuncio(Anuncio anuncio) {
        validar(anuncio);
        return anuncioRepository.atualizar(anuncio);
    }

    public void removerAnuncio(UUID id) {
        anuncioRepository.deletar(id);
    }

    public void reativarAnuncio(UUID id) {
        anuncioRepository.reativar(id);
    }

    public void desativarAnunciosDoVendedor(UUID vendedorId) {
        anuncioRepository.desativarTodosDoVendedor(vendedorId);
    }

    private void validar(Anuncio anuncio) {
        Set<ConstraintViolation<Anuncio>> violations = validator.validate(anuncio);
        List<String> erros = new ArrayList<>(violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));

        if (anuncio.getProduto() == null
                || produtoRepository.buscarPorId(anuncio.getProduto().getId()).isEmpty()) {
            erros.add("Produto informado não existe no catálogo");
        }
        if (anuncio.getVendedor() == null
                || userRepository.buscarPorId(anuncio.getVendedor().getId()).isEmpty()) {
            erros.add("Vendedor informado não existe");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoAnuncioException(erros.stream().sorted().collect(Collectors.toList()));
        }
    }
}
