package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductoPanel extends JPanel {

    private final SistemaController controller;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtMarca;
    private JTextField txtPrecio;

    private JTable tablaProductos;
    private DefaultTableModel tableModel;

    public ProductoPanel(SistemaController controller) {
        this.controller = controller;
        initComponents();
        recargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        txtDescripcion = new JTextField(15);
        txtMarca = new JTextField(15);
        txtPrecio = new JTextField(15);

        JButton btnGuardar = new JButton("Registrar Producto");
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JButton btnLimpiar = new JButton("Limpiar");

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtCodigo, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(txtNombre, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtDescripcion, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtMarca, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Precio (Q):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtPrecio, gbc);
        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(btnLimpiar, gbc);
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(btnGuardar, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Código", "Nombre", "Descripción", "Marca", "Precio (Q)"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(tableModel);
        tablaProductos.setRowHeight(24);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Catálogo de Productos"));

        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarProducto());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void guardarProducto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String marca = txtMarca.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código, Nombre y Precio son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto p = new Producto(codigo, nombre, descripcion, marca, precio);
            controller.registrarProducto(p);

            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            recargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
    }

    public void recargarTabla() {
        tableModel.setRowCount(0);
        for (Producto p : controller.getProductos()) {
            tableModel.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getMarca(),
                    String.format("%.2f", p.getPrecio())
            });
        }
    }
}
