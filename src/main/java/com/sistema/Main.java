package com.sistema;

import com.formdev.flatlaf.FlatLightLaf;
import com.sistema.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Inicializar Look & Feel moderno FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Throwable e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        // Lanzar interfaz gráfica en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
