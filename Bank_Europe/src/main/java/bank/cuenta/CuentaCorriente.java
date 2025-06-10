/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank.cuenta;

public class CuentaCorriente extends CuentaBancaria {
    private static final double COMISION_MENSUAL = 5000;
    private double tasaInteres;

    public CuentaCorriente(String numeroCuenta, double saldo, double tasaInteres) {
        super(numeroCuenta, saldo);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public double calcularInteres() {
        // Interés simple menos comisión mensual
        return (getSaldo() * tasaInteres) - COMISION_MENSUAL;
    }

    public void aplicarComisionMensual() {
        girar(COMISION_MENSUAL);
    }

    @Override
    public String toString() {
        return super.toString() + " (Corriente)";
    }

    public void retirar(double monto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}