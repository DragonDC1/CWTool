package de.fida.cwtool.util;

import de.fida.cwtool.CW_Build;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class IMGLoader {

    public static final int SIZE = 64;

    public static BufferedImage loadImage(CW_Build build) {
        try {
            BufferedImage image = ImageIO.read(new File("images/" + build + ".png"));

            BufferedImage output = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
            output.getGraphics().drawImage(image, 0, 0, SIZE, SIZE, null);
            output.getGraphics().dispose();

            return output;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "<html>Es wurde kein Bild mit dem Namen <b>" + build + "</b> gefunden!</html>");
            System.exit(1);
        }

        return null;
    }
}
