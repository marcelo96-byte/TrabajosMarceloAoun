package com.bibliotecaduoc.controller;

import com.bibliotecaduoc.model.Library;
import com.bibliotecaduoc.model.Book;
import com.bibliotecaduoc.model.User;

import com.bibliotecaduoc.exception.BookNotFoundException;
import com.bibliotecaduoc.exception.BookAlreadyLoanedException;
import com.bibliotecaduoc.exception.NoBooksToReturnException;
import com.bibliotecaduoc.exception.UserNotFoundException;
import com.bibliotecaduoc.exception.InvalidCSVFormatException;

import com.bibliotecaduoc.util.UserPrompter;
import com.bibliotecaduoc.util.identification.IdentificationValidator;
import com.bibliotecaduoc.util.csv.CSVContentExtractor;

import java.util.Scanner;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;


public class LibraryController {
    private Library library;
    
    public LibraryController(Library library) {
        this.library = library;
    }
    
    public void addBook(Scanner scanner) {
        UserPrompter.enterNewBookDataMessage();
        String title = UserPrompter.readValidatedStringWithPrompt(scanner, "Título:");
        String author = UserPrompter.readValidatedStringWithPrompt(scanner, "Autor:");

        for (Book existingBook : library.getBooks()) {
            if (existingBook.getTitle().equalsIgnoreCase(title)) {
                UserPrompter.bookWithGivenTitleAlreadyExistMessage();
                return;
            }
        }

        Book newBook = new Book(title, author, true);
        library.getBooks().add(newBook);
        System.out.println("Libro agregado: " + title + " de " + author);
    }
    
