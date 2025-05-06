/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.teatromorosemana8;

import java.util.*;

public class TeatroMoroSemana8 {

    public static class Cliente {
        int id;
        String nombre;
        String tipo; // Estudiante, Tercera Edad, Regular

        public Cliente(int id, String nombre, String tipo) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
        }
    }

    public static class Asiento {
        int id;
        boolean disponible;

        public Asiento(int id) {
            this.id = id;
            this.disponible = true;
        }
    }

    public static class Venta {
        int id;
        int idCliente;
        int idAsiento;
        double precioFinal;

        public Venta(int id, int idCliente, int idAsiento, double precioFinal) {
            this.id = id;
            this.idCliente = idCliente;
            this.idAsiento = idAsiento;
            this.precioFinal = precioFinal;
        }
    }

    public static class Reserva {
        int id;
        int idCliente;
        int idAsiento;

        public Reserva(int id, int idCliente, int idAsiento) {
            this.id = id;
            this.idCliente = idCliente;
            this.idAsiento = idAsiento;
        }
    }

    private static final int CAPACIDAD_SALA = 50;
    private static final double PRECIO_UNITARIO = 10000;

    private static final Asiento[] asientos = new Asiento[CAPACIDAD_SALA];
    private static final HashMap<Integer, Cliente> clientes = new HashMap<>();
    private static final HashMap<Integer, Venta> ventas = new HashMap<>();
    private static final HashMap<Integer, Reserva> reservas = new HashMap<>();

    private static int contadorClientes = 0;
    private static int contadorVentas = 0;
    private static int contadorReservas = 0;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarAsientos();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> registrarCliente();
                case 2 -> comprarEntrada();
                case 3 -> reservarAsiento();
                case 4 -> imprimirVentas();
                case 5 -> eliminarCliente();
                case 6 -> actualizarCliente();
                case 7 -> eliminarVenta();
                case 8 -> actualizarVenta();
                case 9 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 9);
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú de Venta ---");
        System.out.println("1. Registrar Cliente");
        System.out.println("2. Comprar Entrada");
        System.out.println("3. Reservar Asiento");
        System.out.println("4. Imprimir Ventas");
        System.out.println("5. Eliminar Cliente");
        System.out.println("6. Actualizar Cliente");
        System.out.println("7. Eliminar Venta");
        System.out.println("8. Actualizar Venta");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerEntero() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer
        return valor;
    }

    private static void inicializarAsientos() {
        for (int i = 0; i < CAPACIDAD_SALA; i++) {
            asientos[i] = new Asiento(i + 1);
        }
    }

    private static void registrarCliente() {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        String tipo;
        do {
            System.out.print("Ingrese el tipo de cliente (Estudiante, TerceraEdad, Regular): ");
            tipo = scanner.nextLine().trim();
            if (!tipo.equalsIgnoreCase("Estudiante") && !tipo.equalsIgnoreCase("TerceraEdad") && !tipo.equalsIgnoreCase("Regular")) {
                System.out.println("Tipo inválido. Intente nuevamente.");
            }
        } while (!tipo.equalsIgnoreCase("Estudiante") && !tipo.equalsIgnoreCase("TerceraEdad") && !tipo.equalsIgnoreCase("Regular"));

        contadorClientes++;
        clientes.put(contadorClientes, new Cliente(contadorClientes, nombre, tipo));
        System.out.println("Cliente registrado exitosamente con ID: " + contadorClientes);
    }

    private static Cliente obtenerClientePorID(int idCliente) {
        return clientes.get(idCliente);
    }

    private static void comprarEntrada() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados. Por favor registre un cliente primero.");
            return;
        }
        System.out.print("Ingrese el ID del cliente: ");
        int idCliente = leerEntero();
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Seleccione el número de asiento (1-" + CAPACIDAD_SALA + "): ");
        int asientoId = leerEntero();
        if (asientoId < 1 || asientoId > CAPACIDAD_SALA) {
            System.out.println("Número de asiento fuera de rango.");
            return;
        }
        Asiento asiento = asientos[asientoId - 1];
        if (!asiento.disponible) {
            System.out.println("Asiento no disponible.");
            return;
        }

        double precioFinal = PRECIO_UNITARIO;
        if (cliente.tipo.equalsIgnoreCase("Estudiante")) {
            precioFinal *= 0.90; // 10% descuento
        } else if (cliente.tipo.equalsIgnoreCase("TerceraEdad")) {
            precioFinal *= 0.85; // 15% descuento
        }

        contadorVentas++;
        ventas.put(contadorVentas, new Venta(contadorVentas, idCliente, asientoId, precioFinal));
        asiento.disponible = false;
        System.out.printf("Compra realizada exitosamente.\nCliente: %s\nAsiento: %d\nPrecio final: $%.2f\n",
                cliente.nombre, asientoId, precioFinal);
    }

    private static void reservarAsiento() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados. Por favor registre un cliente primero.");
            return;
        }
        System.out.print("Ingrese el ID del cliente para reservar: ");
        int idCliente = leerEntero();
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Seleccione el número de asiento para reservar (1-" + CAPACIDAD_SALA + "): ");
        int asientoId = leerEntero();
        if (asientoId < 1 || asientoId > CAPACIDAD_SALA) {
            System.out.println("Número de asiento fuera de rango.");
            return;
        }
        Asiento asiento = asientos[asientoId - 1];
        if (!asiento.disponible) {
            System.out.println("Asiento ya está ocupado o reservado.");
            return;
        }

        contadorReservas++;
        reservas.put(contadorReservas, new Reserva(contadorReservas, idCliente, asientoId));
        asiento.disponible = false;
        System.out.println("Reserva realizada para asiento " + asientoId);
    }

    private static void imprimirVentas() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.println("\nLista de ventas:");
        for (Venta venta : ventas.values()) {
            Cliente cliente = obtenerClientePorID(venta.idCliente);
            System.out.printf("Venta ID: %d, Cliente: %s, Asiento: %d, Precio: $%.2f\n",
                    venta.id, cliente != null ? cliente.nombre : "Desconocido", venta.idAsiento, venta.precioFinal);
        }
    }

    private static void eliminarCliente() {
        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int idCliente = leerEntero();
        if (!clientes.containsKey(idCliente)) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        ventas.values().removeIf(venta -> {
            if (venta.idCliente == idCliente) {
                asientos[venta.idAsiento - 1].disponible = true;
                return true;
            }
            return false;
        });
        reservas.values().removeIf(reserva -> {
            if (reserva.idCliente == idCliente) {
                asientos[reserva.idAsiento - 1].disponible = true;
                return true;
            }
            return false;
        });
        clientes.remove(idCliente);
        System.out.println("Cliente eliminado junto con sus ventas y reservas.");
    }

    private static void actualizarCliente() {
        System.out.print("Ingrese el ID del cliente a actualizar: ");
        int idCliente = leerEntero();
        Cliente cliente = obtenerClientePorID(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("Ingrese el nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();
        if (nuevoNombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        String nuevoTipo;
        do {
            System.out.print("Ingrese el nuevo tipo (Estudiante, TerceraEdad, Regular): ");
            nuevoTipo = scanner.nextLine().trim();
            if (!nuevoTipo.equalsIgnoreCase("Estudiante") && !nuevoTipo.equalsIgnoreCase("TerceraEdad") && !nuevoTipo.equalsIgnoreCase("Regular")) {
                System.out.println("Tipo inválido. Intente nuevamente.");
            }
        } while (!nuevoTipo.equalsIgnoreCase("Estudiante") && !nuevoTipo.equalsIgnoreCase("TerceraEdad") && !nuevoTipo.equalsIgnoreCase("Regular"));
        cliente.nombre = nuevoNombre;
        cliente.tipo = nuevoTipo;
        System.out.println("Cliente actualizado correctamente.");
    }

    private static void eliminarVenta() {
        System.out.print("Ingrese el ID de la venta a eliminar: ");
        int idVenta = leerEntero();
        Venta venta = ventas.get(idVenta);
        if (venta == null) {
            System.out.println("Venta no encontrada.");
            return;
        }
        asientos[venta.idAsiento - 1].disponible = true;
        ventas.remove(idVenta);
        System.out.println("Venta eliminada y asiento liberado.");
    }

    private static void actualizarVenta() {
        System.out.print("Ingrese el ID de la venta a actualizar: ");
        int idVenta = leerEntero();
        Venta venta = ventas.get(idVenta);
        if (venta == null) {
            System.out.println("Venta no encontrada.");
            return;
        }
        System.out.print("Ingrese el nuevo número de asiento: ");
        int nuevoAsiento = leerEntero();
        if (nuevoAsiento < 1 || nuevoAsiento > CAPACIDAD_SALA) {
            System.out.println("Número de asiento fuera de rango.");
            return;
        }
        Asiento asientoNuevo = asientos[nuevoAsiento - 1];
        if (!asientoNuevo.disponible) {
            System.out.println("Asiento no disponible.");
            return;
        }
        asientos[venta.idAsiento - 1].disponible = true;
        venta.idAsiento = nuevoAsiento;
        asientoNuevo.disponible = false;
        System.out.println("Venta actualizada con nuevo asiento.");
    }
}
