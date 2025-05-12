/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marcelo
 */


import java.util.*;

class Cliente {
    private String nombre;
    private int edad;
    private String genero;
    private String tipo;

    public Cliente(String nombre, int edad, String genero, String tipo) {
        this.nombre = nombre;
        this.edad = edad;
        this.genero = genero.toLowerCase();
        this.tipo = tipo.toLowerCase();
    }

    public boolean validarEdad() {
        switch (tipo) {
            case "niño":
                return edad < 12;
            case "tercera edad":
                return edad >= 60;
            case "estudiante":
                return edad >= 12 && edad < 60;
            case "mujer":
                return genero.equals("f") || genero.equals("femenino"); 
            case "adulto":
                return edad >= 12 && edad < 60;
            default:
                return false;
        }
    }

    public double calcularDescuento(int precioBase) {
        switch (tipo) {
            case "niño":
                return precioBase * 0.10;
            case "mujer":
                return precioBase * 0.20;
            case "estudiante":
                return precioBase * 0.15;
            case "tercera edad":
                return precioBase * 0.25;
            default:
                return 0;
        }
    }

    public String getNombre() {
        return nombre;
    }
    public int getEdad() {
        return edad;
    }
    public String getTipo() {
        return tipo;
    }
}

class Asiento {
    private final String tipo;
    private boolean disponible;

    public Asiento(String tipo) {
        this.tipo = tipo;
        this.disponible = true;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void reservar() {
        disponible = false;
    }

    public void liberar() {
        disponible = true;
    }
}

class Boleta {
    private static int contadorId = 1;
    private int id;
    private Cliente cliente;
    private Asiento asiento;
    int precioFinal;
    private boolean pagado;

    public Boleta(Cliente cliente, Asiento asiento, int precioFinal) {
        this.id = contadorId++;
        this.cliente = cliente;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.pagado = false;
    }

    public int getId() { return id; }
    public Asiento getAsiento() { return asiento; }
    public void setAsiento(Asiento asiento) { this.asiento = asiento; }
    public void setPrecioFinal(int precioFinal) { this.precioFinal = precioFinal; }
    public Cliente getCliente() { return cliente; }
    public boolean isPagado() { return pagado; }
    public void pagar() { pagado = true; }

    public void imprimirBoleta() {
        System.out.println("\n========== BOLETA DE VENTA ==========");
        System.out.println("ID Venta: " + id);
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Edad: " + cliente.getEdad());
        System.out.println("Tipo Cliente: " + cliente.getTipo());
        System.out.println("Asiento asignado: " + asiento.getTipo());
        System.out.printf("Precio final: %d CLP\n", precioFinal);
        System.out.println("Estado de pago: " + (pagado ? "Pagado" : "Pendiente"));
        System.out.println("=====================================");
    }
}

public class TeatroMoro9 {
    private Map<String, List<Asiento>> asientosPorTipo;
    private Map<Integer, Boleta> ventas;
    private final int precioBase = 10000; // Precio base CLP
    private Scanner scanner;

    public TeatroMoro9() {
        asientosPorTipo = new LinkedHashMap<>();
        ventas = new HashMap<>();
        scanner = new Scanner(System.in);
        inicializarAsientos();
    }

    private void inicializarAsientos() {
        String[] tipos = {"VIP", "Palco", "Platea Baja", "Platea Alta", "Galería"};
        for (String tipo : tipos) {
            List<Asiento> listAsientos = new ArrayList<>();
            for (int i = 0; i < 5; i++) { 
                listAsientos.add(new Asiento(tipo));
            }
            asientosPorTipo.put(tipo.toLowerCase(), listAsientos);
        }
    }

    private void mostrarAsientosDisponibles() {
        System.out.println("\nAsientos disponibles:");
        asientosPorTipo.forEach((tipo, lista) -> {
            long count = lista.stream().filter(Asiento::isDisponible).count();
            System.out.printf("- %s: %d disponibles\n", capitalizar(tipo), count);
        });
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0,1).toUpperCase() + texto.substring(1);
    }

    private Asiento seleccionarAsiento(String tipo) {
        List<Asiento> lista = asientosPorTipo.get(tipo.toLowerCase());
        if (lista == null) {
            return null;
        }
        for (Asiento asiento : lista) {
            if (asiento.isDisponible()) {
                return asiento;
            }
        }
        return null;
    }

    public Boleta procesarVenta(Cliente cliente, Asiento asiento) {
        int descuento = 0;
        if (!cliente.validarEdad()) {
            System.out.println("ERROR: La edad del cliente no corresponde con el tipo seleccionado, no se aplicará descuento.");
        } else {
            descuento = (int) cliente.calcularDescuento(precioBase);
        }
        int precioFinal = precioBase - descuento;
        asiento.reservar();
        Boleta boleta = new Boleta(cliente, asiento, precioFinal);
        ventas.put(boleta.getId(), boleta);
        return boleta;
    }

