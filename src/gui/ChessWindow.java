package gui;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class ChessWindow extends JFrame implements BoardPanel.Listener {

    private BoardPanel boardPanel;
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel whiteCaptured = new JLabel(" ");
    private final JLabel blackCaptured = new JLabel(" ");

    public ChessWindow() {
        super("Xadrez");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(statusLabel, BorderLayout.NORTH);

        whiteCaptured.setFont(new Font("Dialog", Font.PLAIN, 22));
        blackCaptured.setFont(new Font("Dialog", Font.PLAIN, 22));

        JPanel south = new JPanel(new GridLayout(3, 1));
        south.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        south.add(whiteCaptured);
        south.add(blackCaptured);
        JButton newGame = new JButton("Novo jogo");
        newGame.addActionListener(e -> startNewGame());
        south.add(newGame);
        add(south, BorderLayout.SOUTH);

        startNewGame();

        setPreferredSize(new Dimension(660, 790));
        pack();
        setLocationRelativeTo(null);
    }

    private void startNewGame() {
        if (boardPanel != null) {
            remove(boardPanel);
        }
        boardPanel = new BoardPanel(new ChessMatch(), this);
        add(boardPanel, BorderLayout.CENTER);
        updateLabels();
        revalidate();
        repaint();
    }

    private void updateLabels() {
        ChessMatch match = boardPanel.getChessMatch();
        String player = match.getCurrentPlayer() == Color.WHITE ? "Brancas" : "Pretas";

        if (match.getCheckMate()) {
            statusLabel.setText("XEQUE-MATE! Vencedor: " + player);
        } else {
            statusLabel.setText("Turno " + match.getTurn() + " - Vez das " + player
                    + (match.getCheck() ? " - XEQUE!" : ""));
        }

        StringBuilder white = new StringBuilder("Brancas capturadas: ");
        StringBuilder black = new StringBuilder("Pretas capturadas: ");
        for (ChessPiece p : boardPanel.getCaptured()) {
            if (p.getColor() == Color.WHITE) {
                white.append(PieceSymbols.of(p)).append(' ');
            } else {
                black.append(PieceSymbols.of(p)).append(' ');
            }
        }
        whiteCaptured.setText(white.toString());
        blackCaptured.setText(black.toString());
    }

    @Override
    public void onMatchUpdated() {
        updateLabels();
        ChessMatch match = boardPanel.getChessMatch();
        if (match.getCheckMate()) {
            String winner = match.getCurrentPlayer() == Color.WHITE ? "Brancas" : "Pretas";
            JOptionPane.showMessageDialog(this, "Xeque-mate! Vencedor: " + winner);
        }
    }

    @Override
    public void onError(String message) {
        statusLabel.setText(message);
    }
}