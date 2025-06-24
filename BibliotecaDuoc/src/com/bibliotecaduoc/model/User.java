package com.bibliotecaduoc.model;

import java.util.ArrayList;

public class User implements Comparable<User> {
    private String RUT;
    private String firstName;
    private String lastName;
    private String address;
    private int phoneNumber;
    private ArrayList<Book> books;

    public User(String RUT, String firstName, String lastName, String address, int phoneNumber) {
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

    public ArrayList<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return getFullName() + " (RUT: " + RUT + ")";
    }

    @Override
    public int compareTo(User other) {
        return this.getFullName().compareToIgnoreCase(other.getFullName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return RUT.equals(other.RUT);
    }

    @Override
    public int hashCode() {
        return RUT.hashCode();
    }
}