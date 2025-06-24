package com.bibliotecaduoc.util.identification;

import java.util.Scanner;

public class IdentificationValidator {
    public static String getValidatedClientRut(Scanner scanner) {
        while (true) {
            System.out.print("Ingrese RUT (formato: 12.345.678-K): ");
            String rut = scanner.nextLine().trim();
            if (validateRutInput(rut)) return rut;
            System.out.println("RUT inválido. Formato esperado: 12.345.678-K. Intente nuevamente.");
        }
    }

    private static boolean validateRutInput(String rut) {
        final int MIN_LENGTH = 11, MAX_LENGTH = 12;
        boolean isEmpty = rut.isBlank();
        boolean validLength = rut.length() >= MIN_LENGTH && rut.length() <= MAX_LENGTH;
        boolean validCheckDigit = ChileanId.isValidChileanRUT(rut);
        return !isEmpty && validLength && validCheckDigit;
    }
}
