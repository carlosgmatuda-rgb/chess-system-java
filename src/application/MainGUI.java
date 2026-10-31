package application;

import gui.ChessWindow;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainGUI {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // it follows the standard look and feel
        }
        SwingUtilities.invokeLater(() -> new ChessWindow().setVisible(true));
    }
}