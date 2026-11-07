package com.sistema.model;

public class ItemInventario {

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
    public String toString() {
        return producto.getNombre() + " (Stock: " + stock + ")";
    }
}
