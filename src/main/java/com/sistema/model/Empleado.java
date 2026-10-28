package com.sistema.model;

/**
 * Entidad Empleado. Hereda de Persona.
 */
public class Empleado extends Persona {
    private String idEmpleado;
    private String puesto;
    private double salario;

    public Empleado() {
        super();
    }

    public Empleado(String idEmpleado, String nombre, String dpi, String direccion, String puesto, double salario) {
        super(dpi, nombre, direccion);
        this.idEmpleado = idEmpleado;
        this.puesto = puesto;
        this.salario = salario;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return idEmpleado + " - " + getNombre() + " (" + puesto + ")";
    }
}
