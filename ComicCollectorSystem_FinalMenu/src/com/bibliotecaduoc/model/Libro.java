package com.bibliotecaduoc.model;

public class Libro implements Comparable<Libro> {
    private String titulo;
    private String autor;
    private boolean disponible;

    public Libro(String titulo, String autor, boolean disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = disponible;
    }

    // ✅ Getters
    public String getTitle() {
        return titulo;
    }

    public String getAuthor() {
        return autor;
    }

    public boolean isAvailable() {
        return disponible;
    }

    // ✅ Setters
    public void setTitle(String titulo) {
        this.titulo = titulo;
    }

    public void setAuthor(String autor) {
        this.autor = autor;
    }

    public void setAvailable(boolean disponible) {
        this.disponible = disponible;
    }

    // ✅ Métodos para HashSet y TreeSet
    @Override
    public String toString() {
        return titulo + " de " + autor + (disponible ? " (Disponible)" : " (No disponible)");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Libro)) return false;
        Libro other = (Libro) obj;
        return titulo.equalsIgnoreCase(other.titulo) && autor.equalsIgnoreCase(other.autor);
    }

    @Override
    public int hashCode() {
        return titulo.toLowerCase().hashCode() + autor.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(Libro other) {
        return this.titulo.compareToIgnoreCase(other.titulo);
    }
}