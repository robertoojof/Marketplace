package com.mps.produtos.domain.catalogo;

final class Recuo {

    private static final String UNIDADE = "   ";

    private Recuo() {
    }

    static String ate(int nivel) {
        return UNIDADE.repeat(nivel);
    }
}
