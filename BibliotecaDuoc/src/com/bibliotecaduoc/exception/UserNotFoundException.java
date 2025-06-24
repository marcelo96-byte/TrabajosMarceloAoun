package com.bibliotecaduoc.exception;

public class UserNotFoundException extends LibraryException {
     public UserNotFoundException() {
        super("Usuario no registrado en la biblioteca");
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
}
