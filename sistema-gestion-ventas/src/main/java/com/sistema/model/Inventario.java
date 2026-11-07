package com.sistema.model;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private String idBodega;
    private List<ItemInventario> items;

    public Inventario() {
        this.items = new ArrayList<>();
    }

    public Inventario(String idBodega) {
        this.idBodega = idBodega;
        this.items = new ArrayList<>();
    }

    public String getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }

    public List<ItemInventario> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<ItemInventario> items) {
        this.items = items;
    }

    // Si el producto ya existe suma al stock, si no lo agrega a la lista
    public void registrarOActualizarStock(Producto producto, int cantidad) {
        for (ItemInventario item : getItems()) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                item.agregarStock(cantidad);
                return;
            }
        }
        getItems().add(new ItemInventario(producto, cantidad));
    }

    public int obtenerStock(String codigoProducto) {
        for (ItemInventario item : getItems()) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                return item.getStock();
            }
        }
        return 0;
    }

    public boolean descontarStock(String codigoProducto, int cantidad) {
        for (ItemInventario item : getItems()) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                return item.restarStock(cantidad);
            }
        }
        return false;
    }

    // Suma todas las unidades almacenadas en esta bodega
    public int getTotalUnidadesAlmacenadas() {
        int total = 0;
        for (ItemInventario item : getItems()) {
            total += item.getStock();
        }
        return total;
    }
}
