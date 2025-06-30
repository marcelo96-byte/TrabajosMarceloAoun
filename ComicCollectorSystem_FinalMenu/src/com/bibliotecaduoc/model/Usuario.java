package com.bibliotecaduoc.model;

import java.util.ArrayList;

public class Usuario implements Comparable<Usuario> {
    private String RUT;
    private String firstName;
    private String lastName;
    private String address;
    private int phoneNumber;
    private ArrayList<Libro> books;

    public Usuario(String RUT, String firstName, String lastName, String address, int phoneNumber) {
        this.RUT = RUT;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.books = new ArrayList<>();
    }

    public String getRUT() {
        return RUT;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public ArrayList<Libro> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return getFullName() + " (RUT: " + RUT + ")";
    }

    @Override
    public int compareTo(Usuario other) {
        return this.getFullName().compareToIgnoreCase(other.getFullName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Usuario)) return false;
        Usuario other = (Usuario) obj;
        return RUT.equals(other.RUT);
    }

    @Override
    public int hashCode() {
        return RUT.hashCode();
    }
}