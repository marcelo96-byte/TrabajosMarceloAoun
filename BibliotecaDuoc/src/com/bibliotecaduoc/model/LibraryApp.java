package com.bibliotecaduoc.model;

import java.util.*;

public class LibraryApp {
    public static void main(String[] args) {
        Library library = new Library();

        Book b1 = new Book("Cien años de soledad", "Gabriel García Márquez", true);
        Book b2 = new Book("El Principito", "Antoine de Saint-Exupéry", true);
        library.getBooks().add(b1);
        library.getBooks().add(b2);

        User u1 = new User("11.111.111-1", "Camila", "Rojas", "Av. Siempre Viva", 123456789);
        User u2 = new User("22.222.222-2", "Diego", "Fuentes", "Calle Falsa", 987654321);
        library.getUsers().put(u1.getRUT(), u1);
        library.getUsers().put(u2.getRUT(), u2);

        TreeSet<Book> librosOrdenados = new TreeSet<>(library.getBooks());
        TreeSet<User> usuariosOrdenados = new TreeSet<>(library.getUsers().values());

        System.out.println("TreeSet de libros ordenados:");
        for (Book l : librosOrdenados) {
            System.out.println("- " + l);
        }

        System.out.println("\nTreeSet de usuarios ordenados:");
        for (User u : usuariosOrdenados) {
            System.out.println("- " + u);
        }
    }
}