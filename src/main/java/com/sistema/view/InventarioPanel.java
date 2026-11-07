package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Bodega;
import com.sistema.model.Inventario;
import com.sistema.model.ItemInventario;
import com.sistema.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventarioPanel extends JPanel {

    private SistemaController controlador;

    private JComboBox<String> cmbBodega;
    private JComboBox<Producto> cmbProducto;
    private JTextField txtCantidad;

    private JLabel lblResumenCapacidad;
    private JLabel lblContador;
    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;

    public InventarioPanel(SistemaController controlador) {
        this.controlador = controlador;
        iniciarComponentes();
        recargarCombos();
        recargarTabla();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Panel superior para ingresar existencias a una bodega
        JPanel panelFormulario = new JPanel(new BorderLayout(12, 12));
        UITheme.estilizarTarjeta(panelFormulario);

        lblResumenCapacidad = new JLabel("Capacidad: -");
        panelFormulario.add(UITheme.crearEncabezadoSeccion("Ingreso de Stock a Bodega", lblResumenCapacidad), BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 6));
        panelCampos.setOpaque(false);

        cmbBodega = new JComboBox<>();
        cmbProducto = new JComboBox<>();
        txtCantidad = new JTextField(8);

        UITheme.estilizarCombo(cmbBodega);
        UITheme.estilizarCombo(cmbProducto);
        UITheme.estilizarCampoTexto(txtCantidad, "Cantidad");

        JButton btnAgregarStock = new JButton("Ingresar Existencias");
        UITheme.estilizarBotonPrimario(btnAgregarStock, new Color(13, 148, 136));

        panelCampos.add(new JLabel("Bodega:"));
        panelCampos.add(cmbBodega);
        panelCampos.add(new JLabel("Producto:"));
        panelCampos.add(cmbProducto);
        panelCampos.add(new JLabel("Cantidad:"));
        panelCampos.add(txtCantidad);
        panelCampos.add(btnAgregarStock);

        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.NORTH);

        // Tabla inferior con las existencias actuales de la bodega
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(panelTabla);

        lblContador = new JLabel("0 productos");
        panelTabla.add(UITheme.crearEncabezadoSeccion("Existencias en Bodega", lblContador), BorderLayout.NORTH);

        String[] columnas = {"Bodega", "Código", "Producto", "Marca", "Precio Unitario", "Stock Disponible"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaInventario = new JTable(modeloTabla);
        UITheme.estilizarTabla(tablaInventario);
        panelTabla.add(new JScrollPane(tablaInventario), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Eventos para actualizar tabla e ingresar stock
        btnAgregarStock.addActionListener(e -> ingresarStock());
        cmbBodega.addActionListener(e -> recargarTabla());
    }

    public void recargarCombos() {
        String bodegaPrevia = (String) cmbBodega.getSelectedItem();

        cmbBodega.removeAllItems();
        for (Bodega b : controlador.getBodegas()) {
            cmbBodega.addItem(b.getIdBodega());
        }
        if (bodegaPrevia != null) {
            cmbBodega.setSelectedItem(bodegaPrevia);
        }

        cmbProducto.removeAllItems();
        for (Producto p : controlador.getProductos()) {
            cmbProducto.addItem(p);
        }
    }

    // Mostrar capacidad ocupada y disponible de la bodega seleccionada
    private void actualizarInfoCapacidad() {
        String idBodega = (String) cmbBodega.getSelectedItem();
        if (idBodega == null) {
            lblResumenCapacidad.setText("Sin bodega seleccionada");
            return;
        }

        for (Bodega b : controlador.getBodegas()) {
            if (b.getIdBodega().equalsIgnoreCase(idBodega)) {
                Inventario inv = controlador.obtenerOCrearInventario(idBodega);
                int ocupadas = inv.getTotalUnidadesAlmacenadas();
                int libre = Math.max(0, b.getCapacidad() - ocupadas);
                lblResumenCapacidad.setText("Ocupado: " + ocupadas + " / " + b.getCapacidad() + " uds (Libre: " + libre + " uds)");
                break;
            }
        }
    }

    // Validar campos y enviar al controlador
    private void ingresarStock() {
        try {
            String idBodega = (String) cmbBodega.getSelectedItem();
            Producto producto = (Producto) cmbProducto.getSelectedItem();
            String cantTexto = txtCantidad.getText().trim();

            if (idBodega == null || producto == null || cantTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione bodega, producto e ingrese la cantidad.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(cantTexto);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controlador.registrarStock(idBodega, producto, cantidad);

            JOptionPane.showMessageDialog(this, "Stock ingresado exitosamente en bodega " + idBodega + ".");
            txtCantidad.setText("");
            recargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTabla() {
        actualizarInfoCapacidad();
        modeloTabla.setRowCount(0);
        String filtroBodega = (String) cmbBodega.getSelectedItem();

        for (Inventario inv : controlador.getInventarios()) {
            if (filtroBodega != null && !filtroBodega.equalsIgnoreCase(inv.getIdBodega())) {
                continue;
            }
            for (ItemInventario item : inv.getItems()) {
                modeloTabla.addRow(new Object[]{
                        inv.getIdBodega(),
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getProducto().getMarca(),
                        UITheme.formatearMoneda(item.getProducto().getPrecio()),
                        item.getStock()
                });
            }
        }
        lblContador.setText(modeloTabla.getRowCount() + " productos en lista");
    }
}
