package com.sistema.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.sistema.controller.SistemaController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private SistemaController controlador;

    private CardLayout disenoTarjetas;
    private JPanel panelContenido;
    private List<JButton> botonesMenu;

    private DashboardPanel panelDashboard;
    private ProductoPanel panelProductos;
    private ClientePanel panelClientes;
    private EmpleadoPanel panelEmpleados;
    private BodegaPanel panelBodegas;
    private InventarioPanel panelInventario;
    private OrdenCompraPanel panelOrdenes;

    public MainFrame() {
        this.controlador = new SistemaController();
        this.botonesMenu = new ArrayList<>();
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        // Configuracion basica de la ventana principal
        setTitle("Sistema de Gestión de Inventario y Ventas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 780);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(UITheme.FONDO_PRINCIPAL);

        // Configuracion del panel lateral de navegacion
        JPanel menuLateral = new JPanel(new BorderLayout());
        menuLateral.setPreferredSize(new Dimension(240, getHeight()));
        menuLateral.setBackground(UITheme.FONDO_MENU);
        menuLateral.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        JPanel panelMarca = new JPanel(new BorderLayout(8, 4));
        panelMarca.setOpaque(false);
        panelMarca.setBorder(BorderFactory.createEmptyBorder(0, 8, 24, 8));

        JLabel lblTituloMarca = new JLabel("INVENTARIO Y VENTAS");
        lblTituloMarca.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloMarca.setForeground(Color.WHITE);

        JLabel lblSubtituloMarca = new JLabel("Control y Facturación");
        lblSubtituloMarca.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSubtituloMarca.setForeground(UITheme.TEXTO_GRIS_MENU);

        panelMarca.add(lblTituloMarca, BorderLayout.NORTH);
        panelMarca.add(lblSubtituloMarca, BorderLayout.SOUTH);
        menuLateral.add(panelMarca, BorderLayout.NORTH);

        // Botones para cambiar entre los modulos del sistema
        JPanel listaBotones = new JPanel(new GridLayout(7, 1, 0, 6));
        listaBotones.setOpaque(false);

        JButton btnDash = crearBotonMenu("Panel General", "DASHBOARD");
        JButton btnProd = crearBotonMenu("Productos", "PRODUCTOS");
        JButton btnCli  = crearBotonMenu("Clientes", "CLIENTES");
        JButton btnEmp  = crearBotonMenu("Empleados", "EMPLEADOS");
        JButton btnBod  = crearBotonMenu("Bodegas", "BODEGAS");
        JButton btnInv  = crearBotonMenu("Inventario", "INVENTARIO");
        JButton btnOrd  = crearBotonMenu("Órdenes de Compra", "ORDENES");

        listaBotones.add(btnDash);
        listaBotones.add(btnProd);
        listaBotones.add(btnCli);
        listaBotones.add(btnEmp);
        listaBotones.add(btnBod);
        listaBotones.add(btnInv);
        listaBotones.add(btnOrd);

        menuLateral.add(listaBotones, BorderLayout.CENTER);
        panelPrincipal.add(menuLateral, BorderLayout.WEST);

        // Contenedor central con CardLayout para mostrar cada modulo
        disenoTarjetas = new CardLayout();
        panelContenido = new JPanel(disenoTarjetas);
        panelContenido.setBackground(UITheme.FONDO_PRINCIPAL);

        panelDashboard = new DashboardPanel(controlador);
        panelProductos = new ProductoPanel(controlador);
        panelClientes = new ClientePanel(controlador);
        panelEmpleados = new EmpleadoPanel(controlador);
        panelBodegas = new BodegaPanel(controlador);
        panelInventario = new InventarioPanel(controlador);
        panelOrdenes = new OrdenCompraPanel(controlador);

        panelContenido.add(panelDashboard, "DASHBOARD");
        panelContenido.add(panelProductos, "PRODUCTOS");
        panelContenido.add(panelClientes, "CLIENTES");
        panelContenido.add(panelEmpleados, "EMPLEADOS");
        panelContenido.add(panelBodegas, "BODEGAS");
        panelContenido.add(panelInventario, "INVENTARIO");
        panelContenido.add(panelOrdenes, "ORDENES");

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        add(panelPrincipal);

        marcarBotonActivo(btnDash);
    }

    private JButton crearBotonMenu(String texto, String nombreVista) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(UITheme.TEXTO_MENU);
        boton.setBackground(UITheme.FONDO_MENU);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.putClientProperty(FlatClientProperties.STYLE, "arc: 8; borderWidth: 0");
        boton.setBorder(BorderFactory.createEmptyBorder(8, 17, 8, 14));

        boton.addActionListener(e -> {
            marcarBotonActivo(boton);
            actualizarVistaSeleccionada(nombreVista);
            disenoTarjetas.show(panelContenido, nombreVista);
        });

        botonesMenu.add(boton);
        return boton;
    }

    // Resaltar el boton del modulo que esta activo actualmente
    private void marcarBotonActivo(JButton botonActivo) {
        for (JButton b : botonesMenu) {
            if (b == botonActivo) {
                b.setBackground(UITheme.MENU_ACTIVO);
                b.setForeground(Color.WHITE);
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 3, 0, 0, UITheme.COLOR_PRIMARIO),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            } else {
                b.setBackground(UITheme.FONDO_MENU);
                b.setForeground(UITheme.TEXTO_MENU);
                b.setBorder(BorderFactory.createEmptyBorder(8, 17, 8, 14));
            }
        }
    }

    // Recargar las tablas y combos cuando el usuario cambia de modulo
    private void actualizarVistaSeleccionada(String nombreVista) {
        if (nombreVista.equals("DASHBOARD")) {
            panelDashboard.actualizarMetricas();
        } else if (nombreVista.equals("PRODUCTOS")) {
            panelProductos.recargarTabla();
        } else if (nombreVista.equals("CLIENTES")) {
            panelClientes.recargarTabla();
        } else if (nombreVista.equals("EMPLEADOS")) {
            panelEmpleados.recargarTabla();
        } else if (nombreVista.equals("BODEGAS")) {
            panelBodegas.recargarEmpleadosCombo();
            panelBodegas.recargarTabla();
        } else if (nombreVista.equals("INVENTARIO")) {
            panelInventario.recargarCombos();
            panelInventario.recargarTabla();
        } else if (nombreVista.equals("ORDENES")) {
            panelOrdenes.recargarCombos();
            panelOrdenes.recargarTablaOrdenes();
        }
    }
}
