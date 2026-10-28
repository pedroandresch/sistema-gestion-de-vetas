package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Bodega;
import com.sistema.model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BodegaPanel extends JPanel {

    private final SistemaController controller;

    private JTextField txtIdBodega;
    private JComboBox<Empleado> cmbEmpleado;
    private JTextField txtCapacidad;
    private JTextField txtDireccion;

    private JTable tablaBodegas;
    private DefaultTableModel tableModel;

    public BodegaPanel(SistemaController controller) {
        this.controller = controller;
        initComponents();
        recargarEmpleadosCombo();
        recargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nueva Bodega"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdBodega = new JTextField(15);
        cmbEmpleado = new JComboBox<>();
        txtCapacidad = new JTextField(15);
        txtDireccion = new JTextField(15);

        JButton btnGuardar = new JButton("Registrar Bodega");
        btnGuardar.setBackground(new Color(255, 152, 0));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JButton btnLimpiar = new JButton("Limpiar");

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID Bodega:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtIdBodega, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Empleado Encargado:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(cmbEmpleado, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Capacidad Máxima:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtCapacidad, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtDireccion, gbc);

        // Fila 2
        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(btnLimpiar, gbc);
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(btnGuardar, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID Bodega", "Empleado Encargado", "Capacidad", "Dirección"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaBodegas = new JTable(tableModel);
        tablaBodegas.setRowHeight(24);
        tablaBodegas.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaBodegas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Bodegas Registradas"));

        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarBodega());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    public void recargarEmpleadosCombo() {
        cmbEmpleado.removeAllItems();
        for (Empleado e : controller.getEmpleados()) {
            cmbEmpleado.addItem(e);
        }
    }

    private void guardarBodega() {
        try {
            String id = txtIdBodega.getText().trim();
            Empleado emp = (Empleado) cmbEmpleado.getSelectedItem();
            String capStr = txtCapacidad.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (id.isEmpty() || capStr.isEmpty() || direccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Capacidad y Dirección son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int capacidad = Integer.parseInt(capStr);
            if (capacidad <= 0) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Bodega b = new Bodega(id, emp, capacidad, direccion);
            controller.registrarBodega(b);

            JOptionPane.showMessageDialog(this, "Bodega registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            recargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtIdBodega.setText("");
        txtCapacidad.setText("");
        txtDireccion.setText("");
        if (cmbEmpleado.getItemCount() > 0) {
            cmbEmpleado.setSelectedIndex(0);
        }
    }

    public void recargarTabla() {
        tableModel.setRowCount(0);
        for (Bodega b : controller.getBodegas()) {
            String empInfo = (b.getEmpleado() != null) ? b.getEmpleado().getNombre() + " (" + b.getEmpleado().getIdEmpleado() + ")" : "Sin asignar";
            tableModel.addRow(new Object[]{
                    b.getIdBodega(),
                    empInfo,
                    b.getCapacidad(),
                    b.getDireccion()
            });
        }
    }
}
