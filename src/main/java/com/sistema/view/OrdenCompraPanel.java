package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Bodega;
import com.sistema.model.Cliente;
import com.sistema.model.DetalleOrden;
import com.sistema.model.EstadoOrden;
import com.sistema.model.OrdenCompra;
import com.sistema.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompraPanel extends JPanel {

    private SistemaController controlador;
    private JTabbedPane pestanasVentas;

    private JTextField txtIdOrden;
    private JComboBox<Cliente> cmbCliente;
    private JComboBox<String> cmbBodega;
    private JComboBox<Producto> cmbProducto;
    private JLabel lblStockDisponible;
    private JTextField txtCantidadItem;

    private DefaultTableModel modeloCarrito;
    private JTable tablaCarrito;
    private JLabel lblTotalCalculado;
    private List<DetalleOrden> itemsTemporales;

    private DefaultTableModel modeloOrdenes;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloDetalleOrden;
    private JLabel lblResumenOrden;

    public OrdenCompraPanel(SistemaController controlador) {
        this.controlador = controlador;
        this.itemsTemporales = new ArrayList<>();
        iniciarComponentes();
        recargarCombos();
        recargarTablaOrdenes();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout());
        setBackground(UITheme.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        pestanasVentas = new JTabbedPane();
        pestanasVentas.setFont(UITheme.FUENTE_NEGRITA);
        pestanasVentas.addTab("Nueva Orden de Compra", crearPanelNuevaOrden());
        pestanasVentas.addTab("Historial de Órdenes", crearPanelHistorial());
        add(pestanasVentas, BorderLayout.CENTER);
    }

    // Pestana 1: formulario para armar una nueva orden de compra
    private JPanel crearPanelNuevaOrden() {
        JPanel panelNueva = new JPanel(new BorderLayout(14, 14));
        panelNueva.setBackground(UITheme.FONDO_PRINCIPAL);
        panelNueva.setBorder(BorderFactory.createEmptyBorder(12, 4, 12, 4));

        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        UITheme.estilizarTarjeta(panelEncabezado);

        txtIdOrden = new JTextField(10);
        UITheme.estilizarCampoTexto(txtIdOrden, "Ej. ORD-002");
        cmbCliente = new JComboBox<>();
        UITheme.estilizarCombo(cmbCliente);
        cmbBodega = new JComboBox<>();
        UITheme.estilizarCombo(cmbBodega);

        panelEncabezado.add(new JLabel("No. Orden:"));
        panelEncabezado.add(txtIdOrden);
        panelEncabezado.add(new JLabel("Cliente:"));
        panelEncabezado.add(cmbCliente);
        panelEncabezado.add(new JLabel("Bodega:"));
        panelEncabezado.add(cmbBodega);
        panelNueva.add(panelEncabezado, BorderLayout.NORTH);

        // Seccion central para agregar productos al carrito
        JPanel panelCarrito = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(panelCarrito);

        JPanel barraProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        barraProducto.setOpaque(false);

        cmbProducto = new JComboBox<>();
        UITheme.estilizarCombo(cmbProducto);
        lblStockDisponible = new JLabel("Stock: 0 uds");
        lblStockDisponible.setFont(UITheme.FUENTE_NEGRITA);
        txtCantidadItem = new JTextField("1", 5);
        UITheme.estilizarCampoTexto(txtCantidadItem, "Cant.");

        JButton btnAgregarItem = new JButton("Agregar Producto");
        UITheme.estilizarBotonPrimario(btnAgregarItem, UITheme.COLOR_PRIMARIO);
        JButton btnQuitarItem = new JButton("Quitar Seleccionado");
        UITheme.estilizarBotonSecundario(btnQuitarItem);

        barraProducto.add(new JLabel("Producto:"));
        barraProducto.add(cmbProducto);
        barraProducto.add(lblStockDisponible);
        barraProducto.add(new JLabel("Cantidad:"));
        barraProducto.add(txtCantidadItem);
        barraProducto.add(btnAgregarItem);
        barraProducto.add(btnQuitarItem);
        panelCarrito.add(barraProducto, BorderLayout.NORTH);

        String[] columnasCarrito = {"Código", "Producto", "Precio Unitario", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnasCarrito, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        UITheme.estilizarTabla(tablaCarrito);
        panelCarrito.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // Pie del carrito con el total y botones para registrar la orden
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);

        lblTotalCalculado = new JLabel("TOTAL: Q 0.00");
        lblTotalCalculado.setFont(UITheme.FUENTE_TITULO);
        lblTotalCalculado.setForeground(UITheme.COLOR_EXITO);

        JPanel botonesAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesAccion.setOpaque(false);

        JButton btnLimpiar = new JButton("Limpiar");
        UITheme.estilizarBotonSecundario(btnLimpiar);
        JButton btnPendiente = new JButton("Guardar PENDIENTE");
        UITheme.estilizarBotonSecundario(btnPendiente);
        JButton btnCompletar = new JButton("COMPLETAR VENTA");
        UITheme.estilizarBotonPrimario(btnCompletar, UITheme.COLOR_EXITO);

        botonesAccion.add(btnLimpiar);
        botonesAccion.add(btnPendiente);
        botonesAccion.add(btnCompletar);

        panelInferior.add(lblTotalCalculado, BorderLayout.WEST);
        panelInferior.add(botonesAccion, BorderLayout.EAST);
        panelCarrito.add(panelInferior, BorderLayout.SOUTH);
        panelNueva.add(panelCarrito, BorderLayout.CENTER);

        btnAgregarItem.addActionListener(e -> agregarItemAlCarrito());
        btnQuitarItem.addActionListener(e -> quitarItemDelCarrito());
        btnLimpiar.addActionListener(e -> limpiarFormularioOrden());
        btnPendiente.addActionListener(e -> registrarOrden(EstadoOrden.PENDIENTE));
        btnCompletar.addActionListener(e -> registrarOrden(EstadoOrden.COMPLETADA));
        cmbBodega.addActionListener(e -> actualizarEtiquetaStock());
        cmbProducto.addActionListener(e -> actualizarEtiquetaStock());

        return panelNueva;
    }

    // Pestana 2: historial de ordenes y detalle de productos comprados
    private JPanel crearPanelHistorial() {
        JPanel panelHistorial = new JPanel(new BorderLayout(16, 0));
        panelHistorial.setBackground(UITheme.FONDO_PRINCIPAL);
        panelHistorial.setBorder(BorderFactory.createEmptyBorder(12, 4, 12, 4));

        JPanel tarjetaOrdenes = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(tarjetaOrdenes);
        tarjetaOrdenes.add(UITheme.crearEncabezadoSeccion("Órdenes Registradas", null), BorderLayout.NORTH);

        String[] columnasOrdenes = {"No. Orden", "Fecha", "Cliente", "Bodega", "Total", "Estado"};
        modeloOrdenes = new DefaultTableModel(columnasOrdenes, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaOrdenes = new JTable(modeloOrdenes);
        UITheme.estilizarTabla(tablaOrdenes);
        tarjetaOrdenes.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        JPanel botonesHistorial = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesHistorial.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar Orden");
        UITheme.estilizarBotonSecundario(btnCancelar);
        JButton btnCompletar = new JButton("Completar Orden Seleccionada");
        UITheme.estilizarBotonPrimario(btnCompletar, UITheme.COLOR_PRIMARIO);

        botonesHistorial.add(btnCancelar);
        botonesHistorial.add(btnCompletar);
        tarjetaOrdenes.add(botonesHistorial, BorderLayout.SOUTH);

        JPanel tarjetaDetalle = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(tarjetaDetalle);
        tarjetaDetalle.setPreferredSize(new Dimension(360, 0));

        lblResumenOrden = new JLabel("Seleccione una orden");
        tarjetaDetalle.add(UITheme.crearEncabezadoSeccion("Detalle de la Orden", lblResumenOrden), BorderLayout.NORTH);

        String[] columnasDetalle = {"Código", "Producto", "Cant.", "Subtotal"};
        modeloDetalleOrden = new DefaultTableModel(columnasDetalle, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tablaDetalle = new JTable(modeloDetalleOrden);
        UITheme.estilizarTabla(tablaDetalle);
        tarjetaDetalle.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        panelHistorial.add(tarjetaOrdenes, BorderLayout.CENTER);
        panelHistorial.add(tarjetaDetalle, BorderLayout.EAST);

        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleOrdenSeleccionada();
            }
        });
        btnCompletar.addActionListener(e -> cambiarEstadoOrdenSeleccionada( true));
        btnCancelar.addActionListener(e -> cambiarEstadoOrdenSeleccionada(false));

        return panelHistorial;
    }

    public void recargarCombos() {
        cmbCliente.removeAllItems();
        for (Cliente c : controlador.getClientes()) {
            cmbCliente.addItem(c);
        }
        cmbBodega.removeAllItems();
        for (Bodega b : controlador.getBodegas()) {
            cmbBodega.addItem(b.getIdBodega());
        }
        cmbProducto.removeAllItems();
        for (Producto p : controlador.getProductos()) {
            cmbProducto.addItem(p);
        }
        actualizarEtiquetaStock();
    }

    private void actualizarEtiquetaStock() {
        String idBodega = (String) cmbBodega.getSelectedItem();
        Producto prod = (Producto) cmbProducto.getSelectedItem();
        if (idBodega == null || prod == null) {
            lblStockDisponible.setText("Stock: 0 uds");
            return;
        }
        int stock = controlador.consultarStock(idBodega, prod.getCodigo());
        lblStockDisponible.setText("Stock: " + stock + " uds | " + UITheme.formatearMoneda(prod.getPrecio()));
    }

    private void agregarItemAlCarrito() {
        try {
            Producto prod = (Producto) cmbProducto.getSelectedItem();
            int cantidad = Integer.parseInt(txtCantidadItem.getText().trim());
            if (prod == null || cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto y una cantidad mayor a 0.");
                return;
            }
            for (DetalleOrden d : itemsTemporales) {
                if (d.getProducto().getCodigo().equalsIgnoreCase(prod.getCodigo())) {
                    d.setCantidad(d.getCantidad() + cantidad);
                    actualizarTablaCarrito();
                    return;
                }
            }
            itemsTemporales.add(new DetalleOrden(prod, cantidad));
            txtCantidadItem.setText("1");
            actualizarTablaCarrito();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarItemDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila >= 0 && fila < itemsTemporales.size()) {
            itemsTemporales.remove(fila);
            actualizarTablaCarrito();
        }
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        double total = 0;
        for (DetalleOrden d : itemsTemporales) {
            total += d.getSubtotal();
            modeloCarrito.addRow(new Object[]{
                    d.getProducto().getCodigo(), d.getProducto().getNombre(),
                    UITheme.formatearMoneda(d.getPrecioUnitario()), d.getCantidad(),
                    UITheme.formatearMoneda(d.getSubtotal())
            });
        }
        lblTotalCalculado.setText("TOTAL: " + UITheme.formatearMoneda(total));
    }

    // Validar campos y registrar la orden de compra en el controlador
    private void registrarOrden(EstadoOrden estado) {
        try {
            String idOrden = txtIdOrden.getText().trim();
            Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
            String idBodega = (String) cmbBodega.getSelectedItem();

            if (idOrden.isEmpty() || cliente == null || idBodega == null || itemsTemporales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los datos de la orden y agregue al menos un producto.");
                return;
            }

            OrdenCompra orden = new OrdenCompra(idOrden, cliente, idBodega);
            orden.setEstado(estado);
            for (DetalleOrden d : itemsTemporales) {
                orden.agregarDetalle(d.getProducto(), d.getCantidad());
            }

            controlador.registrarOrden(orden);
            JOptionPane.showMessageDialog(this, "Orden registrada correctamente con estado: " + estado);
            limpiarFormularioOrden();
            recargarTablaOrdenes();
            actualizarEtiquetaStock();
            pestanasVentas.setSelectedIndex(1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormularioOrden() {
        txtIdOrden.setText("");
        itemsTemporales.clear();
        actualizarTablaCarrito();
    }

    // Mostrar los productos de la orden seleccionada en la tabla derecha
    private void mostrarDetalleOrdenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        modeloDetalleOrden.setRowCount(0);
        if (fila == -1) {
            lblResumenOrden.setText("Seleccione una orden");
            return;
        }
        String idOrden = String.valueOf(modeloOrdenes.getValueAt(fila, 0));
        for (OrdenCompra o : controlador.getOrdenes()) {
            if (o.getIdOrden().equalsIgnoreCase(idOrden)) {
                lblResumenOrden.setText(o.getIdOrden() + " (" + o.getEstado() + ")");
                for (DetalleOrden d : o.getItems()) {
                    modeloDetalleOrden.addRow(new Object[]{
                            d.getProducto().getCodigo(), d.getProducto().getNombre(),
                            d.getCantidad(), UITheme.formatearMoneda(d.getSubtotal())
                    });
                }
                break;
            }
        }
    }

    private void cambiarEstadoOrdenSeleccionada(boolean completar) {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla.");
            return;
        }
        String idOrden = String.valueOf(modeloOrdenes.getValueAt(fila, 0));
        try {
            if (completar) {
                controlador.completarOrden(idOrden);
                JOptionPane.showMessageDialog(this, "Orden completada y stock descontado.");
            } else {
                controlador.cancelarOrden(idOrden);
                JOptionPane.showMessageDialog(this, "Orden cancelada.");
            }
            recargarTablaOrdenes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTablaOrdenes() {
        modeloOrdenes.setRowCount(0);
        for (OrdenCompra ord : controlador.getOrdenes()) {
            modeloOrdenes.addRow(new Object[]{
                    ord.getIdOrden(), ord.getFecha(), ord.getCliente().getNombre(),
                    ord.getIdBodega(), UITheme.formatearMoneda(ord.getTotal()), ord.getEstado()
            });
        }
    }
}
