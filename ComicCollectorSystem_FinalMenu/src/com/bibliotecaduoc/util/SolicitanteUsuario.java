package com.bibliotecaduoc.util;
import java.util.Scanner;

public class SolicitanteUsuario {
    public static int readValidatedIntWithPrompt(Scanner scanner, String message) {
        System.out.print(message);
        return ValidadorEntrada.readValidInt(scanner);
    }

    public static String readValidatedStringWithPrompt(Scanner scanner, String message) {
        System.out.print(message);
        return ValidadorEntrada.readValidString(scanner);
    }
    
    public static void displayMainMenuOptions() {
        System.out.println("===== Bienvenido a ComicCollectorSystem =====");
        System.out.println("1. Buscar un libro");
        System.out.println("2. Agregar un libro nuevo");
        System.out.println("3. Ver lista de libros disponibles");
        System.out.println("4. Pedir un libro prestado");
        System.out.println("5. Devolver un libro");
        System.out.println("6. Cargar libros desde archivo CSV");
        System.out.println("7. Cargar usuarios desde archivo CSV");
        System.out.println("8. Exportar resumen de préstamos a archivo de texto");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    public static void emptyNumberEntryMessage() {
        System.out.print("Entrada vacía. Por favor ingrese un número: ");
    }
    
    public static void newAttemptForInvalidNumberEntry() {
        System.out.print("Entrada inválida, debe ingresar solo números. Intente otra vez: ");
    }
    
    public static void emtpyStringEntryMessage() {
        System.out.print("Entrada vacía. Por favor ingrese un texto: ");
    }
    
     public static void newAttemptForInvalidStringEntry() {
        System.out.print("Entrada inválida. No se permiten números. Intente otra vez: ");
    }
     
    public static void enterNewBookDataMessage() {
        System.out.print("Ingrese los datos del libro que desea agregar \n");
    }
    
    public static void registerNewUserMessage() {
        System.out.print("Ingrese los datos de la persona que solicita el libro \n");
    }
    
    public static void bookWithGivenTitleAlreadyExistMessage() {
        System.out.println("Ya existe un libro con ese título.");
    }
    
    public static void noBooksAvailablesMessage() {
        System.out.println("No hay libros disponibles en este momento.");
    }
    
    public static void userWithGivenRutNotFoundAndRegisterMessage() {
        System.out.println("No encontramos un usuario con ese RUT. Procederemos a registrarlo.");
    }
    
    public static void invalidOptionAndTryAgainMessage() {
        System.out.println("Opción inválida. Intente nuevamente.");
    }
}