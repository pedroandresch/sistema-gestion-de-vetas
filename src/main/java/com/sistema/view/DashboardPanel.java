package com.sistema.view;

import com.sistema.controller.SistemaController;
import com.sistema.model.Bodega;
import com.sistema.model.EstadoOrden;
import com.sistema.model.Inventario;
import com.sistema.model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private SistemaController controlador;

    private JLabel lblTotalProductos;
    private JLabel lblTotalClientes;
    private JLabel lblTotalBodegas;
    private JLabel lblTotalVentas;

    private DefaultTableModel modeloResumenBodegas;
    private DefaultTableModel modeloOrdenesRecientes;

    public DashboardPanel(SistemaController controlador) {
        this.controlador = controlador;
        iniciarComponentes();
        actualizarMetricas();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        // Encabezado del panel general
        JPanel panelEncabezado = new JPanel(new BorderLayout(4, 4));
        panelEncabezado.setOpaque(false);

        JLabel lblTitulo = new JLabel("Panel General");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(UITheme.TEXTO_OSCURO);

        JLabel lblSubtitulo = new JLabel("Resumen de inventario por bodega y órdenes de compra registradas.");
        lblSubtitulo.setFont(UITheme.FUENTE_NORMAL);
        lblSubtitulo.setForeground(UITheme.TEXTO_SECUNDARIO);

        panelEncabezado.add(lblTitulo, BorderLayout.NORTH);
        panelEncabezado.add(lblSubtitulo, BorderLayout.SOUTH);
        add(panelEncabezado, BorderLayout.NORTH);

        // Tarjetas superiores con contadores generales
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 4, 16, 16));
        panelTarjetas.setOpaque(false);

        lblTotalProductos = new JLabel("0");
        lblTotalClientes = new JLabel("0");
        lblTotalBodegas = new JLabel("0");
        lblTotalVentas = new JLabel("Q 0.00");

        panelTarjetas.add(crearTarjetaIndicador("PRODUCTOS EN CATÁLOGO", lblTotalProductos, UITheme.COLOR_PRIMARIO));
        panelTarjetas.add(crearTarjetaIndicador("CLIENTES REGISTRADOS", lblTotalClientes, new Color(13, 148, 136)));
        panelTarjetas.add(crearTarjetaIndicador("BODEGAS HABILITADAS", lblTotalBodegas, UITheme.COLOR_ADVERTENCIA));
        panelTarjetas.add(crearTarjetaIndicador("INGRESOS COMPLETADOS", lblTotalVentas, UITheme.COLOR_EXITO));

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 18, 18));
        panelTablas.setOpaque(false);

        // Tabla izquierda: ocupacion de bodegas
        JPanel tarjetaBodegas = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(tarjetaBodegas);
        tarjetaBodegas.add(UITheme.crearEncabezadoSeccion("Ocupación de Bodegas", null), BorderLayout.NORTH);

        String[] columnasBodega = {"Bodega", "Encargado", "Ocupado", "Disponible"};
        modeloResumenBodegas = new DefaultTableModel(columnasBodega, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tablaBodegas = new JTable(modeloResumenBodegas);
        UITheme.estilizarTabla(tablaBodegas);
        tarjetaBodegas.add(new JScrollPane(tablaBodegas), BorderLayout.CENTER);

        // Tabla derecha: ordenes recientes
        JPanel tarjetaOrdenes = new JPanel(new BorderLayout(10, 10));
        UITheme.estilizarTarjeta(tarjetaOrdenes);
        tarjetaOrdenes.add(UITheme.crearEncabezadoSeccion("Órdenes de Compra Recientes", null), BorderLayout.NORTH);

        String[] columnasOrdenes = {"No. Orden", "Cliente", "Total", "Estado"};
        modeloOrdenesRecientes = new DefaultTableModel(columnasOrdenes, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tablaOrdenes = new JTable(modeloOrdenesRecientes);
        UITheme.estilizarTabla(tablaOrdenes);
        tarjetaOrdenes.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        panelTablas.add(tarjetaBodegas);
        panelTablas.add(tarjetaOrdenes);

        JPanel panelCentral = new JPanel(new BorderLayout(18, 18));
        panelCentral.setOpaque(false);
        panelCentral.add(panelTarjetas, BorderLayout.NORTH);
        panelCentral.add(panelTablas, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaIndicador(String titulo, JLabel lblValor, Color colorBordeSuperior) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 8));
        tarjeta.setBackground(UITheme.FONDO_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.COLOR_BORDE, 1, true),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(3, 0, 0, 0, colorBordeSuperior),
                        BorderFactory.createEmptyBorder(14, 16, 14, 16)
                )
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitulo.setForeground(UITheme.TEXTO_SECUNDARIO);

        lblValor.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblValor.setForeground(UITheme.TEXTO_OSCURO);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        return tarjeta;
    }

    // Calcular totales y llenar las tablas del panel general
    public void actualizarMetricas() {
        lblTotalProductos.setText(String.valueOf(controlador.getProductos().size()));
        lblTotalClientes.setText(String.valueOf(controlador.getClientes().size()));

        List<Bodega> bodegas = controlador.getBodegas();
        lblTotalBodegas.setText(String.valueOf(bodegas.size()));

        modeloResumenBodegas.setRowCount(0);
        for (Bodega b : bodegas) {
            Inventario inv = controlador.obtenerOCrearInventario(b.getIdBodega());
            int usado = inv.getTotalUnidadesAlmacenadas();
            int libre = Math.max(0, b.getCapacidad() - usado);
            String encargado = b.getEmpleado() != null ? b.getEmpleado().getNombre() : "Sin asignar";

            modeloResumenBodegas.addRow(new Object[]{
                    b.getIdBodega(),
                    encargado,
                    usado + " / " + b.getCapacidad(),
                    libre + " uds"
            });
        }

        List<OrdenCompra> ordenes = controlador.getOrdenes();
        double totalVendido = 0;
        modeloOrdenesRecientes.setRowCount(0);

        for (int i = ordenes.size() - 1; i >= 0; i--) {
            OrdenCompra ord = ordenes.get(i);
            if (ord.getEstado() == EstadoOrden.COMPLETADA) {
                totalVendido += ord.getTotal();
            }
            modeloOrdenesRecientes.addRow(new Object[]{
                    ord.getIdOrden(),
                    ord.getCliente().getNombre(),
                    UITheme.formatearMoneda(ord.getTotal()),
                    ord.getEstado()
            });
        }

        lblTotalVentas.setText(UITheme.formatearMoneda(totalVendido));
    }
}
