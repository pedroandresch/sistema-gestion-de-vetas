package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Cliente;
import com.sistema.model.TipoCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientePanel extends JPanel {

    private final SistemaController controller;

    private JTextField txtDpi;
    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtFechaNacimiento;
    private JComboBox<TipoCliente> cmbTipo;

    private JTable tablaClientes;
    private DefaultTableModel tableModel;

    public ClientePanel(SistemaController controller) {
        this.controller = controller;
        initComponents();
        recargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDpi = new JTextField(15);
        txtNombre = new JTextField(15);
        txtDireccion = new JTextField(15);
        txtFechaNacimiento = new JTextField(15);
        cmbTipo = new JComboBox<>(TipoCliente.values());

        JButton btnGuardar = new JButton("Registrar Cliente");
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JButton btnLimpiar = new JButton("Limpiar");

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("DPI:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtDpi, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formPanel.add(txtNombre, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtDireccion, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Fecha Nac. (AAAA-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtFechaNacimiento, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Tipo de Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(cmbTipo, gbc);
        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(btnLimpiar, gbc);
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(btnGuardar, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"DPI", "Nombre", "Dirección", "Fecha Nacimiento", "Tipo Cliente"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(tableModel);
        tablaClientes.setRowHeight(24);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Clientes"));

        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void guardarCliente() {
        try {
            String dpi = txtDpi.getText().trim();
            String nombre = txtNombre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String fecha = txtFechaNacimiento.getText().trim();
            TipoCliente tipo = (TipoCliente) cmbTipo.getSelectedItem();

            if (dpi.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El DPI y Nombre son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente c = new Cliente(dpi, nombre, direccion, fecha, tipo);
            controller.registrarCliente(c);

            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            recargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtDpi.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtFechaNacimiento.setText("");
        cmbTipo.setSelectedIndex(0);
    }

    public void recargarTabla() {
        tableModel.setRowCount(0);
        for (Cliente c : controller.getClientes()) {
            tableModel.addRow(new Object[]{
                    c.getDpi(),
                    c.getNombre(),
                    c.getDireccion(),
                    c.getFechaNacimiento(),
                    c.getTipo()
            });
        }
    }
}
