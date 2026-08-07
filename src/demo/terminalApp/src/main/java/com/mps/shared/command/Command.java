package com.mps.shared.command;

public interface Command<T> {

    T executar();

    String descricao();
}
