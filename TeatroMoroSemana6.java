/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package HelloWorld;

  import java.util.ArrayList;
import java.util.Scanner;

import java.util.*;

public class TeatroMoroSemana6 {

    private static final int CAPACIDAD_SALA = 50;
    private static final double PRECIO_UNITARIO = 10000;
    private static final long TIEMPO_RESERVA_MS = 5 * 60 * 1000;

    private static int asientosDisponibles = CAPACIDAD_SALA;
    private static double totalIngresos = 0;
    private static int cantidadEntradasVendidas = 0;

    private static final Map<Integer, Long> asientosReservados = new HashMap<>();
    private static final Map<Integer, String> tipoEntradaAsientos = new HashMap<>();  // Map para saber si es VIP o Regular
    private static final Set<Integer> asientosPagados = new HashSet<>();
    private static final List<String> detallesEntradasVendidas = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            limpiarReservasExpiradas();

            System.out.println("\n--- Menú de Venta ---");
            System.out.println("1. Reservar asientos");
            System.out.println("2. Modificar una reserva existente");
            System.out.println("3. Pagar asientos reservados");
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> reservarEntradas(scanner);
                case 2 -> modificarVenta(scanner);
                case 3 -> pagarAsientosReservados(scanner);
                case 4 -> imprimirBoleta();
                case 5 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void reservarEntradas(Scanner scanner) {
        System.out.print("¿Cuántos asientos desea reservar?: ");
        int cantidad = scanner.nextInt();

        if (cantidad <= 0) {
            System.out.println("Debe reservar al menos un asiento.");
            return;
        }

        if (cantidad > asientosDisponibles) {
            System.out.println("No hay suficientes asientos disponibles.");
            return;
        }

        String tipoEntradaTemporal = "Regular";
        double descuentoTemporal = 0.0;

        System.out.println("Seleccione tipo de entrada (1 - Regular, 2 - VIP): ");
        int tipoEntrada = scanner.nextInt();
        if (tipoEntrada == 2) {
            tipoEntradaTemporal = "VIP";
            descuentoTemporal = 0.5;
        }

        List<Integer> asientosAReservar = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            int asiento = solicitarAsiento(scanner, asientosAReservar);
            if (asiento == -1) {
                return;  // Si se cancela la operación, terminamos
            }
            asientosAReservar.add(asiento);
        }

        long tiempoActual = System.currentTimeMillis();
        for (int asiento : asientosAReservar) {
            asientosReservados.put(asiento, tiempoActual);
            tipoEntradaAsientos.put(asiento, tipoEntradaTemporal);  // Almacenamos el tipo de entrada del asiento
            asientosDisponibles--;
        }

