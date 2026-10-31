package gui;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

public class ChessWindow extends JFrame implements BoardPanel.Listener {

    private BoardPanel boardPanel;
    private JPanel boardHolder;

    private JLabel turnLabel;
    private JLabel playerLabel;
    private JLabel noticeLabel;
    private JLabel whiteCapturedLabel;
    private JLabel blackCapturedLabel;

    public ChessWindow() {
        super("Xadrez");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        boardHolder = new JPanel(new SquareLayout());
        boardHolder.setBackground(Theme.BG);
        boardHolder.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 8));
        add(boardHolder, BorderLayout.CENTER);
        add(buildSidebar(), BorderLayout.EAST);

        startNewGame();

        setMinimumSize(new Dimension(720, 580));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(24, 8, 24, 24));
        side.setPreferredSize(new Dimension(290, 0));

        JLabel title = label("Xadrez", Theme.TITLE, Theme.TEXT);
        JLabel subtitle = label("Java · Swing", Theme.SMALL, Theme.MUTED);

        // card de status
        Theme.Card status = new Theme.Card(null);
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        turnLabel = label("TURNO 1", Theme.SMALL_BOLD, Theme.MUTED);
        playerLabel = label("Vez das Brancas", Theme.HEADING, Theme.TEXT);
        playerLabel.setIconTextGap(10);
        noticeLabel = label(" ", Theme.BODY, Theme.DANGER);
        status.add(turnLabel);
        status.add(Box.createVerticalStrut(6));
        status.add(playerLabel);
        status.add(Box.createVerticalStrut(6));
        status.add(noticeLabel);

        // cards piece captured
        whiteCapturedLabel = label(" ", new Font("Dialog", Font.PLAIN, 22), Theme.TEXT);
        blackCapturedLabel = label(" ", new Font("Dialog", Font.PLAIN, 22), Theme.TEXT);
        Theme.Card whiteCard = capturedCard("Brancas capturadas", whiteCapturedLabel);
        Theme.Card blackCard = capturedCard("Pretas capturadas", blackCapturedLabel);

        Theme.FlatButton newGame = new Theme.FlatButton("Novo jogo", Theme.ACCENT);
        newGame.setAlignmentX(Component.LEFT_ALIGNMENT);
        newGame.addActionListener(e -> startNewGame());

        side.add(title);
        side.add(subtitle);
        side.add(Box.createVerticalStrut(20));
        side.add(status);
        side.add(Box.createVerticalStrut(12));
        side.add(whiteCard);
        side.add(Box.createVerticalStrut(12));
        side.add(blackCard);
        side.add(Box.createVerticalGlue());
        side.add(newGame);
        return side;
    }

    private Theme.Card capturedCard(String title, JLabel content) {
        Theme.Card card = new Theme.Card(null);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(label(title, Theme.SMALL_BOLD, Theme.MUTED));
        card.add(Box.createVerticalStrut(6));
        card.add(content);
        return card;
    }

    private static JLabel label(String text, Font font, java.awt.Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void startNewGame() {
        boardHolder.removeAll();
        boardPanel = new BoardPanel(new ChessMatch(), this);
        boardHolder.add(boardPanel);
        updateLabels();
        boardHolder.revalidate();
        boardHolder.repaint();
    }

    private void setNotice(String text, java.awt.Color color) {
        if (text == null || text.isBlank()) {
            noticeLabel.setText(" ");
        } else {
            noticeLabel.setText("<html><div style='width:200px'>" + text + "</div></html>");
        }
        noticeLabel.setForeground(color);
    }

    private void updateLabels() {
        ChessMatch match = boardPanel.getChessMatch();
        boolean white = match.getCurrentPlayer() == Color.WHITE;
        String player = white ? "Brancas" : "Pretas";

        turnLabel.setText("TURNO " + match.getTurn());
        playerLabel.setIcon(Theme.dot(white ? Theme.PIECE_WHITE : Theme.PIECE_BLACK, 16));

        if (match.getCheckMate()) {
            playerLabel.setText("Vencedor: " + player);
            setNotice("XEQUE-MATE!", Theme.ACCENT);
        } else {
            playerLabel.setText("Vez das " + player);
            if (match.getCheck()) {
                setNotice("XEQUE!", Theme.DANGER);
            } else {
                setNotice(" ", Theme.DANGER);
            }
        }

        whiteCapturedLabel.setText(capturedHtml(Color.WHITE));
        blackCapturedLabel.setText(capturedHtml(Color.BLACK));
    }

    private String capturedHtml(Color color) {
        List<ChessPiece> list = boardPanel.getCaptured();
        StringBuilder sb = new StringBuilder("<html><div style='width:200px'>");
        boolean any = false;
        for (ChessPiece p : list) {
            if (p.getColor() == color) {
                sb.append(PieceSymbols.of(p)).append(' ');
                any = true;
            }
        }
        if (!any) {
            sb.append("&nbsp;");
        }
        sb.append("</div></html>");
        return sb.toString();
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
        setNotice(message, Theme.DANGER);
    }
}