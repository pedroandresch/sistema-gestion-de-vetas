package com.sistema;

import com.formdev.flatlaf.FlatLightLaf;
import com.sistema.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el tema visual: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame ventana = new MainFrame();
            ventana.setVisible(true);
        });
    }
}
