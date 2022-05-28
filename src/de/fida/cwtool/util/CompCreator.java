package de.fida.cwtool.util;

import javax.swing.*;

public class CompCreator {

    public static JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 100, 20);

        return label;
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height, int fontSize) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(label.getFont().deriveFont((float) fontSize));

        return label;
    }
}
