package de.fida.cwtool.util;

import de.fida.cwtool.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class CompCreator {

    public static JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 100, 20);

        return label;
    }

    public static JButton createButton(String text, String tip, int x, int y) {
        JButton button = new JButton(text);
        button.setToolTipText(tip);
        button.setBounds(x, y, 95, 20);

        return button;
    }

    public static JButton createButton(String text, String tip, int x, int y, int width) {
        JButton button = new JButton(text);
        button.setToolTipText(tip);
        button.setBounds(x, y, width, 20);

        return button;
    }

    public static JTabbedPane createTabbedPane(int x, int y, int width, int height, HashMap<String, JComponent> tabs) {
        JTabbedPane pane = new JTabbedPane();
        pane.setBounds(x, y, width, height);

        for (Map.Entry<String, JComponent> entry : tabs.entrySet()) {
            String key = entry.getKey();
            JComponent value = entry.getValue();

            pane.addTab(key, value);
        }

        pane.setBorder(new TitledBorder(""));

        return pane;
    }

    public static JComboBox<String> createPlayerSelection(Clan clan, int x, int y) {
        JComboBox<String> box = new JComboBox<>(new SortedComboBoxModel<>());
        box.setBounds(x, y, 118, 20);

        box.addItem("");

        for(Player p : clan.getMembers()) {
            box.addItem(p.getName());
        }

        return box;
    }

    public static JComboBox<String> createTeamSelection(Clan clan, int x, int y) {
        JComboBox<String> box = new JComboBox<>(new SortedComboBoxModel<>());
        box.setBounds(x, y, 118, 20);

        box.addItem("");

        for(PlayerCombination combination : clan.getPlayerCombinations()) {
            box.addItem(combination.getName());
        }

        return box;
    }

    public static JComboBox<String> createRatingSelection(int x, int y) {
        JComboBox<String> box = new JComboBox<>();
        box.setBounds(x, y, 118, 20);

        //box.addItem("");

        for(Rating rating : Rating.values()) {
            box.addItem(rating.toString());
        }

        return box;
    }

    public static JComboBox<String> createBox(SortedComboBoxModel model, int x, int y) {
        JComboBox<String> box = new JComboBox<>(model);
        box.setBounds(x, y, 118, 20);

        return box;
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height, int fontSize, boolean outline) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(x, y, width, height);
        label.setFont(label.getFont().deriveFont((float) fontSize).deriveFont(Font.BOLD));

        if(outline) {
            label.setBorder(new LineBorder(Color.BLACK));
        }

        return label;
    }
}
