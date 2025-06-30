package com.bibliotecaduoc.exception;

public class ExcepcionBiblioteca extends Exception {
    public ExcepcionBiblioteca() {
        super("Se ha producido un error en la biblioteca.");
    }
    
    public ExcepcionBiblioteca(String message) {
        super(message);
    }
}
