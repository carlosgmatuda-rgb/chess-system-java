package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

// Keeps the component square and centered, regardless of the window size
public class SquareLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Insets in = parent.getInsets();
        return new Dimension(560 + in.left + in.right, 560 + in.top + in.bottom);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Insets in = parent.getInsets();
        return new Dimension(320 + in.left + in.right, 320 + in.top + in.bottom);
    }

    @Override
    public void layoutContainer(Container parent) {
        if (parent.getComponentCount() == 0) {
            return;
        }
        Insets in = parent.getInsets();
        int w = parent.getWidth() - in.left - in.right;
        int h = parent.getHeight() - in.top - in.bottom;

        int size = Math.min(w, h);
        size = size - (size % 8); // casas com tamanho inteiro, sem frestas

        int x = in.left + (w - size) / 2;
        int y = in.top + (h - size) / 2;
        parent.getComponent(0).setBounds(x, y, size, size);
    }
}