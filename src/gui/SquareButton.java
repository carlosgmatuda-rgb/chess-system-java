package gui;

import chess.ChessPiece;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SquareButton extends JButton {

    public enum Mark { NONE, MOVE, CAPTURE }

    private static final Color LIGHT = new Color(0xEEEED2);
    private static final Color DARK = new Color(0x769656);
    private static final Color SELECTED = new Color(246, 246, 105, 190);

    private static final Font PIECE_FONT = pickPieceFont();

    private final boolean lightSquare;
    private final String rankLabel;
    private final String fileLabel;

    private ChessPiece piece;
    private boolean selected;
    private boolean check;
    private Mark mark = Mark.NONE;
    private boolean hover;

    public SquareButton(int row, int col) {
        this.lightSquare = (row + col) % 2 == 0;
        this.rankLabel = (col == 0) ? String.valueOf(8 - row) : null;
        this.fileLabel = (row == 7) ? String.valueOf((char) ('a' + col)) : null;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    private static Font pickPieceFont() {
        Set<String> families = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        String[] candidates = {"Segoe UI Symbol", "Noto Sans Symbols 2", "DejaVu Sans", "Arial Unicode MS"};
        for (String name : candidates) {
            if (families.contains(name)) {
                return new Font(name, Font.PLAIN, 1);
            }
        }
        return new Font(Font.DIALOG, Font.PLAIN, 1);
    }

    public void setState(ChessPiece piece, boolean selected, boolean check, Mark mark) {
        this.piece = piece;
        this.selected = selected;
        this.check = check;
        this.mark = mark;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int w = getWidth();
        int h = getHeight();

        // fundo da casa
        g2.setColor(lightSquare ? LIGHT : DARK);
        g2.fillRect(0, 0, w, h);

        if (selected) {
            g2.setColor(SELECTED);
            g2.fillRect(0, 0, w, h);
        }

        if (check) {
            g2.setPaint(new RadialGradientPaint(
                    new Point2D.Float(w / 2f, h / 2f), w * 0.7f,
                    new float[]{0f, 0.7f, 1f},
                    new Color[]{new Color(255, 40, 40, 235), new Color(230, 30, 30, 150), new Color(200, 0, 0, 0)}));
            g2.fillRect(0, 0, w, h);
        }

        if (hover) {
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRect(0, 0, w, h);
        }

        // coordenadas
        Font labelFont = new Font("SansSerif", Font.BOLD, Math.max(9, (int) (h * 0.17)));
        g2.setFont(labelFont);
        g2.setColor(lightSquare ? DARK : LIGHT);
        FontMetrics fm = g2.getFontMetrics();
        if (rankLabel != null) {
            g2.drawString(rankLabel, 4, fm.getAscent() + 2);
        }
        if (fileLabel != null) {
            g2.drawString(fileLabel, w - fm.stringWidth(fileLabel) - 4, h - 5);
        }

        // marcação de jogadas possíveis
        if (mark == Mark.MOVE) {
            int d = (int) (w * 0.3);
            g2.setColor(new Color(0, 0, 0, 45));
            g2.fillOval((w - d) / 2, (h - d) / 2, d, d);
        } else if (mark == Mark.CAPTURE) {
            float sw = Math.max(3f, w * 0.09f);
            g2.setStroke(new BasicStroke(sw));
            g2.setColor(new Color(0, 0, 0, 55));
            int inset = (int) (sw / 2) + 1;
            g2.drawOval(inset, inset, w - 2 * inset, h - 2 * inset);
        }

        // peça (tamanho proporcional à casa)
        if (piece != null) {
            Font f = PIECE_FONT.deriveFont((float) (Math.min(w, h) * 0.78));
            GlyphVector gv = f.createGlyphVector(g2.getFontRenderContext(), PieceSymbols.solid(piece));
            Rectangle2D b = gv.getVisualBounds();
            float tx = (float) ((w - b.getWidth()) / 2 - b.getX());
            float ty = (float) ((h - b.getHeight()) / 2 - b.getY());
            Shape outline = gv.getOutline(tx, ty);

            boolean white = piece.getColor() == chess.Color.WHITE;
            float sw = Math.max(1.5f, w * 0.03f);
            g2.setStroke(new BasicStroke(sw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(white ? new Color(0x1C1C1C) : new Color(255, 255, 255, 170));
            g2.draw(outline);
            g2.setColor(white ? Theme.PIECE_WHITE : Theme.PIECE_BLACK);
            g2.fill(outline);
        }

        g2.dispose();
    }
}