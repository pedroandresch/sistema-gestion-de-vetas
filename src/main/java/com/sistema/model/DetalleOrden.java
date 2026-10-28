package com.sistema.model;

import java.io.Serializable;

/**
 * Detalle o renglón de una orden de compra (Producto + Cantidad + Subtotal).
 */
public class DetalleOrden implements Serializable {
    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleOrden() {
    }

    public DetalleOrden(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        this.subtotal = this.precioUnitario * this.cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.precioUnitario = producto.getPrecio();
            calcularSubtotal();
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    private void calcularSubtotal() {
        this.subtotal = this.precioUnitario * this.cantidad;
    }

    @Override
    public String toString() {
        return producto.getNombre() + " x" + cantidad + " (Subtotal: Q" + String.format("%.2f", subtotal) + ")";
    }
}
