import java.util.ArrayList;
import java.util.Scanner;

class Boleto {
    private final int numero;
    private final String ubicacion;
    private final double precioFinal;
    private final String tipo;

    public Boleto(int numero, String ubicacion, double precioFinal, String tipo) {
        this.numero = numero;
        this.ubicacion = ubicacion;
        this.precioFinal = precioFinal;
        this.tipo = tipo;
    }

    public int getNumero() {
        return numero;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public String getTipo() {
        return tipo;
    }
}

public class SistemaVentaBoletos {
    private static final double PRECIO_VIP = 100.0;
    private static final double PRECIO_PALCOS = 75.0;
    private static final double PRECIO_GENERAL = 50.0;
    private static int contadorBoletos = 1;
    private static double ingresosTotales = 0.0;

    private final ArrayList<Boleto> boletosVendidos = new ArrayList<>();
    private final String nombreTeatro = "Teatro Moro";
    private final int capacidadTeatro = 100;
    private int boletosDisponibles = capacidadTeatro;

    public static void main(String[] args) {
        SistemaVentaBoletos sistema = new SistemaVentaBoletos();
        sistema.mostrarMenu();
    }

    public void mostrarMenu() {
        try (Scanner entrada = new Scanner(System.in)) {
            int opcion;
            
            do {
                System.out.println("\n--- Sistema de Venta - " + nombreTeatro + " ---");
                System.out.println("1. Comprar ");
                System.out.println("2. Ver Promociones");
                System.out.println("3. Buscar Boleto");
                System.out.println("4. Eliminar Boleto");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                
                while (!entrada.hasNextInt()) {
                    System.out.print("Ingrese un número válido: ");
                    entrada.next();
                }
                opcion = entrada.nextInt();
                entrada.nextLine(); // Limpiar salto de línea
                
                switch (opcion) {
                    case 1 -> venderBoleto(entrada);
                    case 2 -> mostrarPromociones();
                    case 3 -> buscarBoleto(entrada);
                    case 4 -> eliminarBoleto(entrada);
                    case 5 -> System.out.println("Saliendo... ¡Gracias por usar el sistema!");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } while (opcion != 5);
        }
    }

    private void venderBoleto(Scanner entrada) {
        if (boletosDisponibles <= 0) {
            System.out.println("No hay boletos disponibles.");
            return;
        }

        System.out.print("Ingrese la ubicación del boleto (VIP, Palcos, General): ");
        String ubicacion = entrada.nextLine().trim().toUpperCase();
        double precioBase = 0.0;

        switch (ubicacion) {
            case "VIP" -> precioBase = PRECIO_VIP;
            case "PALCOS" -> precioBase = PRECIO_PALCOS;
            case "GENERAL" -> precioBase = PRECIO_GENERAL;
            default -> {
                System.out.println("Ubicación inválida.");
                return;
            }
        }

        System.out.print("¿Es estudiante? (sí/no): ");
        String esEstudiante = entrada.nextLine().trim().toLowerCase();
        System.out.print("¿Es adulto mayor? (sí/no): ");
        String esMayor = entrada.nextLine().trim().toLowerCase();

        double descuento = 0.0;
        String tipo = "Regular";

        if (esEstudiante.equals("sí") || esEstudiante.equals("si")) {
            descuento = 0.10;
            tipo = "Estudiante";
        } else if (esMayor.equals("sí") || esMayor.equals("si")) {
            descuento = 0.15;
            tipo = "Adulto Mayor";
        }

        double precioFinal = precioBase * (1 - descuento);
        ingresosTotales += precioFinal;
        boletosDisponibles--;

        Boleto boleto = new Boleto(contadorBoletos++, ubicacion, precioFinal, tipo);
        boletosVendidos.add(boleto);

        System.out.printf("¡Boleto vendido! Precio final: $%.2f | Número de Boleto: %d\n", precioFinal, boleto.getNumero());
    }

    private void mostrarPromociones() {
        System.out.println("\n--- Promociones Actuales ---");
        System.out.println("1. 10% de descuento para estudiantes.");
        System.out.println("2. 15% de descuento para adultos mayores.");
        System.out.println("3. Compra 5 boletos, ¡y recibe 1 gratis! (no implementado todavía).");
    }

    private void buscarBoleto(Scanner entrada) {
        System.out.print("Ingrese el número de boleto a buscar: ");
        while (!entrada.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            entrada.next();
        }
        int numero = entrada.nextInt();
        entrada.nextLine();

        boolean encontrado = false;

        for (Boleto boleto : boletosVendidos) {
            if (boleto.getNumero() == numero) {
                System.out.printf("Boleto Encontrado: #%d | Ubicación: %s | Precio: $%.2f | Tipo: %s\n",
                        boleto.getNumero(), boleto.getUbicacion(), boleto.getPrecioFinal(), boleto.getTipo());
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Boleto no encontrado.");
        }
    }

    private void eliminarBoleto(Scanner entrada) {
        System.out.print("Ingrese el número de boleto a eliminar: ");
        while (!entrada.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            entrada.next();
        }
        int numero = entrada.nextInt();
        entrada.nextLine();

        for (int i = 0; i < boletosVendidos.size(); i++) {
            if (boletosVendidos.get(i).getNumero() == numero) {
                boletosVendidos.remove(i);
                boletosDisponibles++;
                System.out.println("Boleto eliminado correctamente.");
                return;
            }
        }

        System.out.println("Boleto no encontrado.");
    }
}
