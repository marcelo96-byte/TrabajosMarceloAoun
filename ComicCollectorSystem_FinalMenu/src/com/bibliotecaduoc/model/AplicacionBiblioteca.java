package com.bibliotecaduoc.model;

import java.util.*;

public class AplicacionBiblioteca {
    public static void main(String[] args) {
        Biblioteca library = new Biblioteca();

        Libro b1 = new Libro("Cien años de soledad", "Gabriel García Márquez", true);
        Libro b2 = new Libro("El Principito", "Antoine de Saint-Exupéry", true);
        library.getBooks().add(b1);
        library.getBooks().add(b2);

        Usuario u1 = new Usuario("11.111.111-1", "Camila", "Rojas", "Av. Siempre Viva", 123456789);
        Usuario u2 = new Usuario("22.222.222-2", "Diego", "Fuentes", "Calle Falsa", 987654321);
        library.getUsers().put(u1.getRUT(), u1);
        library.getUsers().put(u2.getRUT(), u2);

        TreeSet<Libro> librosOrdenados = new TreeSet<>(library.getBooks());
        TreeSet<Usuario> usuariosOrdenados = new TreeSet<>(library.getUsers().values());

        System.out.println("TreeSet de libros ordenados:");
        for (Libro l : librosOrdenados) {
            System.out.println("- " + l);
        }

        System.out.println("\nTreeSet de usuarios ordenados:");
        for (Usuario u : usuariosOrdenados) {
            System.out.println("- " + u);
        }
    }
}