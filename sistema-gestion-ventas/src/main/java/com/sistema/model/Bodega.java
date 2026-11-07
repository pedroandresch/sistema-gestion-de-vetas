package com.sistema.model;

public class Bodega {

    private String idBodega;
    private Empleado empleado; // Empleado encargado de la bodega
    private int capacidad;
    private String direccion;

    public Bodega() {
    }

    public Bodega(String idBodega, Empleado empleado, int capacidad, String direccion) {
        this.idBodega = idBodega;
        this.empleado = empleado;
        this.capacidad = capacidad;
        this.direccion = direccion;
    }

    public String getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return idBodega + " (" + direccion + ", Cap: " + capacidad + ")";
    }
}
