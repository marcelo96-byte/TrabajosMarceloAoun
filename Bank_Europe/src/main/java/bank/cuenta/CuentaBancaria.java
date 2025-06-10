/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank.cuenta;

public abstract class CuentaBancaria {
    private final String numeroCuenta;
    private double saldo;

    public CuentaBancaria(String numeroCuenta, double saldo) {
        if (numeroCuenta == null || numeroCuenta.length() != 9 || !numeroCuenta.matches("\\d+")) {
            throw new IllegalArgumentException("Número de cuenta debe tener exactamente 9 dígitos");
        }
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
    }

    // Getters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    // Operaciones bancarias
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo");
        }
        saldo += monto;
    }

    public void girar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a girar debe ser positivo");
        }
        if (monto > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el giro");
        }
        saldo -= monto;
    }

    // Método abstracto para calcular intereses
    public abstract double calcularInteres();

    @Override
    public String toString() {
        return "Cuenta: " + numeroCuenta + " - Saldo: $" + String.format("%,.2f", saldo);
    }
}