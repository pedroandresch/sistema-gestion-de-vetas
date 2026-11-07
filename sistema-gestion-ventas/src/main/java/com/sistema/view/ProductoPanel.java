package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductoPanel extends JPanel {

    private SistemaController controlador;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtMarca;
    private JTextField txtPrecio;
    private JTextField txtDescripcion;

    private JButton btnGuardar;
    private JButton btnLimpiar;

    private JTextField txtBuscar;
    private JLabel lblContador;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public ProductoPanel(SistemaController controlador) {
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
        panelFormulario.add(UITheme.crearEncabezadoSeccion("Datos del Producto", null), BorderLayout.NORTH);

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtMarca = new JTextField();
        txtPrecio = new JTextField();
        txtDescripcion = new JTextField();

        UITheme.estilizarCampoTexto(txtCodigo, "Ej. PROD-005");
        UITheme.estilizarCampoTexto(txtNombre, "Nombre del producto");
        UITheme.estilizarCampoTexto(txtMarca, "Marca");
        UITheme.estilizarCampoTexto(txtPrecio, "0.00");
        UITheme.estilizarCampoTexto(txtDescripcion, "Descripción breve");

        JPanel panelCampos = new JPanel(new GridLayout(5, 1, 0, 10));
        panelCampos.setOpaque(false);
        panelCampos.add(UITheme.crearCampoFormulario("Código:", txtCodigo));
        panelCampos.add(UITheme.crearCampoFormulario("Nombre:", txtNombre));
        panelCampos.add(UITheme.crearCampoFormulario("Marca:", txtMarca));
        panelCampos.add(UITheme.crearCampoFormulario("Precio Unitario (Q):", txtPrecio));
        panelCampos.add(UITheme.crearCampoFormulario("Descripción:", txtDescripcion));

        JPanel contenedorCampos = new JPanel(new BorderLayout());
        contenedorCampos.setOpaque(false);
        contenedorCampos.add(panelCampos, BorderLayout.NORTH);
        panelFormulario.add(contenedorCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);

        btnLimpiar = new JButton("Nuevo / Limpiar");
        UITheme.estilizarBotonSecundario(btnLimpiar);

        btnGuardar = new JButton("Guardar");
        UITheme.estilizarBotonPrimario(btnGuardar, UITheme.COLOR_PRIMARIO);

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelFormulario.add(panelBotones, BorderLayout.SOUTH);

        add(panelFormulario, BorderLayout.WEST);

        // Tabla derecha con buscador de productos
        JPanel panelTabla = new JPanel(new BorderLayout(0, 12));
        UITheme.estilizarTarjeta(panelTabla);

        lblContador = new JLabel("0 registrados");

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout(0, 8));
        panelSuperiorTabla.setOpaque(false);
        panelSuperiorTabla.add(UITheme.crearEncabezadoSeccion("Catálogo de Productos", lblContador), BorderLayout.NORTH);

        JPanel barraBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraBusqueda.setOpaque(false);
        txtBuscar = new JTextField(20);
        UITheme.estilizarCampoTexto(txtBuscar, "Buscar por código, nombre o marca...");
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

        String[] columnas = {"Código", "Nombre", "Descripción", "Marca", "Precio Unitario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        UITheme.estilizarTabla(tablaProductos);
        panelTabla.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Eventos de botones y seleccion en la tabla
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarOModificarProducto());
        btnBuscar.addActionListener(e -> recargarTabla(txtBuscar.getText().trim()));
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            recargarTabla("");
        });

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProductoSeleccionado();
            }
        });
    }

    // Evento al seleccionar una fila: carga los datos en el formulario
    private void cargarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }
        String codigo = String.valueOf(modeloTabla.getValueAt(filaSeleccionada, 0));
        for (Producto p : controlador.getProductos()) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                txtCodigo.setText(p.getCodigo());
                txtNombre.setText(p.getNombre());
                txtDescripcion.setText(p.getDescripcion());
                txtMarca.setText(p.getMarca());
                txtPrecio.setText(String.valueOf(p.getPrecio()));
                txtCodigo.setEditable(false);
                btnGuardar.setText("Modificar");
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
        txtCodigo.setEditable(true);
        btnGuardar.setText("Guardar");
        tablaProductos.clearSelection();
    }

    // Validar campos y enviar al controlador
    private void guardarOModificarProducto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String marca = txtMarca.getText().trim();
            String precioTexto = txtPrecio.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código, Nombre y Precio son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(precioTexto);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto p = new Producto(codigo, nombre, descripcion, marca, precio);
            if (txtCodigo.isEditable()) {
                controlador.registrarProducto(p);
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
            } else {
                controlador.actualizarProducto(p);
                JOptionPane.showMessageDialog(this, "Producto modificado correctamente.");
            }

            limpiarFormulario();
            recargarTabla(txtBuscar.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
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

        for (Producto p : controlador.getProductos()) {
            boolean coincide = busqueda.isEmpty()
                    || p.getCodigo().toLowerCase().contains(busqueda)
                    || p.getNombre().toLowerCase().contains(busqueda)
                    || p.getMarca().toLowerCase().contains(busqueda);

            if (coincide) {
                modeloTabla.addRow(new Object[]{
                        p.getCodigo(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getMarca(),
                        UITheme.formatearMoneda(p.getPrecio())
                });
            }
        }
        lblContador.setText(modeloTabla.getRowCount() + " registrados");
    }
}
