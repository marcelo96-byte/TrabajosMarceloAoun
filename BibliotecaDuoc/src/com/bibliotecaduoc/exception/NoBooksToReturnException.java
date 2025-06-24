package com.bibliotecaduoc.exception;

public class NoBooksToReturnException extends LibraryException {
    public NoBooksToReturnException() {
        super("No tiene libros para devolver");
    }
    
    public NoBooksToReturnException(String message) {
        super(message);
    }
}
