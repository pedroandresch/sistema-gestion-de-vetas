package com.sistema.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Inventario de una bodega específica.
 * Contiene la lista de productos y la cantidad por producto (stock).
 */
public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;

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

    /**
     * Registra o actualiza el stock de un producto en este inventario.
     */
    public void registrarOActualizarStock(Producto producto, int cantidad) {
        for (ItemInventario item : getItems()) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                item.agregarStock(cantidad);
                return;
            }
        }
        getItems().add(new ItemInventario(producto, cantidad));
    }

    /**
     * Obtiene el stock disponible de un producto.
     */
    public int obtenerStock(String codigoProducto) {
        for (ItemInventario item : getItems()) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                return item.getStock();
            }
        }
        return 0;
    }

    /**
     * Descuenta stock de un producto si hay suficiente existencia.
     */
    public boolean descontarStock(String codigoProducto, int cantidad) {
        for (ItemInventario item : getItems()) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                return item.restarStock(cantidad);
            }
        }
        return false;
    }

    /**
     * Devuelve la cantidad total de unidades almacenadas en la bodega.
     */
    public int getTotalUnidadesAlmacenadas() {
        int total = 0;
        for (ItemInventario item : getItems()) {
            total += item.getStock();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventario that)) return false;
        return Objects.equals(idBodega, that.idBodega);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBodega);
    }
}
