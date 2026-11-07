package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Bodega;
import com.sistema.model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BodegaPanel extends JPanel {

    private SistemaController controlador;

    private JTextField txtIdBodega;
    private JComboBox<Empleado> cmbEmpleado;
    private JTextField txtCapacidad;
    private JTextField txtDireccion;

    private JTextField txtBuscar;
    private JLabel lblContador;
    private JTable tablaBodegas;
    private DefaultTableModel modeloTabla;

    public BodegaPanel(SistemaController controlador) {
        this.controlador = controlador;
        iniciarComponentes();
        recargarEmpleadosCombo();
        recargarTabla();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Formulario izquierdo para registro de bodegas
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 14));
        UITheme.estilizarTarjeta(panelFormulario);
        panelFormulario.setPreferredSize(new Dimension(350, 0));
        panelFormulario.add(UITheme.crearEncabezadoSeccion("Datos de la Bodega", null), BorderLayout.NORTH);

        txtIdBodega = new JTextField();
        cmbEmpleado = new JComboBox<>();
        txtCapacidad = new JTextField();
        txtDireccion = new JTextField();

        UITheme.estilizarCampoTexto(txtIdBodega, "Ej. BOD-01");
        UITheme.estilizarCombo(cmbEmpleado);
        UITheme.estilizarCampoTexto(txtCapacidad, "Capacidad máxima");
        UITheme.estilizarCampoTexto(txtDireccion, "Ubicación o dirección");

        JPanel panelCampos = new JPanel(new GridLayout(4, 1, 0, 10));
        panelCampos.setOpaque(false);
        panelCampos.add(UITheme.crearCampoFormulario("ID Bodega:", txtIdBodega));
        panelCampos.add(UITheme.crearCampoFormulario("Empleado Encargado:", cmbEmpleado));
        panelCampos.add(UITheme.crearCampoFormulario("Capacidad Máxima (Uds):", txtCapacidad));
        panelCampos.add(UITheme.crearCampoFormulario("Dirección:", txtDireccion));

        JPanel contenedorCampos = new JPanel(new BorderLayout());
        contenedorCampos.setOpaque(false);
        contenedorCampos.add(panelCampos, BorderLayout.NORTH);
        panelFormulario.add(contenedorCampos, BorderLayout.CENTER);

        JButton btnLimpiar = new JButton("Nuevo / Limpiar");
        UITheme.estilizarBotonSecundario(btnLimpiar);

        JButton btnGuardar = new JButton("Guardar");
        UITheme.estilizarBotonPrimario(btnGuardar, UITheme.COLOR_ADVERTENCIA);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.WEST);

        // Tabla derecha con buscador de bodegas
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(panelTabla);

        lblContador = new JLabel("0 bodegas");

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout(10, 8));
        panelSuperiorTabla.setOpaque(false);
        panelSuperiorTabla.add(UITheme.crearEncabezadoSeccion("Bodegas Registradas", lblContador), BorderLayout.NORTH);

        JPanel barraBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraBusqueda.setOpaque(false);
        txtBuscar = new JTextField(20);
        UITheme.estilizarCampoTexto(txtBuscar, "Buscar por ID o dirección...");
        JButton btnBuscar = new JButton("Buscar");
        UITheme.estilizarBotonSecundario(btnBuscar);
        JButton btnVerTodas = new JButton("Ver Todas");
        UITheme.estilizarBotonSecundario(btnVerTodas);

        barraBusqueda.add(new JLabel("Buscar:"));
        barraBusqueda.add(txtBuscar);
        barraBusqueda.add(btnBuscar);
        barraBusqueda.add(btnVerTodas);

        panelSuperiorTabla.add(barraBusqueda, BorderLayout.SOUTH);
        panelTabla.add(panelSuperiorTabla, BorderLayout.NORTH);

        String[] columnas = {"ID Bodega", "Empleado Encargado", "Capacidad Máxima", "Ocupación Actual", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaBodegas = new JTable(modeloTabla);
        UITheme.estilizarTabla(tablaBodegas);
        panelTabla.add(new JScrollPane(tablaBodegas), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Eventos de botones y seleccion en la tabla
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarBodega());
        btnBuscar.addActionListener(e -> recargarTabla(txtBuscar.getText().trim()));
        btnVerTodas.addActionListener(e -> {
            txtBuscar.setText("");
            recargarTabla("");
        });

        tablaBodegas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarBodegaSeleccionada();
            }
        });
    }

    public void recargarEmpleadosCombo() {
        cmbEmpleado.removeAllItems();
        for (Empleado e : controlador.getEmpleados()) {
            cmbEmpleado.addItem(e);
        }
    }

    // Evento al seleccionar una fila: carga los datos en el formulario
    private void cargarBodegaSeleccionada() {
        int filaSeleccionada = tablaBodegas.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
        String idBodega = String.valueOf(modeloTabla.getValueAt(filaSeleccionada, 0));
        for (Bodega b : controlador.getBodegas()) {
            if (b.getIdBodega().equalsIgnoreCase(idBodega)) {
                txtIdBodega.setText(b.getIdBodega());
                txtIdBodega.setEditable(false);
                txtCapacidad.setText(String.valueOf(b.getCapacidad()));
                txtDireccion.setText(b.getDireccion());
                if (b.getEmpleado() != null) {
                    cmbEmpleado.setSelectedItem(b.getEmpleado());
                }
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtIdBodega.setText("");
        txtIdBodega.setEditable(true);
        txtCapacidad.setText("");
        txtDireccion.setText("");
        if (cmbEmpleado.getItemCount() > 0) {
            cmbEmpleado.setSelectedIndex(0);
        }
        tablaBodegas.clearSelection();
    }

    // Validar campos y enviar al controlador
    private void guardarBodega() {
        try {
            if (!txtIdBodega.isEditable()) {
                JOptionPane.showMessageDialog(this, "Presione 'Nuevo / Limpiar' para registrar otra bodega.");
                return;
            }

            String id = txtIdBodega.getText().trim();
            Empleado emp = (Empleado) cmbEmpleado.getSelectedItem();
            String capTexto = txtCapacidad.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (id.isEmpty() || capTexto.isEmpty() || direccion.isEmpty() || emp == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int capacidad = Integer.parseInt(capTexto);
            if (capacidad <= 0) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Bodega b = new Bodega(id, emp, capacidad, direccion);
            controlador.registrarBodega(b);

            JOptionPane.showMessageDialog(this, "Bodega registrada exitosamente.");
            limpiarFormulario();
            recargarTabla(txtBuscar.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
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

        for (Bodega b : controlador.getBodegas()) {
            boolean coincide = busqueda.isEmpty()
                    || b.getIdBodega().toLowerCase().contains(busqueda)
                    || b.getDireccion().toLowerCase().contains(busqueda);

            if (coincide) {
                String encargado = (b.getEmpleado() != null) ? b.getEmpleado().getNombre() : "Sin asignar";
                int ocupacion = controlador.obtenerOCrearInventario(b.getIdBodega()).getTotalUnidadesAlmacenadas();

                modeloTabla.addRow(new Object[]{
                        b.getIdBodega(),
                        encargado,
                        b.getCapacidad() + " uds",
                        ocupacion + " uds",
                        b.getDireccion()
                });
            }
        }
        lblContador.setText(modeloTabla.getRowCount() + " bodegas");
    }
}
