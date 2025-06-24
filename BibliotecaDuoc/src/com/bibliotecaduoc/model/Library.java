package com.bibliotecaduoc.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Library {
    private ArrayList<Book> books;
    private HashMap<String, User> users;

    public Library() {
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
}