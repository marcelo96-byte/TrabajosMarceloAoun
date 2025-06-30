package com.bibliotecaduoc.exception;

public class ExcepcionLibroNoEncontrado extends ExcepcionBiblioteca {
    public ExcepcionLibroNoEncontrado() {
        super("Libro no encontrado");
    }
    
    public ExcepcionLibroNoEncontrado(String message) {
        super(message);
    }
}