    private void crearVenta() {
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();

        int edad = -1;
        while (edad < 0 || edad > 120) {
            System.out.print("Ingrese su edad (0-120): ");
            try {
                edad = Integer.parseInt(scanner.nextLine());
                if (edad < 0 || edad > 120) System.out.println("Edad inválida. Intente nuevamente.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Introduzca un número entero.");
            }
        }

        System.out.print("Ingrese su género (M/F): ");
        String genero = scanner.nextLine().toLowerCase();

        String tipoCliente = "";
        while (true) {
            System.out.print("Ingrese tipo de cliente (niño, mujer, estudiante, tercera edad, adulto): ");
            tipoCliente = scanner.nextLine().toLowerCase();
            if (tipoCliente.equals("niño") && edad >= 12) {
                System.out.println("Error: la edad no corresponde a niño, intente de nuevo.");
            } else if (tipoCliente.equals("tercera edad") && edad < 60) {
                System.out.println("Error: la edad no corresponde a tercera edad, intente de nuevo.");
            } else if (tipoCliente.equals("mujer") && !(genero.equals("f") || genero.equals("femenino"))) {
                System.out.println("Error: género no corresponde a mujer, intente de nuevo.");
            } else if (tipoCliente.equals("estudiante") && (edad <12 || edad >=60)) {
                System.out.println("Error: la edad no corresponde a estudiante, intente de nuevo.");
            } else if (tipoCliente.equals("adulto") && (edad <12 || edad >=60)) {
                System.out.println("Error: la edad no corresponde a adulto, intente de nuevo.");
            } else if (tipoCliente.equals("niño") || tipoCliente.equals("mujer") || tipoCliente.equals("estudiante") || tipoCliente.equals("tercera edad") || tipoCliente.equals("adulto")) {
                break;
            } else {
                System.out.println("Tipo inválido, intente de nuevo.");
            }
        }

        int cantidadEntradas = 0;
        while (true) {
            System.out.print("Ingrese cantidad de entradas a comprar: ");
            try {
                cantidadEntradas = Integer.parseInt(scanner.nextLine());
                if (cantidadEntradas < 1) {
                    System.out.println("Debe comprar al menos una entrada.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Introduzca un número entero.");
            }
        }

        for (int i = 0; i < cantidadEntradas; i++) {
            mostrarAsientosDisponibles();
            String asientoTipo;
            Asiento asientoSeleccionado;
            while (true) {
                System.out.printf("Seleccione tipo de asiento para entrada %d: ", i + 1);
                asientoTipo = scanner.nextLine();
                asientoSeleccionado = seleccionarAsiento(asientoTipo);
                if (asientoSeleccionado == null) {
                    System.out.println("Tipo de asiento inválido o no disponible. Intente nuevamente.");
                } else {
                    break;
                }
            }

            Cliente cliente = new Cliente(nombre, edad, genero, tipoCliente);
            Boleta boleta = procesarVenta(cliente, asientoSeleccionado);
            System.out.println("Entrada asignada correctamente.");
            boleta.imprimirBoleta();
        }
    }

    private void mostrarVentas() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        ventas.values().forEach(Boleta::imprimirBoleta);
    }

