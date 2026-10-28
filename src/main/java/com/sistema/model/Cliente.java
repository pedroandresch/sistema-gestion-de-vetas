package com.sistema.model;

/**
 * Entidad Cliente. Hereda de Persona.
 */
public class Cliente extends Persona {
    private String fechaNacimiento;
    private TipoCliente tipo;

    public Cliente() {
        super();
    }

    public Cliente(String dpi, String nombre, String direccion, String fechaNacimiento, TipoCliente tipo) {
        super(dpi, nombre, direccion);
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return getNombre() + " - " + tipo + " (DPI: " + getDpi() + ")";
    }
}
