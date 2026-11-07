package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Cliente;
import com.sistema.model.TipoCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientePanel extends JPanel {

    private SistemaController controlador;

    private JTextField txtDpi;
    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtFechaNacimiento;
    private JComboBox<TipoCliente> cmbTipo;

    private JButton btnGuardar;
    private JButton btnLimpiar;

    private JTextField txtBuscar;
    private JLabel lblContador;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public ClientePanel(SistemaController controlador) {
        this.controlador = controlador;
        iniciarComponentes();
        recargarTabla();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(20, 0));
        setBackground(UITheme.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Formulario izquierdo para registro y edicion
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 14));
        panelFormulario.setPreferredSize(new Dimension(350, 0));
        UITheme.estilizarTarjeta(panelFormulario);
        panelFormulario.add(UITheme.crearEncabezadoSeccion("Datos del Cliente", null), BorderLayout.NORTH);

        txtDpi = new JTextField();
        txtNombre = new JTextField();
        txtDireccion = new JTextField();
        txtFechaNacimiento = new JTextField();
        cmbTipo = new JComboBox<>(TipoCliente.values());

        UITheme.estilizarCampoTexto(txtDpi, "13 dígitos");
        UITheme.estilizarCampoTexto(txtNombre, "Nombre completo");
        UITheme.estilizarCampoTexto(txtDireccion, "Dirección");
        UITheme.estilizarCampoTexto(txtFechaNacimiento, "AAAA-MM-DD");
        UITheme.estilizarCombo(cmbTipo);

        JPanel panelCampos = new JPanel(new GridLayout(5, 1, 0, 10));
        panelCampos.setOpaque(false);
        panelCampos.add(UITheme.crearCampoFormulario("DPI:", txtDpi));
        panelCampos.add(UITheme.crearCampoFormulario("Nombre Completo:", txtNombre));
        panelCampos.add(UITheme.crearCampoFormulario("Dirección:", txtDireccion));
        panelCampos.add(UITheme.crearCampoFormulario("Fecha de Nacimiento:", txtFechaNacimiento));
        panelCampos.add(UITheme.crearCampoFormulario("Tipo de Cliente:", cmbTipo));

        JPanel contenedorCampos = new JPanel(new BorderLayout());
        contenedorCampos.setOpaque(false);
        contenedorCampos.add(panelCampos, BorderLayout.NORTH);
        panelFormulario.add(contenedorCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);

        btnLimpiar = new JButton("Nuevo / Limpiar");
        UITheme.estilizarBotonSecundario(btnLimpiar);

        btnGuardar = new JButton("Guardar");
        UITheme.estilizarBotonPrimario(btnGuardar, UITheme.COLOR_EXITO);

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.WEST);

        // Tabla derecha con buscador de clientes
        JPanel panelTabla = new JPanel(new BorderLayout(0, 12));
        UITheme.estilizarTarjeta(panelTabla);

        lblContador = new JLabel("0 registrados");

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout(0, 8));
        panelSuperiorTabla.setOpaque(false);
        panelSuperiorTabla.add(UITheme.crearEncabezadoSeccion("Directorio de Clientes", lblContador), BorderLayout.NORTH);

        JPanel barraBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraBusqueda.setOpaque(false);
        txtBuscar = new JTextField(20);
        UITheme.estilizarCampoTexto(txtBuscar, "Buscar por DPI o nombre...");
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

        String[] columnas = {"DPI", "Nombre", "Dirección", "Fecha Nacimiento", "Tipo Cliente"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        UITheme.estilizarTabla(tablaClientes);
        panelTabla.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Eventos de botones y seleccion de fila
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarOModificarCliente());
        btnBuscar.addActionListener(e -> recargarTabla(txtBuscar.getText().trim()));
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            recargarTabla("");
        });

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
    }

    // Evento al seleccionar una fila: carga los datos en el formulario
    private void cargarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
        String dpi = String.valueOf(modeloTabla.getValueAt(filaSeleccionada, 0));
        for (Cliente c : controlador.getClientes()) {
            if (c.getDpi().equalsIgnoreCase(dpi)) {
                txtDpi.setText(c.getDpi());
                txtNombre.setText(c.getNombre());
                txtDireccion.setText(c.getDireccion());
                txtFechaNacimiento.setText(c.getFechaNacimiento());
                cmbTipo.setSelectedItem(c.getTipo());
                txtDpi.setEditable(false);
                btnGuardar.setText("Modificar");
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtDpi.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtFechaNacimiento.setText("");
        cmbTipo.setSelectedIndex(0);
        txtDpi.setEditable(true);
        btnGuardar.setText("Guardar");
        tablaClientes.clearSelection();
    }

    // Validar campos y enviar al controlador
    private void guardarOModificarCliente() {
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
            if (txtDpi.isEditable()) {
                controlador.registrarCliente(c);
                JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
            } else {
                controlador.actualizarCliente(c);
                JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
            }

            limpiarFormulario();
            recargarTabla(txtBuscar.getText().trim());
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

        for (Cliente c : controlador.getClientes()) {
            boolean coincide = busqueda.isEmpty()
                    || c.getDpi().toLowerCase().contains(busqueda)
                    || c.getNombre().toLowerCase().contains(busqueda);

            if (coincide) {
                modeloTabla.addRow(new Object[]{
                        c.getDpi(),
                        c.getNombre(),
                        c.getDireccion(),
                        c.getFechaNacimiento(),
                        c.getTipo()
                });
            }
        }
        lblContador.setText(modeloTabla.getRowCount() + " registrados");
    }
}

// Modulo desarrollado por Persona 1
