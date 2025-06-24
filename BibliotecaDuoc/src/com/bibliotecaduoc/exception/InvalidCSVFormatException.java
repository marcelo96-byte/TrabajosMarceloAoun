package com.bibliotecaduoc.exception;

public class InvalidCSVFormatException extends LibraryException {
    public InvalidCSVFormatException() {
        super("Formato CSV Invalido");
    }
    
    public InvalidCSVFormatException(String message) {
        super(message);
    }
}
