package com.bibliotecaduoc.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {
    private ArrayList<Libro> books;
    private HashMap<String, Usuario> users;

    public Biblioteca() {
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
    }

    public ArrayList<Libro> getBooks() {
        return books;
    }

    public HashMap<String, Usuario> getUsers() {
        return users;
    }
}