package com.bibliotecaduoc.exception;

public class ExcepcionFormatoCSVInvalido extends ExcepcionBiblioteca {
    public ExcepcionFormatoCSVInvalido() {
        super("Formato CSV Invalido");
    }
    
    public ExcepcionFormatoCSVInvalido(String message) {
        super(message);
    }
}
