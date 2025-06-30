package com.bibliotecaduoc.util.csv;

import com.bibliotecaduoc.model.Libro;
import com.bibliotecaduoc.model.Usuario;

import com.bibliotecaduoc.exception.ExcepcionFormatoCSVInvalido;

public class LectorContenidoCSV {
    /**
    * Procesa una línea de un archivo CSV para extraer los datos del libro.
    * 
    * Formatos aceptados:
    * - Título;Autor
    * - Título,Autor
    * - Título;Autor;Disponible (se ignora el tercer campo)
    * - Título,Autor,true (se ignora el tercer campo)
    * 
    * Ejemplos válidos:
    * - Cien Años de Soledad;Gabriel García Márquez
    * - Rayuela,Julio Cortázar,true
    * 
    * @param line línea del archivo CSV
    * @return objeto Libro o Excepcion si la línea no es válida
    */
    public static Libro extractBookFromCSVLine(String line) throws ExcepcionFormatoCSVInvalido {
        String[] parts;
        if (line.contains(";")) {
            parts = line.split(";");
        } else {
            parts = line.split(",");
        }
        if (parts.length >= 2) {
            String titulo = parts[0].trim();
            String autor = parts[1].trim();
            return new Libro(titulo, autor, true); // Disponible por defecto
        }
        throw new ExcepcionFormatoCSVInvalido("Línea inválida en el CSV: " + line);
    }
    
    /**
    * Convierte una línea CSV a un objeto Usuario.
    * 
    * Acepta los siguientes formatos delimitados por coma o punto y coma:
    * 
    * Ejemplos:
    *  - 12345678-9;Juan;Pérez;Av. Siempre Viva 123;987654321
    *  - 98765432-1,María,Gómez,Calle Falsa 456,912345678
    * 
    * Si la línea no contiene 5 campos o el teléfono no es numérico, lanza ExcepcionFormatoCSVInvalido.
    */
    public static Usuario extractUserFromCSVLine(String line) throws ExcepcionFormatoCSVInvalido {
        String[] parts;

        if (line.contains(";")) {
            parts = line.split(";");
        } else {
            parts = line.split(",");
        }

        if (parts.length == 5) {
            String rut = parts[0].trim();
            String name = parts[1].trim();
            String lastName = parts[2].trim();
            String address = parts[3].trim();
            int phone = Integer.parseInt(parts[4].trim());
            return new Usuario(rut, name, lastName, address, phone);
        }

        throw new ExcepcionFormatoCSVInvalido("Formato inválido para línea de usuario: " + line);
    }
}
