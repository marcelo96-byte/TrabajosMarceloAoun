package com.bibliotecaduoc.util;
import java.util.Scanner;

public class InputValidator {
    public static int readValidInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                UserPrompter.emptyNumberEntryMessage();
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                UserPrompter.newAttemptForInvalidNumberEntry();
            }
        }
    }

    public static String readValidString(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                UserPrompter.emtpyStringEntryMessage();
                continue;
            }
            try {
                Double.parseDouble(input);
                UserPrompter.newAttemptForInvalidStringEntry();
            } catch (NumberFormatException e) {
                return input;
            }
        }
    }
} 

