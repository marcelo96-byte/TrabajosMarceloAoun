package com.bibliotecaduoc.app;

import java.util.Scanner;

import com.bibliotecaduoc.model.Biblioteca;

import com.bibliotecaduoc.controller.ControladorBiblioteca;

import com.bibliotecaduoc.util.ValidadorEntrada;
import com.bibliotecaduoc.util.SolicitanteUsuario;


public class BibliotecaDuoc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Biblioteca library = new Biblioteca();
        ControladorBiblioteca controller = new ControladorBiblioteca(library);
        controller.precargarComics();
   
        Boolean running = true;
        while(running) {
            SolicitanteUsuario.displayMainMenuOptions();
            int option = ValidadorEntrada.readValidInt(scanner);
            if (option == 9) {
                running = false;
            } else {
                handleMenuOptions(option, scanner, controller);
            }
        }
    }
    
    public static void handleMenuOptions(int option, Scanner scanner, ControladorBiblioteca controller) {
        switch (option) {
            case 1 -> controller.findBookByTitle(scanner);
            case 2 -> controller.addBook(scanner);
            case 3 -> controller.listAvailableBooks();
            case 4 -> controller.borrowBook(scanner);
            case 5 -> controller.returnBook(scanner);
            case 6 -> controller.cargarLibrosDesdeCSV(scanner);
            case 7 -> controller.loadUsersFromCSV(scanner);
            case 8 -> controller.exportLoanSummary(scanner);
            default -> controller.findBookByTitle(scanner);
        }
    
    }
}
