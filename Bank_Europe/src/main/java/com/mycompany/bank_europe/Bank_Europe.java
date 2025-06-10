/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



package com.mycompany.bank_europe;

import bank.cuenta.CuentaCorriente;
import bank.cuenta.CuentaAhorros;
import bank.cuenta.CuentaDigital;

import java.util.Scanner;

public class Bank_Europe {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Bienvenido a Banco Europe ===");

        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su RUT: ");
        String rut = scanner.nextLine();

        System.out.println("\nSeleccione el tipo de cuenta:");
        System.out.println("- Cuenta Corriente");
        System.out.println("- Cuenta de Ahorros");
        System.out.println("- Cuenta Digital");

        System.out.print("Escriba su elección: ");
        String tipoCuenta = scanner.nextLine().trim().toLowerCase();

       String numeroCuenta;
while (true) {
    System.out.print("Ingrese el número de cuenta (9 dígitos): ");
    numeroCuenta = scanner.nextLine().trim();

    if (numeroCuenta.matches("\\d{9}")) {
        break;
    } else {
        System.out.println(" El número de cuenta debe tener exactamente 9 dígitos numéricos.");
    }
}


        System.out.print("Ingrese el saldo inicial: ");
        double saldoInicial = Double.parseDouble(scanner.nextLine());

        switch (tipoCuenta) {
            case "cuenta corriente" -> {
                System.out.print("Ingrese la tasa de interés: ");
                double tasa = Double.parseDouble(scanner.nextLine());
                CuentaCorriente cuenta = new CuentaCorriente(numeroCuenta, saldoInicial, tasa);
                imprimirCuenta(cuenta);
            }
            case "cuenta de ahorros" -> {
                System.out.print("Ingrese la tasa de interés: ");
                double tasa = Double.parseDouble(scanner.nextLine());
                System.out.print("Ingrese la duración en meses: ");
                int duracion = Integer.parseInt(scanner.nextLine());
                CuentaAhorros cuenta = new CuentaAhorros(numeroCuenta, saldoInicial, tasa, duracion);
                imprimirCuenta(cuenta);
            }
            case "cuenta digital" -> {
                System.out.print("Ingrese la tasa de interés: ");
                double tasa = Double.parseDouble(scanner.nextLine());
                CuentaDigital cuenta = new CuentaDigital(numeroCuenta, saldoInicial, tasa, nombre, rut);
                imprimirCuenta(cuenta);
                cuenta.mostrarInformacionCliente();
            }
            default -> {
                System.out.println(" Tipo de cuenta no reconocido. Intente de nuevo.");
            }
        }

        scanner.close();
    }

    private static void imprimirCuenta(Object cuenta) {
        System.out.println("\n=== Información de la Cuenta ===");
        System.out.println(cuenta.toString());

        try {
            var metodo = cuenta.getClass().getMethod("calcularInteres");
            double interes = (double) metodo.invoke(cuenta);
            System.out.println("Interés calculado: $" + String.format("%,.2f", interes));
        } catch (Exception e) {
            System.out.println("No se pudo calcular el interés.");
        }
    }
}
