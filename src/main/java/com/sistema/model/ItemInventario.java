package com.sistema.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un producto específico y su stock en inventario.
 */
public class ItemInventario implements Serializable {
    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int stock;

    public ItemInventario() {
    }

    public ItemInventario(Producto producto, int stock) {
        this.producto = producto;
        this.stock = stock;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

    public boolean restarStock(int cantidad) {
        if (cantidad <= this.stock) {
            this.stock -= cantidad;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemInventario that)) return false;
        return Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto);
    }

    @Override
    public String toString() {
        return producto.getNombre() + " (Stock: " + stock + ")";
    }
}