    private void actualizarVenta() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas para actualizar.");
            return;
        }
        int idVenta;
        while (true) {
            System.out.print("Ingrese ID de venta a actualizar: ");
            try {
                idVenta = Integer.parseInt(scanner.nextLine());
                if (!ventas.containsKey(idVenta)) {
                    System.out.println("ID no encontrado, intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }

        String nuevoTipo;
        Asiento nuevoAsiento;
        while (true) {
            System.out.print("Ingrese nuevo tipo de asiento: ");
            nuevoTipo = scanner.nextLine();
            nuevoAsiento = seleccionarAsiento(nuevoTipo);
            if (nuevoAsiento == null) {
                System.out.println("Asiento no disponible o tipo incorrecto, intente de nuevo.");
            } else {
                break;
            }
        }

        Boleta boleta = ventas.get(idVenta);
        Asiento anterior = boleta.getAsiento();
        anterior.liberar();
        nuevoAsiento.reservar();
        boleta.setAsiento(nuevoAsiento);

        Cliente cliente = boleta.getCliente();
        int descuento = cliente.validarEdad() ? (int) cliente.calcularDescuento(precioBase) : 0;
        boleta.setPrecioFinal(precioBase - descuento);
        System.out.println("Venta actualizada satisfactoriamente.");
        boleta.imprimirBoleta();
    }

    private void eliminarVenta() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas para eliminar.");
            return;
        }
        int idVenta;
        while (true) {
            System.out.print("Ingrese ID de venta a eliminar: ");
            try {
                idVenta = Integer.parseInt(scanner.nextLine());
                if (!ventas.containsKey(idVenta)) {
                    System.out.println("ID no encontrado, intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }

        Boleta boleta = ventas.remove(idVenta);
        boleta.getAsiento().liberar();
        System.out.println("Venta eliminada correctamente.");
    }

    private void pagarVenta() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas para pagar.");
            return;
        }
        int idVenta;
        while (true) {
            System.out.print("Ingrese ID de venta para pagar: ");
            try {
                idVenta = Integer.parseInt(scanner.nextLine());
                if (!ventas.containsKey(idVenta)) {
                    System.out.println("ID no encontrado, intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }

        Boleta boleta = ventas.get(idVenta);
        if (boleta.isPagado()) {
            System.out.println("Esta venta ya está pagada.");
            boleta.imprimirBoleta();
            return;
        }

        System.out.printf("El total a pagar es: %d CLP\n", boleta.precioFinal);
        System.out.print("Ingrese 'PAGAR' para confirmar el pago o cualquier otra cosa para cancelar: ");
        String confirmacion = scanner.nextLine();
        if (confirmacion.equalsIgnoreCase("PAGAR")) {
            boleta.pagar();
            System.out.println("Pago realizado exitosamente.");
            boleta.imprimirBoleta();
        } else {
            System.out.println("Pago cancelado.");
        }
    }

    private void ejecutarPruebas() {
        System.out.println("\nEjecutando pruebas...");

        Cliente c1 = new Cliente("Ana", 10, "f", "niño");
        assert c1.validarEdad() : "Error en validación edad niño";
        assert Math.abs(c1.calcularDescuento(precioBase) - (precioBase * 0.10)) < 0.01 : "Error en descuento niño";

        Asiento a1 = seleccionarAsiento("VIP");
        assert a1 != null && a1.isDisponible() : "Error en selección asiento VIP";
        a1.reservar();
        assert !a1.isDisponible() : "Error en reserva asiento";

        Boleta b1 = procesarVenta(new Cliente("Luis", 65, "m", "tercera edad"), seleccionarAsiento("Palco"));
        assert ventas.containsKey(b1.getId()) : "Error en creación de venta";
        assert !b1.getAsiento().isDisponible() : "Error en marcado de asiento reservado";

        actualizarVentaPrueba(b1.getId(), "Galería");
        Boleta bActualizada = ventas.get(b1.getId());
        assert bActualizada.getAsiento().getTipo().equalsIgnoreCase("Galería") : "Error en actualización de asiento";

        eliminarVentaPrueba(b1.getId());
        assert !ventas.containsKey(b1.getId()) : "Error en eliminación de venta";

        System.out.println("Todas las pruebas pasaron.");
    }

    private void actualizarVentaPrueba(int idVenta, String nuevoTipo) {
        Boleta boleta = ventas.get(idVenta);
        if (boleta == null) return;
        Asiento nuevoAsiento = seleccionarAsiento(nuevoTipo);
        if (nuevoAsiento == null) return;

        boleta.getAsiento().liberar();
        nuevoAsiento.reservar();
        boleta.setAsiento(nuevoAsiento);

        Cliente cliente = boleta.getCliente();
        int descuento = cliente.validarEdad() ? (int) cliente.calcularDescuento(precioBase) : 0;
        boleta.setPrecioFinal(precioBase - descuento);
    }

    private void eliminarVentaPrueba(int idVenta) {
        Boleta boleta = ventas.remove(idVenta);
        if(boleta != null) {
            boleta.getAsiento().liberar();
        }
    }

    public void menu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENÚ Teatro Moro =====");
            System.out.println("1) Nueva venta");
            System.out.println("2) Mostrar ventas");
            System.out.println("3) Actualizar venta");
            System.out.println("4) Eliminar venta");
            System.out.println("5) Mostrar asientos disponibles");
            System.out.println("6) Pagar venta");
            System.out.println("7) Ejecutar pruebas automáticas");
            System.out.println("8) Salir");
            System.out.print("Seleccione opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": crearVenta(); break;
                case "2": mostrarVentas(); break;
                case "3": actualizarVenta(); break;
                case "4": eliminarVenta(); break;
                case "5": mostrarAsientosDisponibles(); break;
                case "6": pagarVenta(); break;
                case "7": ejecutarPruebas(); break;
                case "8": salir = true; System.out.println("Gracias por usar el sistema. ¡Hasta pronto!"); break;
                default: System.out.println("Opción inválida. Intente de nuevo."); break;
            }
        }
    }

    public static void main(String[] args) {
        TeatroMoro9 sistema = new TeatroMoro9();
        sistema.menu();
    }
}
