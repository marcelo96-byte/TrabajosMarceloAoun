package bank.cuenta;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marce
 */


import bank.cliente.InfoCliente;  
public class CuentaDigital extends CuentaBancaria implements InfoCliente {
    private double tasaInteres;
    private String nombreCliente;
    private String rutCliente;

    public CuentaDigital(String numeroCuenta, double saldo, double tasaInteres, 
                        String nombreCliente, String rutCliente) {
        super(numeroCuenta, saldo);
        this.tasaInteres = tasaInteres;
        this.nombreCliente = nombreCliente;
        this.rutCliente = rutCliente;
    }

    @Override
    public double calcularInteres() {
        // Tasa de interés reducida para cuentas digitales
        return getSaldo() * (tasaInteres * 0.75);
    }

    @Override
    public void mostrarInformacionCliente() {
        System.out.println("=== Información del Cliente ===");
        System.out.println("Nombre: " + nombreCliente);
        System.out.println("RUT: " + rutCliente);
        System.out.println("Cuenta Digital: " + getNumeroCuenta());
        System.out.println("Saldo Actual: $" + String.format("%,.2f", getSaldo()));
    }

    @Override
    public String getNombreCliente() {
        return nombreCliente;
    }

    @Override
    public String getRutCliente() {
        return rutCliente;
    }

    @Override
    public String toString() {
        return super.toString() + " (Digital - " + nombreCliente + ")";
    }

    public void retirar(double monto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}