package com.bibliotecaduoc.exception;

public class ExcepcionUsuarioNoEncontrado extends ExcepcionBiblioteca {
     public ExcepcionUsuarioNoEncontrado() {
        super("Usuario no registrado en la biblioteca");
    }
    
    public ExcepcionUsuarioNoEncontrado(String message) {
        super(message);
    }
}
