package gui;

import chess.ChessPiece;
import chess.Color;

public class PieceSymbols {

    private PieceSymbols() {
    }

    public static String of(ChessPiece piece) {
        boolean white = piece.getColor() == Color.WHITE;
        switch (piece.toString()) {
            case "K": return white ? "\u2654" : "\u265A";
            case "Q": return white ? "\u2655" : "\u265B";
            case "R": return white ? "\u2656" : "\u265C";
            case "B": return white ? "\u2657" : "\u265D";
            case "N": return white ? "\u2658" : "\u265E";
            case "P": return white ? "\u2659" : "\u265F";
            default:  return piece.toString();
        }
    }
}