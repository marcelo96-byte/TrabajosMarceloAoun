package com.bibliotecaduoc.controller;

import com.bibliotecaduoc.model.Biblioteca;
import com.bibliotecaduoc.model.Libro;
import com.bibliotecaduoc.model.Usuario;

import com.bibliotecaduoc.exception.ExcepcionLibroNoEncontrado;
import com.bibliotecaduoc.exception.ExcepcionLibroPrestado;
import com.bibliotecaduoc.exception.ExcepcionSinLibrosParaDevolver;
import com.bibliotecaduoc.exception.ExcepcionUsuarioNoEncontrado;
import com.bibliotecaduoc.exception.ExcepcionFormatoCSVInvalido;

import com.bibliotecaduoc.util.SolicitanteUsuario;
import com.bibliotecaduoc.util.identification.ValidadorIdentificacion;
import com.bibliotecaduoc.util.csv.LectorContenidoCSV;

import java.util.Scanner;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;


public class ControladorBiblioteca {
    private Biblioteca library;
    
    public ControladorBiblioteca(Biblioteca library) {
        this.library = library;
    }
    
    public void addBook(Scanner scanner) {
        SolicitanteUsuario.enterNewBookDataMessage();
        String titulo = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Título:");
        String autor = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Autor:");

        for (Libro existingBook : library.getBooks()) {
            if (existingBook.getTitle().equalsIgnoreCase(titulo)) {
                SolicitanteUsuario.bookWithGivenTitleAlreadyExistMessage();
                return;
            }
        }

        Libro newBook = new Libro(titulo, autor, true);
        library.getBooks().add(newBook);
        System.out.println("Libro agregado: " + titulo + " de " + autor);
    }
    
