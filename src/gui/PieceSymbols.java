package gui;

import chess.ChessPiece;
import chess.Color;

public class PieceSymbols {

    private PieceSymbols() {
    }

    // Outlined symbols for white pieces and filled-in symbols for black pieces (used for captured pieces)
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

    // Filled-in symbols for both colors (used on the game board, which paints over them)
    public static String solid(ChessPiece piece) {
        switch (piece.toString()) {
            case "K": return "\u265A";
            case "Q": return "\u265B";
            case "R": return "\u265C";
            case "B": return "\u265D";
            case "N": return "\u265E";
            case "P": return "\u265F";
            default:  return piece.toString();
        }
    }
}