package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompraPanel extends JPanel {

    private final SistemaController controller;

    private JTextField txtIdOrden;
    private JComboBox<Cliente> cmbCliente;
    private JComboBox<String> cmbBodega;

    // Selector para armar los ítems de la orden (al menos 1 producto)
    private JComboBox<Producto> cmbProducto;
    private JTextField txtCantidadItem;
    private DefaultTableModel cartTableModel;
    private JTable tablaCarrito;
    private JLabel lblTotalCalculado;

    private List<DetalleOrden> itemsTemporales;

    // Tabla de órdenes registradas
    private DefaultTableModel ordenesTableModel;
    private JTable tablaOrdenes;

    public OrdenCompraPanel(SistemaController controller) {
        this.controller = controller;
        this.itemsTemporales = new ArrayList<>();
        initComponents();
        recargarCombos();
        recargarTablaOrdenes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel Superior: Formulario de Nueva Orden y Carrito de Productos
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Crear Nueva Orden de Compra"));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        txtIdOrden = new JTextField(8);
        cmbCliente = new JComboBox<>();
        cmbBodega = new JComboBox<>();

        headerPanel.add(new JLabel("No. Orden:"));
        headerPanel.add(txtIdOrden);
        headerPanel.add(new JLabel("Cliente:"));
        headerPanel.add(cmbCliente);
        headerPanel.add(new JLabel("Bodega de Despacho:"));
        headerPanel.add(cmbBodega);

        topPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de selección de productos para la orden
        JPanel itemSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        cmbProducto = new JComboBox<>();
        txtCantidadItem = new JTextField("1", 5);
        JButton btnAgregarItem = new JButton("Agregar a la Orden");
        JButton btnQuitarItem = new JButton("Quitar Seleccionado");

        itemSelectPanel.add(new JLabel("Producto:"));
        itemSelectPanel.add(cmbProducto);
        itemSelectPanel.add(new JLabel("Cantidad:"));
        itemSelectPanel.add(txtCantidadItem);
        itemSelectPanel.add(btnAgregarItem);
        itemSelectPanel.add(btnQuitarItem);

        // Tabla del carrito temporal de la orden actual
        String[] cartCols = {"Código", "Producto", "Precio Unitario (Q)", "Cantidad", "Subtotal (Q)"};
        cartTableModel = new DefaultTableModel(cartCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(cartTableModel);
        tablaCarrito.setRowHeight(22);
        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        scrollCarrito.setPreferredSize(new Dimension(500, 120));

        JPanel cartContainer = new JPanel(new BorderLayout(5, 5));
        cartContainer.add(itemSelectPanel, BorderLayout.NORTH);
        cartContainer.add(scrollCarrito, BorderLayout.CENTER);

        // Barra inferior del formulario (Total y Botones de acción)
        JPanel footerFormPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        lblTotalCalculado = new JLabel("TOTAL: Q0.00");
        lblTotalCalculado.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTotalCalculado.setForeground(new Color(46, 125, 50));

        JButton btnGuardarPendiente = new JButton("Guardar como Pendiente");
        JButton btnCompletarDirecto = new JButton("Completar Venta y Facturar");
        btnCompletarDirecto.setBackground(new Color(46, 125, 50));
        btnCompletarDirecto.setForeground(Color.WHITE);
        btnCompletarDirecto.setFocusPainted(false);

        footerFormPanel.add(lblTotalCalculado);
        footerFormPanel.add(btnGuardarPendiente);
        footerFormPanel.add(btnCompletarDirecto);

        cartContainer.add(footerFormPanel, BorderLayout.SOUTH);
        topPanel.add(cartContainer, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Panel Inferior: Historial de Órdenes Registradas con Acciones
        JPanel historialPanel = new JPanel(new BorderLayout(10, 10));
        historialPanel.setBorder(BorderFactory.createTitledBorder("Historial de Órdenes de Compra"));

        String[] orderCols = {"No. Orden", "Fecha", "Cliente", "DPI", "Bodega", "Items", "Total (Q)", "Estado"};
        ordenesTableModel = new DefaultTableModel(orderCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaOrdenes = new JTable(ordenesTableModel);
        tablaOrdenes.setRowHeight(24);
        tablaOrdenes.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollOrdenes = new JScrollPane(tablaOrdenes);

        JPanel accionesOrdenesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnCompletarSeleccionada = new JButton("Completar Orden Seleccionada (Descuenta Stock)");
        btnCompletarSeleccionada.setBackground(new Color(33, 150, 243));
        btnCompletarSeleccionada.setForeground(Color.WHITE);

        JButton btnCancelarSeleccionada = new JButton("Cancelar Orden Seleccionada");
        btnCancelarSeleccionada.setBackground(new Color(244, 67, 54));
        btnCancelarSeleccionada.setForeground(Color.WHITE);

        accionesOrdenesPanel.add(btnCompletarSeleccionada);
        accionesOrdenesPanel.add(btnCancelarSeleccionada);

        historialPanel.add(scrollOrdenes, BorderLayout.CENTER);
        historialPanel.add(accionesOrdenesPanel, BorderLayout.SOUTH);

        add(historialPanel, BorderLayout.CENTER);

        // Eventos
        btnAgregarItem.addActionListener(e -> agregarItemAlCarrito());
        btnQuitarItem.addActionListener(e -> quitarItemDelCarrito());
        btnGuardarPendiente.addActionListener(e -> registrarOrden(EstadoOrden.PENDIENTE));
        btnCompletarDirecto.addActionListener(e -> registrarOrden(EstadoOrden.COMPLETADA));
        btnCompletarSeleccionada.addActionListener(e -> completarOrdenSeleccionada());
        btnCancelarSeleccionada.addActionListener(e -> cancelarOrdenSeleccionada());
    }

    public void recargarCombos() {
        cmbCliente.removeAllItems();
        for (Cliente c : controller.getClientes()) {
            cmbCliente.addItem(c);
        }

        cmbBodega.removeAllItems();
        for (Bodega b : controller.getBodegas()) {
            cmbBodega.addItem(b.getIdBodega());
        }

        cmbProducto.removeAllItems();
        for (Producto p : controller.getProductos()) {
            cmbProducto.addItem(p);
        }
    }

    private void agregarItemAlCarrito() {
        Producto prod = (Producto) cmbProducto.getSelectedItem();
        String cantStr = txtCantidadItem.getText().trim();

        if (prod == null || cantStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto e ingrese la cantidad.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Si ya existe en la orden actual, sumar
            boolean existe = false;
            for (DetalleOrden d : itemsTemporales) {
                if (d.getProducto().getCodigo().equalsIgnoreCase(prod.getCodigo())) {
                    d.setCantidad(d.getCantidad() + cantidad);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                itemsTemporales.add(new DetalleOrden(prod, cantidad));
            }

            txtCantidadItem.setText("1");
            actualizarTablaCarrito();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarItemDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila >= 0 && fila < itemsTemporales.size()) {
            itemsTemporales.remove(fila);
            actualizarTablaCarrito();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del carrito para quitar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTablaCarrito() {
        cartTableModel.setRowCount(0);
        double total = 0.0;
        for (DetalleOrden d : itemsTemporales) {
            total += d.getSubtotal();
            cartTableModel.addRow(new Object[]{
                    d.getProducto().getCodigo(),
                    d.getProducto().getNombre(),
                    String.format("%.2f", d.getPrecioUnitario()),
                    d.getCantidad(),
                    String.format("%.2f", d.getSubtotal())
            });
        }
        lblTotalCalculado.setText("TOTAL: Q" + String.format("%.2f", total));
    }

    private void registrarOrden(EstadoOrden estadoDeseado) {
        try {
            String idOrden = txtIdOrden.getText().trim();
            Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
            String idBodega = (String) cmbBodega.getSelectedItem();

            if (idOrden.isEmpty() || cliente == null || idBodega == null) {
                JOptionPane.showMessageDialog(this, "No. Orden, Cliente y Bodega de despacho son requeridos.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (itemsTemporales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La orden de compra debe contener al menos un producto.", "Requisito Obligatorio", JOptionPane.WARNING_MESSAGE);
                return;
            }

            OrdenCompra orden = new OrdenCompra(idOrden, cliente, idBodega);
            orden.setEstado(estadoDeseado);
            for (DetalleOrden d : itemsTemporales) {
                orden.agregarDetalle(d.getProducto(), d.getCantidad());
            }

            controller.registrarOrden(orden);

            String msj = (estadoDeseado == EstadoOrden.COMPLETADA)
                    ? "¡Orden #" + idOrden + " COMPLETADA con éxito!\nSe descontó automáticamente el stock del inventario."
                    : "Orden #" + idOrden + " registrada como PENDIENTE.";

            JOptionPane.showMessageDialog(this, msj, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormularioOrden();
            recargarTablaOrdenes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al procesar orden", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completarOrdenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla para completarla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idOrden = (String) ordenesTableModel.getValueAt(fila, 0);
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Desea completar la orden #" + idOrden + "?\nEsto restará los productos del stock en inventario.",
                "Confirmar Venta", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            try {
                controller.completarOrden(idOrden);
                JOptionPane.showMessageDialog(this, "Orden #" + idOrden + " completada y stock descontado con éxito.", "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
                recargarTablaOrdenes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al completar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelarOrdenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla para cancelarla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idOrden = (String) ordenesTableModel.getValueAt(fila, 0);
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar la orden #" + idOrden + "?",
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            try {
                controller.cancelarOrden(idOrden);
                JOptionPane.showMessageDialog(this, "Orden #" + idOrden + " cancelada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                recargarTablaOrdenes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cancelar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormularioOrden() {
        txtIdOrden.setText("");
        itemsTemporales.clear();
        actualizarTablaCarrito();
        txtCantidadItem.setText("1");
    }

    public void recargarTablaOrdenes() {
        ordenesTableModel.setRowCount(0);
        for (OrdenCompra ord : controller.getOrdenes()) {
            StringBuilder itemsResumen = new StringBuilder();
            for (DetalleOrden det : ord.getItems()) {
                if (itemsResumen.length() > 0) itemsResumen.append(", ");
                itemsResumen.append(det.getProducto().getNombre()).append(" x").append(det.getCantidad());
            }

            ordenesTableModel.addRow(new Object[]{
                    ord.getIdOrden(),
                    ord.getFecha(),
                    ord.getCliente().getNombre(),
                    ord.getCliente().getDpi(),
                    ord.getIdBodega(),
                    itemsResumen.toString(),
                    String.format("%.2f", ord.getTotal()),
                    ord.getEstado()
            });
        }
    }
}
