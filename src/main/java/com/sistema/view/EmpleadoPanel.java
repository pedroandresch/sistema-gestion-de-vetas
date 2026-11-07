package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmpleadoPanel extends JPanel {

    private SistemaController controlador;

    private JTextField txtIdEmpleado;
    private JTextField txtNombre;
    private JTextField txtDpi;
    private JTextField txtDireccion;
    private JTextField txtPuesto;
    private JTextField txtSalario;

    private JTextField txtBuscar;
    private JLabel lblContador;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;

    public EmpleadoPanel(SistemaController controlador) {
        this.controlador = controlador;
        iniciarComponentes();
        recargarTabla();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Formulario izquierdo para registro de empleados
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 14));
        UITheme.estilizarTarjeta(panelFormulario);
        panelFormulario.setPreferredSize(new Dimension(350, 0));
        panelFormulario.add(UITheme.crearEncabezadoSeccion("Datos del Empleado", null), BorderLayout.NORTH);

        txtIdEmpleado = new JTextField();
        txtNombre = new JTextField();
        txtDpi = new JTextField();
        txtDireccion = new JTextField();
        txtPuesto = new JTextField();
        txtSalario = new JTextField();

        UITheme.estilizarCampoTexto(txtIdEmpleado, "Ej. EMP-01");
        UITheme.estilizarCampoTexto(txtNombre, "Nombre completo");
        UITheme.estilizarCampoTexto(txtDpi, "13 dígitos");
        UITheme.estilizarCampoTexto(txtDireccion, "Dirección");
        UITheme.estilizarCampoTexto(txtPuesto, "Puesto laboral");
        UITheme.estilizarCampoTexto(txtSalario, "0.00");

        JPanel panelCampos = new JPanel(new GridLayout(6, 1, 0, 10));
        panelCampos.setOpaque(false);
        panelCampos.add(UITheme.crearCampoFormulario("ID Empleado:", txtIdEmpleado));
        panelCampos.add(UITheme.crearCampoFormulario("Nombre Completo:", txtNombre));
        panelCampos.add(UITheme.crearCampoFormulario("DPI:", txtDpi));
        panelCampos.add(UITheme.crearCampoFormulario("Dirección:", txtDireccion));
        panelCampos.add(UITheme.crearCampoFormulario("Puesto:", txtPuesto));
        panelCampos.add(UITheme.crearCampoFormulario("Salario Mensual (Q):", txtSalario));

        JPanel contenedorCampos = new JPanel(new BorderLayout());
        contenedorCampos.setOpaque(false);
        contenedorCampos.add(panelCampos, BorderLayout.NORTH);
        panelFormulario.add(contenedorCampos, BorderLayout.CENTER);

        JButton btnLimpiar = new JButton("Nuevo / Limpiar");
        UITheme.estilizarBotonSecundario(btnLimpiar);

        JButton btnGuardar = new JButton("Guardar");
        UITheme.estilizarBotonPrimario(btnGuardar, new Color(124, 58, 237));

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.WEST);

        // Tabla derecha con buscador de empleados
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(panelTabla);

        lblContador = new JLabel("0 registrados");

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout(10, 8));
        panelSuperiorTabla.setOpaque(false);
        panelSuperiorTabla.add(UITheme.crearEncabezadoSeccion("Nómina de Empleados", lblContador), BorderLayout.NORTH);

        JPanel barraBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraBusqueda.setOpaque(false);
        txtBuscar = new JTextField(20);
        UITheme.estilizarCampoTexto(txtBuscar, "Buscar por ID, nombre o puesto...");
        JButton btnBuscar = new JButton("Buscar");
        UITheme.estilizarBotonSecundario(btnBuscar);
        JButton btnVerTodos = new JButton("Ver Todos");
        UITheme.estilizarBotonSecundario(btnVerTodos);

        barraBusqueda.add(new JLabel("Buscar:"));
        barraBusqueda.add(txtBuscar);
        barraBusqueda.add(btnBuscar);
        barraBusqueda.add(btnVerTodos);

        panelSuperiorTabla.add(barraBusqueda, BorderLayout.SOUTH);
        panelTabla.add(panelSuperiorTabla, BorderLayout.NORTH);

        String[] columnas = {"ID Empleado", "Nombre", "DPI", "Dirección", "Puesto", "Salario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloTabla);
        UITheme.estilizarTabla(tablaEmpleados);
        panelTabla.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Eventos de botones y seleccion en la tabla
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnBuscar.addActionListener(e -> recargarTabla(txtBuscar.getText().trim()));
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            recargarTabla("");
        });

        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEmpleadoSeleccionado();
            }
        });
    }

    // Evento al seleccionar una fila: carga los datos en el formulario
    private void cargarEmpleadoSeleccionado() {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
        String id = String.valueOf(modeloTabla.getValueAt(filaSeleccionada, 0));
        for (Empleado emp : controlador.getEmpleados()) {
            if (emp.getIdEmpleado().equalsIgnoreCase(id)) {
                txtIdEmpleado.setText(emp.getIdEmpleado());
                txtNombre.setText(emp.getNombre());
                txtDpi.setText(emp.getDpi());
                txtDireccion.setText(emp.getDireccion());
                txtPuesto.setText(emp.getPuesto());
                txtSalario.setText(String.valueOf(emp.getSalario()));
                txtIdEmpleado.setEditable(false);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtIdEmpleado.setText("");
        txtNombre.setText("");
        txtDpi.setText("");
        txtDireccion.setText("");
        txtPuesto.setText("");
        txtSalario.setText("");
        txtIdEmpleado.setEditable(true);
        tablaEmpleados.clearSelection();
    }

    // Validar campos y enviar al controlador
    private void guardarEmpleado() {
        try {
            if (!txtIdEmpleado.isEditable()) {
                JOptionPane.showMessageDialog(this, "Presione 'Nuevo / Limpiar' para registrar otro empleado.");
                return;
            }

            String id = txtIdEmpleado.getText().trim();
            String nombre = txtNombre.getText().trim();
            String dpi = txtDpi.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String puesto = txtPuesto.getText().trim();
            String salarioTexto = txtSalario.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || dpi.isEmpty() || salarioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Nombre, DPI y Salario son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double salario = Double.parseDouble(salarioTexto);
            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Empleado emp = new Empleado(id, nombre, dpi, direccion, puesto, salario);
            controlador.registrarEmpleado(emp);

            JOptionPane.showMessageDialog(this, "Empleado registrado exitosamente.");
            limpiarFormulario();
            recargarTabla(txtBuscar.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTabla() {
        recargarTabla("");
    }

    public void recargarTabla(String filtro) {
        modeloTabla.setRowCount(0);
        String busqueda = filtro.toLowerCase();

        for (Empleado e : controlador.getEmpleados()) {
            boolean coincide = busqueda.isEmpty()
                    || e.getIdEmpleado().toLowerCase().contains(busqueda)
                    || e.getNombre().toLowerCase().contains(busqueda)
                    || e.getPuesto().toLowerCase().contains(busqueda);

            if (coincide) {
                modeloTabla.addRow(new Object[]{
                        e.getIdEmpleado(),
                        e.getNombre(),
                        e.getDpi(),
                        e.getDireccion(),
                        e.getPuesto(),
                        UITheme.formatearMoneda(e.getSalario())
                });
            }
        }
        lblContador.setText(modeloTabla.getRowCount() + " empleados");
    }
}

// Modulo desarrollado por Persona 1
