package com.mps.produtos.application;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.domain.Produto;
import com.mps.produtos.domain.exception.ValidacaoProdutoException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ProdutoService {

    private final IProdutoRepository produtoRepository;
    private final Validator validator;

    public ProdutoService(IProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void adicionarProduto(Produto produto) {
        validar(produto);
        produtoRepository.salvar(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.buscarTodos();
    }

    public Optional<Produto> buscarProdutoPorId(UUID id) {
        return produtoRepository.buscarPorId(id);
    }

    public Produto atualizarProduto(Produto produto) {
        validar(produto);
        return produtoRepository.atualizar(produto);
    }

    public void removerProduto(UUID id) {
        produtoRepository.deletar(id);
    }

    public void reativarProduto(UUID id) {
        produtoRepository.reativar(id);
    }

    private void validar(Produto produto) {
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        if (!violations.isEmpty()) {
            List<String> erros = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.toList());
            throw new ValidacaoProdutoException(erros);
        }
    }
}
