package com.sistema.view;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UITheme {

    // Colores para el menu lateral y el fondo general
    public static final Color FONDO_MENU = new Color(15, 23, 42);
    public static final Color MENU_ACTIVO = new Color(51, 65, 85);
    public static final Color TEXTO_MENU = new Color(226, 232, 240);
    public static final Color TEXTO_GRIS_MENU = new Color(148, 163, 184);

    public static final Color FONDO_PRINCIPAL = new Color(244, 247, 251);
    public static final Color FONDO_TARJETA = Color.WHITE;
    public static final Color COLOR_BORDE = new Color(226, 232, 240);

    // Colores de botones y textos
    public static final Color COLOR_PRIMARIO = new Color(37, 99, 235);
    public static final Color COLOR_EXITO = new Color(16, 185, 129);
    public static final Color COLOR_PELIGRO = new Color(220, 38, 38);
    public static final Color COLOR_ADVERTENCIA = new Color(217, 119, 6);
    public static final Color TEXTO_OSCURO = new Color(15, 23, 42);
    public static final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);

    // Fuentes estandar para la interfaz
    public static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FUENTE_NEGRITA = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FUENTE_NORMAL = new Font("SansSerif", Font.PLAIN, 12);

    // Formatear un valor numerico en Quetzales
    public static String formatearMoneda(double monto) {
        return String.format("Q %.2f", monto);
    }

    // Dar estilo de tarjeta blanca con borde a un panel
    public static void estilizarTarjeta(JComponent componente) {
        componente.setBackground(FONDO_TARJETA);
        componente.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
    }

    public static void estilizarCampoTexto(JTextField campo, String textoGuia) {
        campo.putClientProperty(FlatClientProperties.STYLE, "arc: 8; margin: 5,10,5,10");
        if (textoGuia != null && !textoGuia.isEmpty()) {
            campo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, textoGuia);
        }
        campo.setFont(FUENTE_NORMAL);
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.putClientProperty(FlatClientProperties.STYLE, "arc: 8; padding: 3,6,3,6");
        combo.setFont(FUENTE_NORMAL);
    }

    public static void estilizarBotonPrimario(JButton boton, Color colorFondo) {
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(FUENTE_NEGRITA);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.putClientProperty(FlatClientProperties.STYLE, "arc: 8; borderWidth: 0; margin: 7,16,7,16");
    }

    public static void estilizarBotonSecundario(JButton boton) {
        boton.setBackground(new Color(241, 245, 249));
        boton.setForeground(TEXTO_OSCURO);
        boton.setFont(FUENTE_NEGRITA);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.putClientProperty(FlatClientProperties.STYLE, "arc: 8; borderWidth: 1; borderColor: #CBD5E1; margin: 7,14,7,14");
    }

    // Configurar el diseno de una tabla
    public static void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(241, 245, 249));
        tabla.setFont(FUENTE_NORMAL);
        tabla.setSelectionBackground(new Color(219, 234, 254));
        tabla.setSelectionForeground(TEXTO_OSCURO);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(FUENTE_NEGRITA);
        encabezado.setBackground(new Color(248, 250, 252));
        encabezado.setForeground(new Color(71, 85, 105));
        encabezado.setReorderingAllowed(false);
        encabezado.setPreferredSize(new Dimension(encabezado.getWidth(), 34));
    }

    // Crear encabezado para secciones con titulo y etiqueta de conteo
    public static JPanel crearEncabezadoSeccion(String titulo, JLabel lblContador) {
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setOpaque(false);
        panelEncabezado.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_SUBTITULO);
        lblTitulo.setForeground(TEXTO_OSCURO);
        panelEncabezado.add(lblTitulo, BorderLayout.WEST);

        if (lblContador != null) {
            lblContador.setFont(FUENTE_NEGRITA);
            lblContador.setForeground(TEXTO_SECUNDARIO);
            panelEncabezado.add(lblContador, BorderLayout.EAST);
        }
        return panelEncabezado;
    }

    // Crear un bloque con etiqueta arriba y campo de entrada abajo
    public static JPanel crearCampoFormulario(String textoEtiqueta, JComponent campoEntrada) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setFont(FUENTE_NEGRITA);
        etiqueta.setForeground(TEXTO_OSCURO);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(campoEntrada, BorderLayout.CENTER);
        return panel;
    }
}
