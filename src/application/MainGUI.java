package application;

import gui.ChessWindow;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainGUI {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // segue com o look and feel padrão
        }
        SwingUtilities.invokeLater(() -> new ChessWindow().setVisible(true));
    }
}