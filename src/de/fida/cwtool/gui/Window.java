package de.fida.cwtool.gui;

import de.fida.cwtool.*;
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
import java.util.*;
import java.util.List;

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

    private JLabel labelTeam;
    private JLabel labelRating;

    private JComboBox<String> boxTeam;
    private JComboBox<String> boxRating;

    private JButton buttonAddTeam;
    private JButton buttonRemoveTeam;
    private JButton buttonRenameTeam;

    private JButton buttonEditMode;
    private JButton buttonAddBuild;
    private JButton buttonRemoveBuild;

    private boolean editMode;

    public final String UNCHECKED = "☐";
    public final String CHECKED = "☑";

    private final int ROW_SIZE = 50;

    private final int MAX_PLAYER_SIZE = 4;

    private final JLabel[] MEMBERS_LABEL = new JLabel[MAX_PLAYER_SIZE];
    private final JComboBox[] MEMBERS_BOX = new JComboBox[MAX_PLAYER_SIZE];
    private final String[] SAVED_PLAYER = new String[MAX_PLAYER_SIZE];

    private boolean refreshTeams;

    private JPanel panelPlayerSelection;

    private final Color EDIT_ACTIVE = new Color(0x216421);
    private final Color EDIT_INACTIVE = new Color(0x963032);

    private final HashMap<String, BufferedImage> IMAGES = new HashMap<>();

    private final HashMap<String, JComponent> TABS = new HashMap<>();
    private final HashMap<String, Boolean> STATES = new HashMap<>();

    private final List<Player> AVAILABLE_PLAYER = new ArrayList<>();

    private static PlayerCombination currentPlayerCombination;

    public void init(Clan clan) throws Exception {
        this.clan = clan;

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        loadImages();

        createWindow();

        labelClan = CompCreator.createLabel(clan.getName(), -1, -1, 100, 50, 25, true);
        frame.add(labelClan);

        // Left panel
        addLeftPanel();

        // Right panel
        addRightPanel();

        frame.setVisible(true);

        addActions();

        labelPlayer.requestFocus();
    }

    private void createWindow() {
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
    }

    private void addLeftPanel() {
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
    }

    private void addRightPanel() {
        // Tab 1
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

        JTree tree = new JTree(root);
        tree.setCellRenderer(new ImageTreeCellRenderer(this));
        tree.setRowHeight(ROW_SIZE);

        paneScroll = new JScrollPane(tree);
        paneScroll.setBorder(null);
        frame.add(paneScroll);

        TABS.put("Übersicht", paneScroll);

        buttonEditMode = CompCreator.createButton("Bearbeitung aktivieren", "Aktiviert die Bearbeitung, sodass Änderungen getroffen werden können", 300, 500, 175);
        buttonEditMode.setForeground(EDIT_INACTIVE);
        frame.add(buttonEditMode);

        buttonAddBuild = CompCreator.createButton("Build hinzufügen", "Fügt einen neuen Build dem Spieler hinzu", 490, 500, 175);
        buttonAddBuild.setEnabled(false);
        frame.add(buttonAddBuild);

        buttonRemovePlayer = CompCreator.createButton("Bearbeitung aktivieren", "Entfernt einen Build des Spielers", 680, 500, 175);
        buttonRemovePlayer.setEnabled(false);
        frame.add(buttonRemovePlayer);

        buttonEditMode.addActionListener(e -> {
            // Edit mode: false
            if (buttonEditMode.getText().equals("Bearbeitung aktivieren")) {
                editMode = true;
                buttonEditMode.setText("Bearbeitung deaktivieren");
                buttonEditMode.setForeground(EDIT_ACTIVE);

                buttonAddBuild.setEnabled(true);
                buttonRemovePlayer.setEnabled(true);
            } else if (buttonEditMode.getText().equals("Bearbeitung deaktivieren")) {
                editMode = false;
                buttonEditMode.setForeground(EDIT_INACTIVE);
                buttonEditMode.setText("Bearbeitung aktivieren");

                buttonAddBuild.setEnabled(false);
                buttonRemovePlayer.setEnabled(false);
            }
        });

        buttonAddBuild.addActionListener(e -> {
            JFrame frame = new JFrame("Konfiguration");
            frame.setSize(200, 150);
            frame.setLayout(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JLabel labelBuild = new JLabel("Build");
            labelBuild.setBounds(15, 15, 50, 20);
            JComboBox boxBuilds = new JComboBox<>();
            boxBuilds.setBounds(75, 15, 95, 20);

            JLabel labelDoppler = new JLabel("Doppler:");
            labelDoppler.setBounds(15, 42, 50, 20);
            JCheckBox boxDoppler = new JCheckBox();
            boxDoppler.setBounds(75, 42, 50, 20);

            for (CW_Build build : CW_Build.values()) {
                boxBuilds.addItem(build.toString());

                boxBuilds.setVisible(true);
                frame.setVisible(true);
            }

            JButton button = new JButton("Bestätigen");
            button.setBounds(30, 75, 125, 25);

            button.addActionListener(e1 -> {

            });

            button.setVisible(true);
            frame.add(button);

            frame.add(labelBuild);
            frame.add(boxBuilds);
            frame.add(labelDoppler);
            frame.add(boxDoppler);

            frame.setVisible(true);
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

                                if (player != null) {
                                    for (CW_Build build : CW_Build.values()) {
                                        if (build.toString().equals(node.getParent().toString())) {
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

                                if (player != null) {
                                    for (CW_Build build : CW_Build.values()) {
                                        if (build.toString().equals(node.getParent().toString())) {
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

            if (!boxPlayer.getSelectedItem().toString().isEmpty()) {
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

        // Tab 2
        JPanel panelTeamBuilder = new JPanel();
        panelTeamBuilder.setLayout(null);

        labelTeam = CompCreator.createLabel("Team:", 25, 10);
        panelTeamBuilder.add(labelTeam);

        boxTeam = CompCreator.createTeamSelection(clan, 100, 10);
        panelTeamBuilder.add(boxTeam);

        buttonAddTeam = CompCreator.createButton("Erstellen", "Erstellt ein neues Team", 15, 40);
        panelTeamBuilder.add(buttonAddTeam);
        buttonRemoveTeam = CompCreator.createButton("Entfernen", "Entfernt das aktuell ausgewählte Team", 125, 40);
        panelTeamBuilder.add(buttonRemoveTeam);
        buttonRenameTeam = CompCreator.createButton("Umbennen", "Ändert den Namen des aktuell ausgewählten Teams", 235, 40);
        panelTeamBuilder.add(buttonRenameTeam);

        labelRating = CompCreator.createLabel("Rating:", 335, 10);
        panelTeamBuilder.add(labelRating);

        boxRating = CompCreator.createRatingSelection(410, 10);
        boxRating.setEnabled(false);
        panelTeamBuilder.add(boxRating);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    PlayerCombination currentPlayerCombination = clan.getPlayerCombination(boxTeam.getSelectedItem().toString());

                    //AVAILABLE_PLAYER.clear();

                    if (currentPlayerCombination != null) {

                        for (Player player : clan.getMembers()) {
                            if (!currentPlayerCombination.getPlayers().contains(player)) {
                                //System.out.println("FÜGE " + player.getName() + " hinzu");
                                //AVAILABLE_PLAYER.add(player);
                            }
                        }
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        boxTeam.addItemListener(e -> {
            if (!refreshTeams) {
                // Any team selected?
                boolean teamSelected = !boxTeam.getSelectedItem().toString().isEmpty();

                boxRating.setEnabled(teamSelected);

                PlayerCombination currentPlayerCombination = clan.getPlayerCombination(boxTeam.getSelectedItem().toString());
                Window.currentPlayerCombination = currentPlayerCombination;

                // Load rating
                if (currentPlayerCombination != null) {
                    if (currentPlayerCombination.getRating() == null) {
                        currentPlayerCombination.setRating(Rating.GOLD);
                    }

                    boxRating.setSelectedItem(currentPlayerCombination.getRating().toString());
                }

                for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
                    MEMBERS_BOX[i].removeAllItems();

                    MEMBERS_BOX[i].addItem("");
                }

                for (Player player : clan.getMembers()) {
                    // Load rating
                    if (currentPlayerCombination != null) {
                        //if(!currentPlayerCombination.getPlayers().contains(player)) {

                        for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
                            //System.out.println("ADD: " + player.getName() + " to " + currentPlayerCombination.getName());
                            MEMBERS_BOX[i].addItem(player.getName());
                        }
                        //}
                    }
                }

                panelPlayerSelection.setVisible(teamSelected);
            }
        });

        boxRating.addItemListener(e -> {
            PlayerCombination currentPlayerCombination = clan.getPlayerCombination(boxTeam.getSelectedItem().toString());

            if (currentPlayerCombination != null) {
                for (Rating rating : Rating.values()) {
                    if (rating.toString().equals(boxRating.getSelectedItem().toString())) {
                        currentPlayerCombination.setRating(rating);
                    }
                }
            }
        });

        buttonAddTeam.addActionListener(e -> {
            String teamName = JOptionPane.showInputDialog(null, "Geben Sie den Namen des neuen Teams ein:");

            if (clan.getPlayerCombination(teamName) == null) {
                clan.addPlayerCombination(teamName);

                boxTeam.addItem(teamName);
                boxTeam.setSelectedItem(teamName);

                boxRating.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(null, "Dieses Team exisitert bereits!", "Achtung", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonRemoveTeam.addActionListener(e -> {
            String currentTeam = boxTeam.getSelectedItem().toString();

            if (!currentTeam.isEmpty()) {
                int value = JOptionPane.showConfirmDialog(null, "<html>Wollen Sie <b>" + currentTeam + "</b> wirklich entfernen?", "Achtung", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                if (value == 0) {
                    boxTeam.removeItem(currentTeam);

                    clan.removePlayerCombination(currentTeam);
                }
            }
        });

        buttonRenameTeam.addActionListener(e -> {
            String currentTeam = boxTeam.getSelectedItem().toString();
            String newName = JOptionPane.showInputDialog(null, "<html>Geben Sie den neuen Namen des Teams <b>" + currentTeam + "</b> ein:</html>");

            if (!currentTeam.isEmpty()) {
                PlayerCombination currentPlayerCombination = clan.getPlayerCombination(currentTeam);

                // Load rating
                if (currentPlayerCombination != null) {
                    currentPlayerCombination.setName(newName);

                    refreshTeams = true;
                    boxTeam.removeAllItems();

                    boxTeam.addItem("");

                    for (PlayerCombination combination : clan.getPlayerCombinations()) {
                        boxTeam.addItem(combination.getName());
                    }

                    boxTeam.setSelectedItem(newName);

                    refreshTeams = false;
                }
            }
        });

        // Player selection
        panelPlayerSelection = new JPanel();
        panelPlayerSelection.setLayout(null);
        panelPlayerSelection.setBorder(new TitledBorder("Spielerauswahl"));
        panelPlayerSelection.setBounds(50, 75, 450, 250);

        panelTeamBuilder.add(panelPlayerSelection);

        AVAILABLE_PLAYER.addAll(clan.getMembers());

        for (Player p : clan.getMembers()) {
            STATES.put(p.getName(), true);
        }

        for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
            JLabel label = CompCreator.createLabel("Spieler " + (i + 1) + ":", 25, 25 + (i * 25));
            MEMBERS_LABEL[i] = label;

            JComboBox<String> member = CompCreator.createBox(75, 25 + (i * 25));
            MEMBERS_BOX[i] = member;

            member.addItem("");

            for (Player player : AVAILABLE_PLAYER) {
                //member.addItem(player.getName());
            }

            member.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    if (member.getSelectedItem() != null) {
                        if (member.getSelectedItem().toString().isEmpty()) {
                            member.removeAllItems();

                            member.addItem("");

                            for (Map.Entry<String, Boolean> entry : STATES.entrySet()) {
                                String key = entry.getKey();
                                boolean value = entry.getValue();

                                if (value) {
                                    member.addItem(key);
                                }
                            }
                        }
                    }


                    super.mousePressed(e);
                }
            });

            member.addItemListener(e -> {
                if (Window.currentPlayerCombination != null) {
                    if (member.getSelectedItem() != null) {
                        if (!member.getSelectedItem().toString().isEmpty()) {
                            STATES.replace(member.getSelectedItem().toString(), false);
                        }
                    }
                }
            });

            //int finalI = i;

            //member.addItemListener(e -> {
            //    if (Window.currentPlayerCombination != null) {
            //        if (member.getSelectedItem() != null) {
//
            //            if(!member.getSelectedItem().toString().isEmpty()) {
            //                AVAILABLE_PLAYER.remove(clan.getPlayer(member.getSelectedItem().toString()));
//
            //                SAVED_PLAYER[finalI] = member.getSelectedItem().toString();
//
            //                for (JComboBox box : MEMBERS_BOX) {
            //                    if (box != member) {
            //                        // Other boxes
//
            //                        //box.removeAllItems();
//
            //                        //box.addItem("");
//
            //                        //for (Player player : AVAILABLE_PLAYER) {
            //                        //box.addItem(player.getName());
            //                        //}
//
            //                        box.removeItem(member.getSelectedItem().toString());
            //                    } else {
            //                        // Own box
//
            //                    }
            //                }
            //            } else {
            //                // Reset player
            //                for (JComboBox box : MEMBERS_BOX) {
            //                    if (box != member) {
            //                        // Other boxes
//
            //                        //box.removeAllItems();
//
            //                        //box.addItem("");
//
            //                        //for (Player player : AVAILABLE_PLAYER) {
            //                        //box.addItem(player.getName());
            //                        //}
//
            //                        if(SAVED_PLAYER[finalI] != null) {
            //                            if(!SAVED_PLAYER[finalI].isEmpty()) {
            //                                box.addItem(SAVED_PLAYER[finalI]);
            //                            }
            //                        }
            //                    } else {
            //                        // Own box
//
//
            //                    }
            //                }
            //            }
//
            //            //System.out.println("Füge " + clan.getPlayer(member.getSelectedItem().toString()) + " dem Team " + Window.currentPlayerCombination.getName() + " hinzu");
            //            //Window.currentPlayerCombination.addPlayer(clan.getPlayer(member.getSelectedItem().toString()));
//
            //            /*Player player = clan.getPlayer(member.getSelectedItem().toString());
//
            //            if(player == null) {
            //                for(Player pl : clan.getMembers()) {
            //                    if(!AVAILABLE_PLAYER.contains(pl)) {
            //                        System.out.println("Füge " + pl.getName() + " wieder hinzu");
            //                        AVAILABLE_PLAYER.add(pl);
            //                    }
            //                }
            //            }
//
            //            System.out.println("Entferne " + clan.getPlayer(member.getSelectedItem().toString()) + " aus Liste");
            //            AVAILABLE_PLAYER.remove(clan.getPlayer(member.getSelectedItem().toString()));
//
            //            for(Player p : AVAILABLE_PLAYER) {
            //                System.out.println("NOCH ÜBRIG: " + p.getName());
            //            }
//
            //            member.removeAllItems();
//
            //            try {
            //                for (Player pla : AVAILABLE_PLAYER) {
            //                    member.addItem(pla.getName());
            //                }
            //            } catch (Exception ignored) {
            //            }*/
            //        }
            //    }
            //});

            panelPlayerSelection.add(label);
            panelPlayerSelection.add(member);
        }

        TABS.put("TeamBuilder", panelTeamBuilder);

        paneOverview = CompCreator.createTabbedPane(300, 80, 555, 392, TABS);
        frame.add(paneOverview);
    }

    private void addActions() {
        buttonAddPlayer.addActionListener(e -> {
            String playerName = JOptionPane.showInputDialog(null, "Geben Sie den Namen des neuen Spielers ein:");

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