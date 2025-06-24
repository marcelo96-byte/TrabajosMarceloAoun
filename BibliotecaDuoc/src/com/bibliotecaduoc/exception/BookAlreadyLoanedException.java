package com.bibliotecaduoc.exception;

public class BookAlreadyLoanedException extends LibraryException {
    public BookAlreadyLoanedException() {
        super("El libro ya ha sido prestado");
    }
    
    public BookAlreadyLoanedException(String message) {
        super(message);
    }
}
