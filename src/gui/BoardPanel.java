package gui;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.pieces.King;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

    public interface Listener {
        void onMatchUpdated();
        void onError(String message);
    }

    private final ChessMatch chessMatch;
    private final Listener listener;
    private final List<ChessPiece> captured = new ArrayList<>();
    private final SquareButton[][] squares = new SquareButton[8][8];

    private int sourceRow = -1;
    private int sourceCol = -1;
    private boolean[][] possibleMoves;

    public BoardPanel(ChessMatch chessMatch, Listener listener) {
        this.chessMatch = chessMatch;
        this.listener = listener;
        setLayout(new GridLayout(8, 8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SquareButton b = new SquareButton(i, j);
                final int row = i;
                final int col = j;
                b.addActionListener(e -> onClick(row, col));
                squares[i][j] = b;
                add(b);
            }
        }
        refresh();
    }

    public ChessMatch getChessMatch() {
        return chessMatch;
    }

    public List<ChessPiece> getCaptured() {
        return captured;
    }

    private ChessPosition toChessPosition(int row, int col) {
        return new ChessPosition((char) ('a' + col), 8 - row);
    }

    private void onClick(int i, int j) {
        if (chessMatch.getCheckMate()) {
            return;
        }
        ChessPiece clicked = chessMatch.getPieces()[i][j];
        boolean ownPiece = clicked != null && clicked.getColor() == chessMatch.getCurrentPlayer();

        try {
            if (sourceRow == -1 || ownPiece) {
                if (sourceRow == i && sourceCol == j) {
                    clearSelection();
                } else {
                    select(i, j);
                }
                listener.onMatchUpdated();
            } else {
                performMove(i, j);
            }
        } catch (ChessException e) {
            listener.onError(e.getMessage());
        }
        refresh();
    }

    private void select(int i, int j) {
        clearSelection();
        possibleMoves = chessMatch.possibleMoves(toChessPosition(i, j));
        sourceRow = i;
        sourceCol = j;
    }

    private void performMove(int i, int j) {
        ChessPosition source = toChessPosition(sourceRow, sourceCol);
        ChessPosition target = toChessPosition(i, j);

        ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
        if (capturedPiece != null) {
            captured.add(capturedPiece);
        }
        clearSelection();

        if (chessMatch.getPromoted() != null) {
            askPromotion();
        }
        refresh();
        listener.onMatchUpdated();
    }

    private void askPromotion() {
        String[] names = {"Rainha", "Torre", "Bispo", "Cavalo"};
        String[] types = {"Q", "R", "B", "N"};
        int choice = JOptionPane.showOptionDialog(this, "Escolha a peça para a promoção:",
                "Promoção", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, names, names[0]);
        if (choice < 0) {
            choice = 0; // closed the window: queen
        }
        chessMatch.replacePromotedPiece(types[choice]);
    }

    private void clearSelection() {
        sourceRow = -1;
        sourceCol = -1;
        possibleMoves = null;
    }

    public void refresh() {
        ChessPiece[][] pieces = chessMatch.getPieces();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece p = pieces[i][j];

                SquareButton.Mark mark = SquareButton.Mark.NONE;
                if (possibleMoves != null && possibleMoves[i][j]) {
                    mark = (p != null) ? SquareButton.Mark.CAPTURE : SquareButton.Mark.MOVE;
                }
                boolean selected = (i == sourceRow && j == sourceCol);
                boolean check = chessMatch.getCheck() && p instanceof King
                        && p.getColor() == chessMatch.getCurrentPlayer();

                squares[i][j].setState(p, selected, check, mark);
            }
        }
        repaint();
    }
}