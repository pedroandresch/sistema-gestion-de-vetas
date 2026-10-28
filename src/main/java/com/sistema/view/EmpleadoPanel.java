package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmpleadoPanel extends JPanel {

    private final SistemaController controller;

    private JTextField txtIdEmpleado;
    private JTextField txtNombre;
    private JTextField txtDpi;
    private JTextField txtDireccion;
    private JTextField txtPuesto;
    private JTextField txtSalario;

    private JTable tablaEmpleados;
    private DefaultTableModel tableModel;

    public EmpleadoPanel(SistemaController controller) {
        this.controller = controller;
        initComponents();
        recargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Empleado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdEmpleado = new JTextField(15);
        txtNombre = new JTextField(15);
        txtDpi = new JTextField(15);
        txtDireccion = new JTextField(15);
        txtPuesto = new JTextField(15);
        txtSalario = new JTextField(15);

        JButton btnGuardar = new JButton("Registrar Empleado");
        btnGuardar.setBackground(new Color(156, 39, 176));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JButton btnLimpiar = new JButton("Limpiar");

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID Empleado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtIdEmpleado, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(txtNombre, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("DPI:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtDpi, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtDireccion, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Puesto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtPuesto, gbc);
        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Salario (Q):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(txtSalario, gbc);

        // Fila 3
        gbc.gridx = 2; gbc.gridy = 3; formPanel.add(btnLimpiar, gbc);
        gbc.gridx = 3; gbc.gridy = 3; formPanel.add(btnGuardar, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID Empleado", "Nombre", "DPI", "Dirección", "Puesto", "Salario (Q)"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEmpleados = new JTable(tableModel);
        tablaEmpleados.setRowHeight(24);
        tablaEmpleados.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Nómina de Empleados"));

        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void guardarEmpleado() {
        try {
            String id = txtIdEmpleado.getText().trim();
            String nombre = txtNombre.getText().trim();
            String dpi = txtDpi.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String puesto = txtPuesto.getText().trim();
            String salarioStr = txtSalario.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || dpi.isEmpty() || salarioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Nombre, DPI y Salario son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double salario = Double.parseDouble(salarioStr);
            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Empleado emp = new Empleado(id, nombre, dpi, direccion, puesto, salario);
            controller.registrarEmpleado(emp);

            JOptionPane.showMessageDialog(this, "Empleado registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            recargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtIdEmpleado.setText("");
        txtNombre.setText("");
        txtDpi.setText("");
        txtDireccion.setText("");
        txtPuesto.setText("");
        txtSalario.setText("");
    }

    public void recargarTabla() {
        tableModel.setRowCount(0);
        for (Empleado e : controller.getEmpleados()) {
            tableModel.addRow(new Object[]{
                    e.getIdEmpleado(),
                    e.getNombre(),
                    e.getDpi(),
                    e.getDireccion(),
                    e.getPuesto(),
                    String.format("%.2f", e.getSalario())
            });
        }
    }
}