    public void findBookByTitle(Scanner scanner) {
        String titulo = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Ingrese el título del libro que desea buscar:");

        try {
            Libro book = getBookByTitleOrThrow(titulo);
            System.out.println("Libro encontrado: ");
            System.out.println("- Título: " + book.getTitle());
            System.out.println("- Autor: " + book.getAuthor());
            System.out.println("- Disponible: " + (book.isAvailable() ? "Sí" : "No"));
        } catch (ExcepcionLibroNoEncontrado e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void listAvailableBooks() {
        boolean found = false;
     
        System.out.println("Lista de libros disponibles:");
        for (Libro book : library.getBooks()) {
            if (book.isAvailable()) {
                found = true;
                System.out.println("- " + book.getTitle() + " de " + book.getAuthor());
            }
        }

        if (!found) {
            SolicitanteUsuario.noBooksAvailablesMessage();
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
        String titulo = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Ingrese el título del cómic que desea pedir prestado (ej: Batman, Spider-Man, Deadpool):");

        try {
            Libro bookToBorrow = getBookByTitleOrThrow(titulo);
            if (!bookToBorrow.isAvailable()) {
                throw new ExcepcionLibroPrestado("El libro '" + titulo + "' ya está prestado.");
            }
            
            // Step 1: Preguntar al usuario por su rut
            String rut = promptUserForEnteringAValidRUT(scanner);
            
            // Step 2: Encontrar rut o crear nuevo registro
            Usuario user = library.getUsers().get(rut);
            Boolean userNotFound = user == null;
            if (userNotFound) {
                SolicitanteUsuario.userWithGivenRutNotFoundAndRegisterMessage();
                user = registerNewUser(rut, scanner);
            }
            // Step 3: Asignar nuevo libro al usuario y marcarlo como no disponible
            user.getBooks().add(bookToBorrow);
            bookToBorrow.setAvailable(false);
   
            System.out.println("El libro '" + titulo + "' ha sido prestado correctamente.");

        } catch (ExcepcionLibroNoEncontrado | ExcepcionLibroPrestado e) {
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
            String rut = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Ingrese su RUT: ");

            Usuario user = library.getUsers().get(rut);
            if (user == null) {
                throw new ExcepcionUsuarioNoEncontrado("No se encontró un usuario con el RUT proporcionado.");
            }

            ArrayList<Libro> borrowedBooks = user.getBooks();
            if (borrowedBooks.isEmpty()) {
                throw new ExcepcionSinLibrosParaDevolver("El usuario no tiene libros para devolver.");
            }

            System.out.println("Libros actualmente prestados:");
            for (int i = 0; i < borrowedBooks.size(); i++) {
                Libro book = borrowedBooks.get(i);
                System.out.println((i + 1) + ". " + book.getTitle() + " de " + book.getAuthor());
            }

            int option = -1;
            while (true) {
                option = SolicitanteUsuario.readValidatedIntWithPrompt(scanner, "Ingrese el número del libro que desea devolver:");
                if (option >= 1 && option <= borrowedBooks.size()) {
                    break;
                }
                SolicitanteUsuario.invalidOptionAndTryAgainMessage();
            }

            Libro returnedBook = borrowedBooks.get(option - 1);
            returnedBook.setAvailable(true);
            borrowedBooks.remove(returnedBook);

            System.out.println("El libro '" + returnedBook.getTitle() + "' ha sido devuelto correctamente.");

        } catch (ExcepcionUsuarioNoEncontrado | ExcepcionSinLibrosParaDevolver e) {
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
    public void cargarLibrosDesdeCSV(Scanner scanner) {
        String filepath = SolicitanteUsuario.readValidatedStringWithPrompt(scanner,
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
                    Libro book = LectorContenidoCSV.extractBookFromCSVLine(line);
                    library.getBooks().add(book);
                    loadedCount++;
                } catch (ExcepcionFormatoCSVInvalido e) {
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
        String filepath = SolicitanteUsuario.readValidatedStringWithPrompt(scanner,
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
                    Usuario user = LectorContenidoCSV.extractUserFromCSVLine(line);
                    library.getUsers().put(user.getRUT(), user);
                    loadedCount++;
                } catch (ExcepcionFormatoCSVInvalido | NumberFormatException e) {
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
        String filepath = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Ruta donde desea guardar el resumen (TXT), por ejemplo: C:/Documentos/resumen.txt o /Users/tuusuario/resumen.txt: ");
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("=== RESUMEN DE PRÉSTAMOS ===\n");
            for (Usuario user : library.getUsers().values()) {
                if (!user.getBooks().isEmpty()) {
                    writer.write("\nUsuario: " + user.getRUT() + " - " + user.getBooks().size() + " libros prestados\n");
                    for (Libro book : user.getBooks()) {
                        writer.write("- " + book.getTitle() + " de " + book.getAuthor() + "\n");
                    }
                }
            }
            System.out.println("Resumen de préstamos guardado en archivo.");
        } catch (IOException e) {
            System.out.println("Error al guardar el resumen: " + e.getMessage());
        }
    }
    
    
    private Libro getBookByTitleOrThrow(String titulo) throws ExcepcionLibroNoEncontrado {
        for (Libro book : library.getBooks()) {
            if (book.getTitle().equalsIgnoreCase(titulo)) {
                return book;
            }
        }
        throw new ExcepcionLibroNoEncontrado("No se encontró el libro '" + titulo + "'.");
    }
    
    private Usuario registerNewUser(String rut, Scanner scanner) {
        SolicitanteUsuario.registerNewUserMessage();
        String name = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Nombre: ");
        String lastName = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Apellido: ");
        String address = SolicitanteUsuario.readValidatedStringWithPrompt(scanner, "Dirección: ");
        int phoneNumber = SolicitanteUsuario.readValidatedIntWithPrompt(scanner, "Teléfono: ");
   
        Usuario newUser = new Usuario(rut, name, lastName, address, phoneNumber);
        library.getUsers().put(rut, newUser);
        System.out.println("Usuario registrado exitosamente.");
     
        return newUser;
    }
    
     private String promptUserForEnteringAValidRUT(Scanner scanner) {
        while (true) {
            String personRUT = ValidadorIdentificacion.getValidatedClientRut(scanner);
            return personRUT;
        }
    }
  public void precargarComics() {
    library.getBooks().add(new Libro("Superman", "DC Comics", true));
    library.getBooks().add(new Libro("Batman", "DC Comics", true));
    library.getBooks().add(new Libro("Spider-Man", "Marvel", true));
    library.getBooks().add(new Libro("Iron Man", "Marvel", true));
    library.getBooks().add(new Libro("The Flash", "DC Comics", true));
    library.getBooks().add(new Libro("Wolverine", "Marvel", true));
    library.getBooks().add(new Libro("Wonder Woman", "DC Comics", true));
    library.getBooks().add(new Libro("Deadpool", "Marvel", true));
    library.getBooks().add(new Libro("Green Lantern", "DC Comics", true));
    library.getBooks().add(new Libro("Doctor Strange", "Marvel", true));
}

}
