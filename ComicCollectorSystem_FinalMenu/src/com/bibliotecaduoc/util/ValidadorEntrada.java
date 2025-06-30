package com.bibliotecaduoc.util;
import java.util.Scanner;

public class ValidadorEntrada {
    public static int readValidInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                SolicitanteUsuario.emptyNumberEntryMessage();
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                SolicitanteUsuario.newAttemptForInvalidNumberEntry();
            }
        }
    }

    public static String readValidString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                SolicitanteUsuario.emtpyStringEntryMessage();
                continue;
            }
            try {
                Double.parseDouble(input);
                SolicitanteUsuario.newAttemptForInvalidStringEntry();
            } catch (NumberFormatException e) {
                return input;
            }
        }
    }
} 

