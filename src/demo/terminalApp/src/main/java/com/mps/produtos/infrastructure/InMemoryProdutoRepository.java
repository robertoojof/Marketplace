package com.mps.produtos.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.produtos.domain.IProdutoRepository;
import com.mps.produtos.domain.Produto;
import com.mps.shared.exception.RepositorioException;

public class InMemoryProdutoRepository implements IProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();

    @Override
    public void salvar(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(UUID.randomUUID());
        }
        produtos.add(produto);
    }

    @Override
    public List<Produto> buscarTodos() {
        return List.copyOf(produtos);
    }

    @Override
    public Optional<Produto> buscarPorId(UUID id) {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public Produto atualizar(Produto produto) {
        int index = indiceDoProduto(produto.getId());
        produtos.set(index, produto);
        return produto;
    }

    @Override
    public void deletar(UUID id) {
        produtos.get(indiceDoProduto(id)).setAtivo(false);
    }

    @Override
    public void reativar(UUID id) {
        produtos.get(indiceDoProduto(id)).setAtivo(true);
    }

    private int indiceDoProduto(UUID id) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new RepositorioException("Produto não encontrado");
    }
}
