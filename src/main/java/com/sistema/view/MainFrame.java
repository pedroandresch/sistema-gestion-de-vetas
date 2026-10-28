package com.sistema.view;

import com.sistema.controller.SistemaController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final SistemaController controller;

    private ProductoPanel productoPanel;
    private ClientePanel clientePanel;
    private EmpleadoPanel empleadoPanel;
    private BodegaPanel bodegaPanel;
    private InventarioPanel inventarioPanel;
    private OrdenCompraPanel ordenCompraPanel;

    public MainFrame() {
        this.controller = new SistemaController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Inventario y Ventas - POO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);

        // Encabezado
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 32, 44));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titleLabel = new JLabel("Sistema de Gestión de Inventario y Ventas");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel subLabel = new JLabel("Arquitectura POO • Persistencia JSON • Swing");
        subLabel.setForeground(new Color(176, 190, 197));
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(subLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Pestañas de módulos
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        productoPanel = new ProductoPanel(controller);
        clientePanel = new ClientePanel(controller);
        empleadoPanel = new EmpleadoPanel(controller);
        bodegaPanel = new BodegaPanel(controller);
        inventarioPanel = new InventarioPanel(controller);
        ordenCompraPanel = new OrdenCompraPanel(controller);

        tabbedPane.addTab("📦 Productos", productoPanel);
        tabbedPane.addTab("👥 Clientes", clientePanel);
        tabbedPane.addTab("👔 Empleados", empleadoPanel);
        tabbedPane.addTab("🏢 Bodegas", bodegaPanel);
        tabbedPane.addTab("📊 Inventario", inventarioPanel);
        tabbedPane.addTab("🛒 Órdenes de Compra", ordenCompraPanel);

        // Al cambiar de pestaña, refrescar datos dependientes
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case 3 -> { // Bodegas
                    bodegaPanel.recargarEmpleadosCombo();
                    bodegaPanel.recargarTabla();
                }
                case 4 -> { // Inventario
                    inventarioPanel.recargarCombos();
                    inventarioPanel.recargarTabla();
                }
                case 5 -> { // Órdenes
                    ordenCompraPanel.recargarCombos();
                    ordenCompraPanel.recargarTablaOrdenes();
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado inferior
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        JLabel lblStatus = new JLabel("Sistema listo • Datos sincronizados con carpeta /data");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setForeground(Color.GRAY);
        statusBar.add(lblStatus);

        add(statusBar, BorderLayout.SOUTH);
    }
}
