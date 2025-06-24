package com.bibliotecaduoc.exception;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException() {
        super("Libro no encontrado");
    }
    
    public BookNotFoundException(String message) {
        super(message);
    }
}
