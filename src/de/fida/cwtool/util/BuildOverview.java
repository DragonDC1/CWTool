package de.fida.cwtool.util;

import de.fida.cwtool.Build;
import de.fida.cwtool.Player;
import de.fida.cwtool.gui.Window;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Finnegan Kaiser | 30.05.2022 (10:06)
 **/

public class BuildOverview extends JPanel {

    private Player player;

    private Window window;

    public BuildOverview(Window window) {
        this.window = window;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;
        RenderingHints rh = g.getRenderingHints();

        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        rh.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setRenderingHints(rh);

        if (player != null) {
            g.setColor(Color.BLACK);
            //g.drawRect(35, 0, 200, 25);

            for (int i = 0; i < player.getBuilds().size(); i++) {
                Build build = player.getBuilds().get(i);

                BufferedImage image = window.getImage(build.getArt());

                g.drawImage(image, (i + 1) * 40, 0, 38, 38, null);
            }
        }

        repaint();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
