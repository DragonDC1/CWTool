package de.fida.cwtool.gui;

import de.fida.cwtool.CW_Build;
import de.fida.cwtool.Clan;
import de.fida.cwtool.util.CompCreator;
import de.fida.cwtool.util.IMGLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Window {

    private JFrame frame;

    private final int WIDTH = 900;
    private final int HEIGHT = 600;

    private final String VERSION = "V1.0";
    private final String TITLE = "CW Tool by Finn & Daniel " + VERSION;

    private JLabel labelClan;

    private final HashMap<String, BufferedImage> IMAGES = new HashMap<>();

    public void init(Clan clan) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        loadImages();

        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        labelClan = CompCreator.createLabel(clan.toString(), 0, 0, 100, 50, 25);

        frame.add(labelClan);

        frame.setVisible(true);
    }

    private void loadImages() {
        for (CW_Build build : CW_Build.values()) {
            BufferedImage image = IMGLoader.loadImage(build);

            IMAGES.put(build.toString(), image);
        }
    }

    public BufferedImage getImage(CW_Build build) {
        for (Map.Entry<String, BufferedImage> entry : IMAGES.entrySet()) {
            String key = entry.getKey();
            BufferedImage value = entry.getValue();

            if(key == build.toString()) return value;
        }

        return null;
    }
}
