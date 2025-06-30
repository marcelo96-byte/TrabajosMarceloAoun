package com.bibliotecaduoc.exception;

public class ExcepcionSinLibrosParaDevolver extends ExcepcionBiblioteca {
    public ExcepcionSinLibrosParaDevolver() {
        super("No tiene libros para devolver");
    }
    
    public ExcepcionSinLibrosParaDevolver(String message) {
        super(message);
    }
}
