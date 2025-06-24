package com.bibliotecaduoc.exception;

public class LibraryException extends Exception {
    public LibraryException() {
        super("Se ha producido un error en la biblioteca.");
    }
    
    public LibraryException(String message) {
        super(message);
    }
}
