/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication3;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            double precioBase = 100.0; // Precio base de la entrada
            boolean continuar = true;
            
            while (continuar) {
                mostrarMenu();
                int opcion = scanner.nextInt();
                
                switch (opcion) {
                    case 1 -> comprarEntrada(scanner, precioBase);
                    case 2 -> {
                        System.out.println("Gracias por su visita. ¡Hasta luego!");
                        continuar = false;
                    }
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            }
        } // Precio base de la entrada
    }

    private static void mostrarMenu() {
        System.out.println("----- Menú Principal -----");
        System.out.println("1. Comprar entrada");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción (1 o 2): ");
    }

    private static void mostrarPlanoTeatro() {
        System.out.println("----- Plano del Teatro -----");
        System.out.println("Zona A: [X][X][X][X][X]");
        System.out.println("Zona B: [X][X][X][X][X]");
        System.out.println("Zona C: [X][X][X][X][X]");
        System.out.println("Las 'X' representan asientos disponibles.");
    }

    private static void comprarEntrada(Scanner scanner, double precioBase) {
        mostrarPlanoTeatro();
        System.out.print("Seleccione la ubicación del asiento (A, B, C): ");
        char zona = scanner.next().toUpperCase().charAt(0);

        if (zona != 'A' && zona != 'B' && zona != 'C') {
            System.out.println("Ubicación no válida. Intente de nuevo.");
            return;
        }

        System.out.print("Ingrese su edad: ");
        int edad;
        try {
            edad = scanner.nextInt();
            if (edad < 0) {
                System.out.println("La edad no puede ser negativa. Intente de nuevo.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Entrada no válida. Debe ingresar un número para la edad.");
            scanner.next(); // Limpiar el buffer
            return;
        }

        // Aplicar descuentos según la zona
        double descuento = 0;
        switch (zona) {
            case 'A' -> descuento = 10; // Descuento para Zona A
            case 'B' -> descuento = 5; // Descuento para Zona B
            case 'C' -> descuento = 0; // Sin descuento para Zona C
            default -> {
            }
        }

        // Aplicar descuentos adicionales según la edad
        if (edad < 18) {
            descuento += 10; // Descuento adicional para estudiantes
        } else if (edad >= 65) {
            descuento += 15; // Descuento adicional para tercera edad
        }

        // Calcular precio final
        double precioFinal = calcularPrecioFinal(precioBase, descuento);

        // Mostrar resumen de la compra
        System.out.println("----- Resumen de la Compra -----");
        System.out.println("Ubicación del asiento: Zona " + zona);
        System.out.println("Precio base de la entrada: $" + precioBase);
        System.out.println("Descuento aplicado: " + descuento + "%");
        System.out.printf("Precio final a pagar: $%.2f%n", precioFinal);

        // Preguntar si desea realizar otra compra
        System.out.print("¿Desea realizar otra compra? (s/n): ");
        char otraCompra = scanner.next().toLowerCase().charAt(0);
        if (otraCompra != 's') {
            System.out.println("Gracias por su compra. ¡Hasta luego!");
        }
    }

    private static double calcularPrecioFinal(double precioBase, double descuento) {
        return precioBase - (precioBase * descuento / 100);
    }
}