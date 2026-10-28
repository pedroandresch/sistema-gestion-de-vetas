package com.sistema.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Superclase abstracta Persona.
 * Aplica el principio de abstracción y herencia para Cliente y Empleado.
 */
public abstract class Persona implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String dpi;
    protected String nombre;
    protected String direccion;

    public Persona() {
    }

    public Persona(String dpi, String nombre, String direccion) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(o instanceof Persona persona)) return false;
        return Objects.equals(dpi, persona.dpi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dpi);
    }

    @Override
    public String toString() {
        return nombre + " (DPI: " + dpi + ")";
    }
}
