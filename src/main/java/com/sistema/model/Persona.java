package com.sistema.model;

// Clase abstracta base para Cliente y Empleado
public abstract class Persona {

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
    public String toString() {
        return nombre + " (DPI: " + dpi + ")";
    }
}

// Modulo desarrollado por Persona 1
