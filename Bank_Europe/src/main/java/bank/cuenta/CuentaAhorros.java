/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank.cuenta;

public class CuentaAhorros extends CuentaBancaria {
    private double tasaInteres;
    private int mesesContrato;

    public CuentaAhorros(String numeroCuenta, double saldo, double tasaInteres, int mesesContrato) {
        super(numeroCuenta, saldo);
        this.tasaInteres = tasaInteres;
        this.mesesContrato = mesesContrato;
    }

    @Override
    public double calcularInteres() {
        // Interés compuesto considerando meses de contrato
        return getSaldo() * Math.pow(1 + tasaInteres, mesesContrato) - getSaldo();
    }

    public void renovarContrato(int mesesAdicionales) {
        this.mesesContrato += mesesAdicionales;
    }

    @Override
    public String toString() {
        return super.toString() + " (Ahorros - " + mesesContrato + " meses)";
    }

    public void retirar(double monto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}