package com.bibliotecaduoc.app;

import java.util.Scanner;

import com.bibliotecaduoc.model.Library;

import com.bibliotecaduoc.controller.LibraryController;

import com.bibliotecaduoc.util.InputValidator;
import com.bibliotecaduoc.util.UserPrompter;


public class BibliotecaDuoc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        LibraryController controller = new LibraryController(library);
   
        Boolean running = true;
        while(running) {
            UserPrompter.displayMainMenuOptions();
            int option = InputValidator.readValidInt(scanner);
            if (option == 9) {
                running = false;
            } else {
                handleMenuOptions(option, scanner, controller);
            }
        }
    }
    
    public static void handleMenuOptions(int option, Scanner scanner, LibraryController controller) {
        switch (option) {
            case 1 -> controller.findBookByTitle(scanner);
            case 2 -> controller.addBook(scanner);
            case 3 -> controller.listAvailableBooks();
            case 4 -> controller.borrowBook(scanner);
            case 5 -> controller.returnBook(scanner);
            case 6 -> controller.loadBooksFromCSV(scanner);
            case 7 -> controller.loadUsersFromCSV(scanner);
            case 8 -> controller.exportLoanSummary(scanner);
            default -> controller.findBookByTitle(scanner);
        }
    
    }
}
