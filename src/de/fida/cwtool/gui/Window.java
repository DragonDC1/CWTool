package de.fida.cwtool.gui;

import de.fida.cwtool.Build;
import de.fida.cwtool.CW_Build;
import de.fida.cwtool.Clan;
import de.fida.cwtool.Player;
import de.fida.cwtool.util.CompCreator;
import de.fida.cwtool.util.IMGLoader;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Window {

    private JFrame frame;

    private final int WIDTH = 900;
    private final int HEIGHT = 600;

    private final String VERSION = "V1.0";
    private final String TITLE = "CW Tool by Finn & Daniel " + VERSION;

    public Clan clan;

    private JLabel labelClan;
    private JLabel labelPlayer;

    private JButton buttonAddPlayer;
    private JButton buttonRemovePlayer;

    private JComboBox<String> boxPlayer;

    private JTabbedPane paneOverview;
    private JScrollPane paneScroll;

    private JButton buttonEditMode;

    private boolean editMode;

    public final String UNCHECKED = "☐";
    public final String CHECKED = "☑";

    private final int ROW_SIZE = 50;

    private final Color EDIT_ACTIVE = new Color(0x216421);
    private final Color EDIT_INACTIVE = new Color(0x963032);

    private final HashMap<String, BufferedImage> IMAGES = new HashMap<>();

    public void init(Clan clan) throws Exception {
        this.clan = clan;

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        loadImages();

        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clan.saveToFile("clan.txt");

                super.windowClosing(e);
            }
        });

        labelClan = CompCreator.createLabel(clan.getName(), -1, -1, 100, 50, 25, true);

        frame.add(labelClan);

        TitledBorder borderPlayer = new TitledBorder("Spielerauswahl");

        // Left panel
        JPanel panelPlayerSelection = new JPanel();
        panelPlayerSelection.setLayout(null);
        panelPlayerSelection.setBorder(borderPlayer);
        panelPlayerSelection.setBounds(25, 75, 240, 400);

        labelPlayer = CompCreator.createLabel("Spieler:", 25, 25);

        boxPlayer = CompCreator.createPlayerSelection(clan, 100, 25);

        buttonAddPlayer = CompCreator.createButton("Hinzufügen", "Fügt einen neuen Spieler hinzu", 15, 55);
        buttonRemovePlayer = CompCreator.createButton("Entfernen", "Entfernt den aktuell ausgewählten Spieler", 125, 55);

        panelPlayerSelection.add(labelPlayer);
        panelPlayerSelection.add(boxPlayer);
        panelPlayerSelection.add(buttonAddPlayer);
        panelPlayerSelection.add(buttonRemovePlayer);

        frame.add(panelPlayerSelection);

        // Right panel
        HashMap<String, JComponent> TABS = new HashMap<>();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

        JTree tree = new JTree(root);
        tree.setCellRenderer(new ImageTreeCellRenderer(this));
        tree.setRowHeight(ROW_SIZE);

        paneScroll = new JScrollPane(tree);
        paneScroll.setBorder(null);
        frame.add(paneScroll);

        TABS.put("Übersicht", paneScroll);

        paneOverview = CompCreator.createTabbedPane(300, 80, 555, 392, TABS);

        buttonEditMode = CompCreator.createButton("Bearbeitung aktivieren", "Aktiviert die Bearbeitung, sodass Änderungen getroffen werden können", 300, 500, 175);
        buttonEditMode.setForeground(EDIT_INACTIVE);

        buttonEditMode.addActionListener(e -> {
            // Edit mode: false
            if (buttonEditMode.getText().equals("Bearbeitung aktivieren")) {
                editMode = true;
                buttonEditMode.setText("Bearbeitung deaktivieren");
                buttonEditMode.setForeground(EDIT_ACTIVE);
            } else if (buttonEditMode.getText().equals("Bearbeitung deaktivieren")) {
                editMode = false;
                buttonEditMode.setForeground(EDIT_INACTIVE);
                buttonEditMode.setText("Bearbeitung aktivieren");
            }
        });

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (editMode) {
                        String name = selPath.getPath()[selPath.getPathCount() - 1].toString();

                        if (name.contains("Doppler")) {

                            if (isChecked(name)) {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                                node.setUserObject(UNCHECKED + " Doppler");

                                Player player = clan.getPlayer(boxPlayer.getSelectedItem().toString());

                                if(player != null) {
                                    for(CW_Build build : CW_Build.values()) {
                                        if(build.toString().equals(node.getParent().toString())) {
                                            player.updateDoppler(build, false);
                                        }
                                    }
                                }

                                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                                model.reload();

                                // Expand all rows
                                for (int i = 0; i < tree.getRowCount(); i++) {
                                    tree.expandRow(i);
                                }
                            } else {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                                node.setUserObject(CHECKED + " Doppler");

                                Player player = clan.getPlayer(boxPlayer.getSelectedItem().toString());

                                if(player != null) {
                                    for(CW_Build build : CW_Build.values()) {
                                        if(build.toString().equals(node.getParent().toString())) {
                                            player.updateDoppler(build, true);
                                        }
                                    }
                                }

                                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                                model.reload();

                                // Expand all rows
                                for (int i = 0; i < tree.getRowCount(); i++) {
                                    tree.expandRow(i);
                                }
                            }
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);

        boxPlayer.addItemListener(e -> {
            // Change name of root
            root.setUserObject(boxPlayer.getSelectedItem());

            // Remove all builds
            root.removeAllChildren();
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.reload();

            if(!boxPlayer.getSelectedItem().toString().isEmpty()) {
                // Add builds
                Player player = clan.getPlayer(boxPlayer.getSelectedItem().toString());
                if (player != null) {
                    for (Build build : player.getBuilds()) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(build.getArt());

                        DefaultMutableTreeNode doppler;
                        if (build.isDoppler()) {
                            doppler = new DefaultMutableTreeNode(CHECKED + " Doppler");
                        } else {
                            doppler = new DefaultMutableTreeNode(UNCHECKED + " Doppler");
                        }
                        node.add(doppler);

                        root.add(node);
                    }
                }

                // Expand all rows
                for (int i = 0; i < tree.getRowCount(); i++) {
                    tree.expandRow(i);
                }
            }

            tree.repaint();
        });

        frame.add(paneOverview);

        frame.add(buttonEditMode);

        frame.setVisible(true);

        addActions();

        labelPlayer.requestFocus();
    }

    private void addActions() {
        buttonAddPlayer.addActionListener(e -> {
            String playerName = JOptionPane.showInputDialog(null, "Geben Sie den Namen des neuen Spielers ein?");

            if (clan.getPlayer(playerName) == null) {
                Player player = new Player(playerName);

                clan.addMember(player);

                boxPlayer.addItem(playerName);
                boxPlayer.setSelectedItem(playerName);
            } else {
                JOptionPane.showMessageDialog(null, "Dieser Spieler exisitert bereits!", "Achtung", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonRemovePlayer.addActionListener(e -> {
            String currentPlayer = boxPlayer.getSelectedItem().toString();

            if (!currentPlayer.isEmpty()) {
                int value = JOptionPane.showConfirmDialog(null, "<html>Wollen Sie <b>" + currentPlayer + "</b> wirklich entfernen?", "Achtung", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                if (value == 0) {
                    boxPlayer.removeItem(currentPlayer);

                    clan.removeMember(clan.getPlayer(currentPlayer));
                }
            }
        });
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

            if (key.equals(build.toString())) return value;
        }

        return null;
    }

    public BufferedImage getImage(String path) {
        return IMGLoader.loadImage(path);
    }

    public JComboBox<String> getBoxPlayer() {
        return boxPlayer;
    }

    public boolean isChecked(String name) {
        return name.contains(CHECKED);
    }
}

class ImageTreeCellRenderer implements TreeCellRenderer {

    private final JLabel label;

    private Window window;

    ImageTreeCellRenderer(Window window) {
        this.window = window;
        label = new JLabel();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {

        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

        if (!userObject.toString().isEmpty()) {
            // Is doppler
            if (!userObject.toString().contains("Doppler")) {
                // Add builds
                Player player = window.clan.getPlayer(window.getBoxPlayer().getSelectedItem().toString());
                for (Build build : player.getBuilds()) {
                    if (build.getArt().toString().equals(userObject.toString())) {
                        label.setIcon(new ImageIcon(window.getImage(build.getArt())));
                    }
                }

                // Is root?
                if (player.getName().equals(userObject)) {
                    label.setIcon(null);
                    label.setFont(label.getFont().deriveFont(18f).deriveFont(Font.BOLD));
                } else {
                    label.setFont(label.getFont().deriveFont(16f).deriveFont(Font.PLAIN));
                }
                label.setText(userObject.toString());

            } else {
                label.setIcon(null);
                if (window.isChecked(userObject.toString())) label.setText(window.CHECKED + " Doppler");
                else label.setText(window.UNCHECKED + " Doppler");

                label.setFont(label.getFont().deriveFont(16f));
            }
        } else {
            label.setText("");
        }

        label.setSize(200, tree.getRowHeight());

        return label;
    }
}