package com.sistema.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad Orden de Compra.
 * Debe permitir registrar al menos un producto, calcular total
 * y gestionar su estado (PENDIENTE, COMPLETADA, CANCELADA).
 */
public class OrdenCompra implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idOrden;
    private Cliente cliente;
    private String idBodega; // Bodega de donde sale el stock
    private List<DetalleOrden> items;
    private String fecha;
    private double total;
    private EstadoOrden estado;

    public OrdenCompra() {
        this.items = new ArrayList<>();
        this.estado = EstadoOrden.PENDIENTE;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public OrdenCompra(String idOrden, Cliente cliente, String idBodega) {
        this.idOrden = idOrden;
        this.cliente = cliente;
        this.idBodega = idBodega;
        this.items = new ArrayList<>();
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.estado = EstadoOrden.PENDIENTE;
        this.total = 0.0;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(String idBodega) {
        this.idBodega = idBodega;
    }

    public List<DetalleOrden> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<DetalleOrden> items) {
        this.items = items;
        calcularTotal();
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        for (DetalleOrden detalle : getItems()) {
            if (detalle.getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                calcularTotal();
                return;
            }
        }
        getItems().add(new DetalleOrden(producto, cantidad));
        calcularTotal();
    }

    public void eliminarDetalle(String codigoProducto) {
        getItems().removeIf(d -> d.getProducto().getCodigo().equalsIgnoreCase(codigoProducto));
        calcularTotal();
    }

    public void calcularTotal() {
        double acum = 0.0;
        for (DetalleOrden item : getItems()) {
            acum += item.getSubtotal();
        }
        this.total = acum;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenCompra that)) return false;
        return Objects.equals(idOrden, that.idOrden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrden);
    }

    @Override
    public String toString() {
        return "Orden #" + idOrden + " - " + cliente.getNombre() + " (" + estado + ") Total: Q" + String.format("%.2f", total);
    }
}
