package org.aps;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class GerenciamentoUsuariosApp {

    private InterfaceUsuario interfaceUsuario;

    public GerenciamentoUsuariosApp() {
        interfaceUsuario = new InterfaceUsuario(this);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.invokeLater(GerenciamentoUsuariosApp::new);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
