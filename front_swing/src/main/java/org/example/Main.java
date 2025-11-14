package org.example;

import org.example.ui.EventListFrame;

import javax.swing.*;


public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        SwingUtilities.invokeLater(() -> {
            new EventListFrame().setVisible(true);
        });
    }
}