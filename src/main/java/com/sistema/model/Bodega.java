package com.sistema.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad Bodega.
 */
public class Bodega implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idBodega;
    private Empleado empleado; // Empleado encargado
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bodega bodega)) return false;
        return Objects.equals(idBodega, bodega.idBodega);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBodega);
    }

    @Override
    public String toString() {
        return idBodega + " (" + direccion + ", Cap: " + capacidad + ")";
    }
}