        System.out.println("Asientos reservados: " + asientosAReservar);
        System.out.println("Tipo de entrada: " + tipoEntradaTemporal + " | Descuento: " + (descuentoTemporal * 100) + "%");
    }

    private static void pagarAsientosReservados(Scanner scanner) {
        if (asientosReservados.isEmpty()) {
            System.out.println("No hay asientos reservados para pagar.");
            return;
        }

        System.out.println("Asientos reservados disponibles para pagar: " + asientosReservados.keySet());
        System.out.print("¿Cuántos asientos desea pagar?: ");
        int cantidad = scanner.nextInt();

        if (cantidad <= 0) {
            System.out.println("Debe pagar al menos un asiento.");
            return;
        }

        List<Integer> asientosPagadosLocal = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            System.out.print("Ingrese el número del asiento #" + (i + 1) + ": ");
            int asiento = scanner.nextInt();

            if (!asientosReservados.containsKey(asiento)) {
                System.out.println("El asiento " + asiento + " no está reservado.");
                i--;
                continue;
            }

            // Determinar si el asiento es VIP o Regular
            double precioFinal = PRECIO_UNITARIO;
            String tipoEntrada = tipoEntradaAsientos.get(asiento);  // Obtenemos el tipo de entrada para el asiento

            // Si es VIP, aplicar un 50% de descuento
            if ("VIP".equals(tipoEntrada)) {
                precioFinal *= 0.5;  // Descuento del 50%
            }

            detallesEntradasVendidas.add("Asiento " + asiento + " | Tipo: " + tipoEntrada + " | Precio final: $" + precioFinal);

            asientosReservados.remove(asiento);
            asientosPagados.add(asiento);
            asientosPagadosLocal.add(asiento);

            totalIngresos += precioFinal;
            cantidadEntradasVendidas++;
            asientosDisponibles++;

            System.out.println("[DEBUG] Asiento " + asiento + " pagado correctamente. Total ingresos: " + totalIngresos);
        }

        if (!asientosPagadosLocal.isEmpty()) {
            System.out.println("Pago realizado para: " + asientosPagadosLocal);
            System.out.println("Total pagado: $" + (asientosPagadosLocal.size() * PRECIO_UNITARIO * 0.5));  // Se aplica el descuento si es VIP
        } else {
            System.out.println("No se procesó ningún pago.");
        }
    }

    private static void imprimirBoleta() {
        limpiarReservasExpiradas();
        System.out.println("\n--- Boleta ---");

        if (!asientosPagados.isEmpty()) {
            System.out.println("Asientos pagados: " + asientosPagados);

            for (String detalle : detallesEntradasVendidas) {
                System.out.println("- " + detalle);
            }

            System.out.println("[DEBUG] Ingresos acumulados: $" + totalIngresos);
        } else {
            System.out.println("No se han realizado compras aún.");
        }

        System.out.println("Gracias por su compra. ¡Disfrute la función!");
    }

    private static void limpiarReservasExpiradas() {
        long ahora = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, Long>> iterator = asientosReservados.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Long> entry = iterator.next();
            if (ahora - entry.getValue() > TIEMPO_RESERVA_MS) {
                System.out.println("Reserva expirada para asiento " + entry.getKey());
                iterator.remove();
                tipoEntradaAsientos.remove(entry.getKey());  // Remover el tipo de entrada asociado al asiento
            }
        }
    }

    // Función reutilizada para validar asientos
    private static boolean esAsientoValido(int asiento) {
        return asiento >= 1 && asiento <= CAPACIDAD_SALA && 
               !asientosReservados.containsKey(asiento) && 
               !asientosPagados.contains(asiento);
    }

    // Solicita un asiento y verifica si es válido
    private static int solicitarAsiento(Scanner scanner, List<Integer> asientosAReservar) {
        System.out.print("Ingrese el número del asiento (1-50): ");
        int asiento = scanner.nextInt();

        if (!esAsientoValido(asiento)) {
            System.out.println("El asiento " + asiento + " no está disponible.");
            return -1;
        }

        return asiento;
    }

    
        private static void modificarVenta(Scanner scanner) {
    if (asientosReservados.isEmpty()) {
        System.out.println("No hay reservas para modificar.");
        return;
    }

    System.out.println("Asientos actualmente reservados: " + asientosReservados.keySet());
    System.out.print("Ingrese el número del asiento que desea cambiar: ");
    int asientoViejo = scanner.nextInt();

    if (!asientosReservados.containsKey(asientoViejo)) {
        System.out.println("Ese asiento no está reservado.");
        return;
    }

    System.out.print("Ingrese el nuevo número de asiento (1-50): ");
    int asientoNuevo = scanner.nextInt();

    if (!esAsientoValido(asientoNuevo)) {
        System.out.println("El nuevo asiento no está disponible o no es válido.");
        return;
    }

    // Transferimos la reserva
    long tiempoReserva = asientosReservados.remove(asientoViejo);
    asientosReservados.put(asientoNuevo, tiempoReserva);

    // Transferimos el tipo de entrada también
    String tipo = tipoEntradaAsientos.remove(asientoViejo);
    tipoEntradaAsientos.put(asientoNuevo, tipo);

    System.out.println("Reserva modificada: Asiento " + asientoViejo + " cambiado por asiento " + asientoNuevo);
}

       
    }

