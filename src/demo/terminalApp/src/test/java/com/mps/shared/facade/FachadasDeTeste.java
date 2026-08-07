package com.mps.shared.facade;

public final class FachadasDeTeste {

    private FachadasDeTeste() {
    }

    public static void reiniciar() {
        FacadeSingletonController.reset();
    }
}