    public void findBookByTitle(Scanner scanner) {
        String title = UserPrompter.readValidatedStringWithPrompt(scanner, "Ingrese el título del libro que desea buscar:");

        try {
            Book book = getBookByTitleOrThrow(title);
            System.out.println("Libro encontrado: ");
            System.out.println("- Título: " + book.getTitle());
            System.out.println("- Autor: " + book.getAuthor());
            System.out.println("- Disponible: " + (book.isAvailable() ? "Sí" : "No"));
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void listAvailableBooks() {
        boolean found = false;
     
        System.out.println("Lista de libros disponibles:");
        for (Book book : library.getBooks()) {
            if (book.isAvailable()) {
                found = true;
                System.out.println("- " + book.getTitle() + " de " + book.getAuthor());
            }
        }

        if (!found) {
            UserPrompter.noBooksAvailablesMessage();
        }
    }
    
     /**
     * Maneja todo el proceso de solicitud de un libro:
     * 1. Verifica si el libro existe y está disponible.
     * 2. Solicita al usuario su RUT, validando si ya está registrado.
     * 3. Si el usuario no está registrado, permite registrarlo en el momento.
     * 4. Asigna el libro al usuario y actualiza su disponibilidad.
     */
    public void borrowBook(Scanner scanner) {
        String title = UserPrompter.readValidatedStringWithPrompt(scanner, "Ingrese el título del libro que desea solicitar, por ejemplo: Rayuela o Cien años de soledad: ");

        try {
            Book bookToBorrow = getBookByTitleOrThrow(title);
            if (!bookToBorrow.isAvailable()) {
                throw new BookAlreadyLoanedException("El libro '" + title + "' ya está prestado.");
            }
            
            // Step 1: Preguntar al usuario por su rut
            String rut = promptUserForEnteringAValidRUT(scanner);
            
            // Step 2: Encontrar rut o crear nuevo registro
            User user = library.getUsers().get(rut);
            Boolean userNotFound = user == null;
            if (userNotFound) {
                UserPrompter.userWithGivenRutNotFoundAndRegisterMessage();
                user = registerNewUser(rut, scanner);
            }
            // Step 3: Asignar nuevo libro al usuario y marcarlo como no disponible
            user.getBooks().add(bookToBorrow);
            bookToBorrow.setAvailable(false);
   
            System.out.println("El libro '" + title + "' ha sido prestado correctamente.");

        } catch (BookNotFoundException | BookAlreadyLoanedException e) {
            System.out.println(e.getMessage());
        }
    }
    
     /**
     * Maneja el proceso completo de devolución de libros:
     * - Solicita el RUT del usuario.
     * - Verifica que el usuario exista y tenga libros para devolver.
     * - Muestra los libros prestados y permite seleccionar cuál devolver.
     * - Actualiza el estado del libro y la lista del usuario.
     * - Controla errores mediante excepciones personalizadas.
     */
    public void returnBook(Scanner scanner) {
        try {
            String rut = UserPrompter.readValidatedStringWithPrompt(scanner, "Ingrese su RUT: ");

            User user = library.getUsers().get(rut);
            if (user == null) {
                throw new UserNotFoundException("No se encontró un usuario con el RUT proporcionado.");
            }

            ArrayList<Book> borrowedBooks = user.getBooks();
            if (borrowedBooks.isEmpty()) {
                throw new NoBooksToReturnException("El usuario no tiene libros para devolver.");
            }

            System.out.println("Libros actualmente prestados:");
            for (int i = 0; i < borrowedBooks.size(); i++) {
                Book book = borrowedBooks.get(i);
                System.out.println((i + 1) + ". " + book.getTitle() + " de " + book.getAuthor());
            }

            int option = -1;
            while (true) {
                option = UserPrompter.readValidatedIntWithPrompt(scanner, "Ingrese el número del libro que desea devolver:");
                if (option >= 1 && option <= borrowedBooks.size()) {
                    break;
                }
                UserPrompter.invalidOptionAndTryAgainMessage();
            }

            Book returnedBook = borrowedBooks.get(option - 1);
            returnedBook.setAvailable(true);
            borrowedBooks.remove(returnedBook);

            System.out.println("El libro '" + returnedBook.getTitle() + "' ha sido devuelto correctamente.");

        } catch (UserNotFoundException | NoBooksToReturnException e) {
            System.out.println(e.getMessage());
        }
    }
    
     /**
     * Carga libros desde un archivo CSV.
     * 
     * Acepta delimitadores por coma o punto y coma.
     * Ejemplos válidos de líneas CSV:
     *  - Cien años de soledad;Gabriel García Márquez;true
     *  - Rayuela,Gabriel García Márquez,true
     * 
     * Las líneas con formato incorrecto son ignoradas.
     */
    public void loadBooksFromCSV(Scanner scanner) {
        String filepath = UserPrompter.readValidatedStringWithPrompt(scanner,
            "Ruta del archivo de libros (CSV), por ejemplo: C:/Documentos/libros.csv o /Users/tuusuario/libros.csv: ");
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean firstLine = true;
            int loadedCount = 0;
            while ((line = reader.readLine()) != null) {
                if (firstLine && line.toLowerCase().contains("titulo")) {
                    firstLine = false;
                    continue; // Saltar encabezado
                }
                // Agregar el libro a la librería
                try {
                    Book book = CSVContentExtractor.extractBookFromCSVLine(line);
                    library.getBooks().add(book);
                    loadedCount++;
                } catch (InvalidCSVFormatException e) {
                    System.out.println("Libro ignorado por formato incorrecto: " + e.getMessage());
                }
            }

            if (loadedCount > 0) {
                System.out.println("Libros cargados desde archivo. Total: " + loadedCount);
            } else {
                System.out.println("No se cargaron libros. Verifica el formato del archivo.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de libros: " + e.getMessage());
        }
    }
    
     /**
     * Carga usuarios desde un archivo CSV.
     * 
     * Acepta los siguientes formatos delimitados por coma o punto y coma:
     * Ejemplos:
     *  - 12345678-9;Juan;Pérez;Av. Siempre Viva 123;987654321
     *  - 98765432-1,María,Gómez,Calle Falsa 456,912345678
     * 
     * Si una línea es inválida o el teléfono no es numérico, será ignorada y se notifica al usuario.
     */
    public void loadUsersFromCSV(Scanner scanner) {
        String filepath = UserPrompter.readValidatedStringWithPrompt(scanner,
            "Ruta del archivo de usuarios (CSV), por ejemplo: C:/Documentos/usuarios.csv o /Users/tuusuario/usuarios.csv: ");
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean firstLine = true;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                if (firstLine && line.toLowerCase().contains("rut")) {
                    firstLine = false;
                    continue; // Saltar encabezado
                }

                try {
                    User user = CSVContentExtractor.extractUserFromCSVLine(line);
                    library.getUsers().put(user.getRUT(), user);
                    loadedCount++;
                } catch (InvalidCSVFormatException | NumberFormatException e) {
                    System.out.println("Línea ignorada: " + e.getMessage());
                }
            }

            if (loadedCount > 0) {
                System.out.println("Usuarios cargados desde archivo. Total: " + loadedCount);
            } else {
                System.out.println("No se cargaron usuarios. Verifica el formato del archivo.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de usuarios: " + e.getMessage());
        }
    }
    
     /**
     * Genera un resumen de los préstamos actuales y lo guarda en un archivo TXT.
     * Recorre todos los usuarios registrados y lista los libros que tienen prestados.
     * El resumen se guarda en la ruta especificada por el usuario.
     */
    public void exportLoanSummary(Scanner scanner) {
        String filepath = UserPrompter.readValidatedStringWithPrompt(scanner, "Ruta donde desea guardar el resumen (TXT), por ejemplo: C:/Documentos/resumen.txt o /Users/tuusuario/resumen.txt: ");
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("=== RESUMEN DE PRÉSTAMOS ===\n");
            for (User user : library.getUsers().values()) {
                if (!user.getBooks().isEmpty()) {
                    writer.write("\nUsuario: " + user.getRUT() + " - " + user.getBooks().size() + " libros prestados\n");
                    for (Book book : user.getBooks()) {
                        writer.write("- " + book.getTitle() + " de " + book.getAuthor() + "\n");
                    }
                }
            }
            System.out.println("Resumen de préstamos guardado en archivo.");
        } catch (IOException e) {
            System.out.println("Error al guardar el resumen: " + e.getMessage());
        }
    }
    
    
    private Book getBookByTitleOrThrow(String title) throws BookNotFoundException {
        for (Book book : library.getBooks()) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        throw new BookNotFoundException("No se encontró el libro '" + title + "'.");
    }
    
    private User registerNewUser(String rut, Scanner scanner) {
        UserPrompter.registerNewUserMessage();
        String name = UserPrompter.readValidatedStringWithPrompt(scanner, "Nombre: ");
        String lastName = UserPrompter.readValidatedStringWithPrompt(scanner, "Apellido: ");
        String address = UserPrompter.readValidatedStringWithPrompt(scanner, "Dirección: ");
        int phoneNumber = UserPrompter.readValidatedIntWithPrompt(scanner, "Teléfono: ");
   
        User newUser = new User(rut, name, lastName, address, phoneNumber);
        library.getUsers().put(rut, newUser);
        System.out.println("Usuario registrado exitosamente.");
     
        return newUser;
    }
    
     private String promptUserForEnteringAValidRUT(Scanner scanner) {
        while (true) {
            String personRUT = IdentificationValidator.getValidatedClientRut(scanner);
            return personRUT;
        }
    }
  
}
