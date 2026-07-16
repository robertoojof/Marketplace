package com.mps.anuncios.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mps.anuncios.domain.Anuncio;
import com.mps.anuncios.domain.IAnuncioRepository;
import com.mps.shared.exception.RepositorioException;

public class InMemoryAnuncioRepository implements IAnuncioRepository {

    private final List<Anuncio> anuncios = new ArrayList<>();

    @Override
    public void salvar(Anuncio anuncio) {
        if (anuncio.getId() == null) {
            anuncio.setId(UUID.randomUUID());
        }
        anuncios.add(anuncio);
    }

    @Override
    public List<Anuncio> buscarTodos() {
        return List.copyOf(anuncios);
    }

    @Override
    public Optional<Anuncio> buscarPorId(UUID id) {
        return anuncios.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Anuncio> buscarPorVendedor(UUID vendedorId) {
        return anuncios.stream()
                .filter(a -> a.getVendedor().getId().equals(vendedorId))
                .toList();
    }

    @Override
    public Anuncio atualizar(Anuncio anuncio) {
        int index = indiceDoAnuncio(anuncio.getId());
        anuncios.set(index, anuncio);
        return anuncio;
    }

    @Override
    public void deletar(UUID id) {
        anuncios.get(indiceDoAnuncio(id)).setAtivo(false);
    }

    @Override
    public void reativar(UUID id) {
        anuncios.get(indiceDoAnuncio(id)).setAtivo(true);
    }

    @Override
    public void desativarTodosDoVendedor(UUID vendedorId) {
        anuncios.stream()
                .filter(a -> a.getVendedor().getId().equals(vendedorId))
                .forEach(a -> a.setAtivo(false));
    }

    private int indiceDoAnuncio(UUID id) {
        for (int i = 0; i < anuncios.size(); i++) {
            if (anuncios.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new RepositorioException("Anúncio não encontrado");
    }
}
