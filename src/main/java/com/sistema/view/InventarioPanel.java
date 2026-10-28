package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Bodega;
import com.sistema.model.Inventario;
import com.sistema.model.ItemInventario;
import com.sistema.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class InventarioPanel extends JPanel {

    private final SistemaController controller;

    private JComboBox<String> cmbBodega;
    private JComboBox<Producto> cmbProducto;
    private JTextField txtCantidad;

    private JTable tablaInventario;
    private DefaultTableModel tableModel;

    private JLabel lblCapacidadInfo;

    public InventarioPanel(SistemaController controller) {
        this.controller = controller;
        initComponents();
        recargarCombos();
        recargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar / Ingresar Stock al Inventario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbBodega = new JComboBox<>();
        cmbProducto = new JComboBox<>();
        txtCantidad = new JTextField(10);
        lblCapacidadInfo = new JLabel("Ocupación: -");
        lblCapacidadInfo.setFont(new Font("SansSerif", Font.BOLD, 12));

        JButton btnAgregarStock = new JButton("Ingresar Stock");
        btnAgregarStock.setBackground(new Color(0, 150, 136));
        btnAgregarStock.setForeground(Color.WHITE);
        btnAgregarStock.setFocusPainted(false);

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Seleccionar Bodega:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(cmbBodega, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(cmbProducto, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Cantidad a ingresar:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtCantidad, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(lblCapacidadInfo, gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(btnAgregarStock, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Bodega", "Código Producto", "Producto", "Marca", "Precio Unitario (Q)", "Stock Disponible"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaInventario = new JTable(tableModel);
        tablaInventario.setRowHeight(24);
        tablaInventario.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaInventario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Inventario por Bodega"));

        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnAgregarStock.addActionListener(e -> ingresarStock());
        cmbBodega.addActionListener(e -> {
            actualizarInfoCapacidad();
            recargarTabla();
        });
    }

    public void recargarCombos() {
        cmbBodega.removeAllItems();
        for (Bodega b : controller.getBodegas()) {
            cmbBodega.addItem(b.getIdBodega());
        }

        cmbProducto.removeAllItems();
        for (Producto p : controller.getProductos()) {
            cmbProducto.addItem(p);
        }

        actualizarInfoCapacidad();
    }

    private void actualizarInfoCapacidad() {
        String idBodega = (String) cmbBodega.getSelectedItem();
        if (idBodega == null) {
            lblCapacidadInfo.setText("Ocupación: -");
            return;
        }

        Optional<Bodega> bOpt = controller.buscarBodega(idBodega);
        if (bOpt.isPresent()) {
            Bodega b = bOpt.get();
            Inventario inv = controller.obtenerOCrearInventario(idBodega);
            int ocupadas = inv.getTotalUnidadesAlmacenadas();
            lblCapacidadInfo.setText("Ocupación: " + ocupadas + " / " + b.getCapacidad() + " unidades");
        }
    }

    private void ingresarStock() {
        try {
            String idBodega = (String) cmbBodega.getSelectedItem();
            Producto producto = (Producto) cmbProducto.getSelectedItem();
            String cantStr = txtCantidad.getText().trim();

            if (idBodega == null || producto == null || cantStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione bodega, producto e ingrese la cantidad.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.registrarStock(idBodega, producto, cantidad);

            JOptionPane.showMessageDialog(this, "Stock ingresado exitosamente en bodega " + idBodega + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtCantidad.setText("");
            actualizarInfoCapacidad();
            recargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al registrar stock", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTabla() {
        tableModel.setRowCount(0);
        String filtroBodega = (String) cmbBodega.getSelectedItem();

        for (Inventario inv : controller.getInventarios()) {
            if (filtroBodega != null && !filtroBodega.equalsIgnoreCase(inv.getIdBodega())) {
                continue;
            }
            for (ItemInventario item : inv.getItems()) {
                tableModel.addRow(new Object[]{
                        inv.getIdBodega(),
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getProducto().getMarca(),
                        String.format("%.2f", item.getProducto().getPrecio()),
                        item.getStock()
                });
            }
        }
    }
}
