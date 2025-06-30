package com.bibliotecaduoc.exception;

public class ExcepcionLibroPrestado extends ExcepcionBiblioteca {
    public ExcepcionLibroPrestado() {
        super("El libro ya ha sido prestado");
    }
    
    public ExcepcionLibroPrestado(String message) {
        super(message);
    }
}
