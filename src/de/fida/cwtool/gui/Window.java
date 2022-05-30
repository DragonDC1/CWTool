package de.fida.cwtool.gui;

import de.fida.cwtool.*;
import de.fida.cwtool.gui.events.NodeClickEvent;
import de.fida.cwtool.util.BuildOverview;
import de.fida.cwtool.util.CompCreator;
import de.fida.cwtool.util.IMGLoader;
import de.fida.cwtool.util.SortedComboBoxModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
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

    public JComboBox<String> boxPlayer;

    private JTabbedPane tabbedPane;
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

    public boolean editMode;

    public final String UNCHECKED = "☐";
    public final String CHECKED = "☑";

    private final int ROW_SIZE = 50;

    private final int MAX_PLAYER_SIZE = 4;
    private final int MAX_BUILDS = 99;

    private final JLabel[] MEMBERS_LABEL = new JLabel[MAX_PLAYER_SIZE];
    private final JComboBox[] MEMBERS_BOX = new JComboBox[MAX_PLAYER_SIZE];
    private final BuildOverview[] MEMBERS_BUILDS = new BuildOverview[MAX_PLAYER_SIZE];
    private final String[] SAVED_PLAYER = new String[MAX_PLAYER_SIZE];

    private boolean refreshPlayer;
    private boolean refreshTeams;

    private JPanel panelPlayerSelection;

    private final Color EDIT_ACTIVE = new Color(0x216421);
    private final Color EDIT_INACTIVE = new Color(0x963032);

    private final HashMap<String, BufferedImage> IMAGES = new HashMap<>();

    private final HashMap<String, JComponent> TABS = new HashMap<>();
    private final HashMap<String, Boolean> STATES = new HashMap<>();

    private final List<String> AVAILABLE_PLAYER = new ArrayList<>();

    private static PlayerCombination currentPlayerCombination;

    public void init(Clan clan) throws Exception {
        this.clan = clan;

        // Sort members
        clan.getMembers().sort(comparePlayer);

        // Set Windows theme
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Preload all images
        loadImages();

        // Open window
        createWindow();

        // Clan label
        labelClan = CompCreator.createLabel(clan.getName(), -1, -1, 100, 50, 25, true);
        frame.add(labelClan);

        // Left panel
        addLeftPanel();

        // Right panel
        addRightPanel();

        frame.setVisible(true);

        Thread thread = new Thread(() -> {
            while (true) {

                /*if (currentPlayerCombination != null) {
                    for(int c = 0; c < currentPlayerCombination.getPlayers().length; c++) {
                        Player player = currentPlayerCombination.getPlayers()[c];

                        if(player != null) {
                            System.out.println((c + 1) + " bekommt " + player.getName());
                            MEMBERS_BOX[c].setSelectedItem(player.getName());
                        }
                    }
                }*/




                /*for(PlayerCombination combination : clan.getPlayerCombinations()) {
                    for(int i = 0; i < combination.getPlayers().length; i++) {
                        Player player = combination.getPlayers()[i];

                        if(player != null) {
                            System.out.println((i + 1) + " bekommt " + player.getName());
                            MEMBERS_BOX[i].setSelectedItem(player.getName());
                        }
                    }
                }*/

                /*for (PlayerCombination combination : clan.getPlayerCombinations()) {
                    if (currentPlayerCombination != null) {
                        if (combination.getName().equals(currentPlayerCombination.getName())) {
                            for (int c = 0; c < combination.getPlayers().length; c++) {
                                Player player = combination.getPlayers()[c];

                                if (player != null) {
                                    System.out.println((c + 1) + " bekommt " + player.getName());
                                    MEMBERS_BOX[c].setSelectedItem(player.getName());
                                }
                            }
                        }
                    }
                }*/

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        labelPlayer.requestFocus();
    }

    private void createWindow() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        // Save file on exit
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
        buttonRemovePlayer.setEnabled(false);

        buttonAddPlayer.addActionListener(e -> {
            String playerName = JOptionPane.showInputDialog(null, "Geben Sie den Namen des neuen Spielers ein:");

            if (clan.getPlayer(playerName) == null) {
                Player player = new Player(playerName);

                clan.addMember(player);

                boxPlayer.addItem(playerName);
                boxPlayer.setSelectedItem(playerName);

                AVAILABLE_PLAYER.clear();

                for (Player p : clan.getMembers()) {
                    AVAILABLE_PLAYER.add(p.getName());
                }

                refreshPlayer = true;
                for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
                    MEMBERS_BOX[i].removeAllItems();

                    MEMBERS_BOX[i].addItem("");

                    for (String p : AVAILABLE_PLAYER) {
                        MEMBERS_BOX[i].addItem(p);
                    }
                }

                if (currentPlayerCombination != null) {
                    for (int i = 0; i < currentPlayerCombination.getPlayers().length; i++) {
                        Player p = currentPlayerCombination.getPlayers()[i];

                        if (p != null) {
                            System.out.println((i + 1) + " bekommt " + p.getName());
                            MEMBERS_BOX[i].setSelectedItem(p.getName());
                            System.out.println("Lerne " + p.getName() + " in " + (i + 1));
                            SAVED_PLAYER[i] = p.getName();
                            MEMBERS_BUILDS[i].setPlayer(p);

                            for (JComboBox box : MEMBERS_BOX) {
                                if (box != MEMBERS_BOX[i]) {
                                    box.removeItem(MEMBERS_BOX[i].getSelectedItem().toString());
                                }
                            }
                        }
                    }
                }
                refreshPlayer = false;
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

                    AVAILABLE_PLAYER.clear();

                    for (Player p : clan.getMembers()) {
                        AVAILABLE_PLAYER.add(p.getName());
                    }

                    refreshPlayer = true;
                    for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
                        MEMBERS_BOX[i].removeAllItems();

                        MEMBERS_BOX[i].addItem("");

                        for (String p : AVAILABLE_PLAYER) {
                            MEMBERS_BOX[i].addItem(p);
                        }
                    }

                    if (currentPlayerCombination != null) {
                        for (int i = 0; i < currentPlayerCombination.getPlayers().length; i++) {
                            Player player = currentPlayerCombination.getPlayers()[i];

                            if (player != null) {
                                System.out.println((i + 1) + " bekommt " + player.getName());
                                MEMBERS_BOX[i].setSelectedItem(player.getName());
                                System.out.println("Lerne " + player.getName() + " in " + (i + 1));
                                SAVED_PLAYER[i] = player.getName();
                                MEMBERS_BUILDS[i].setPlayer(player);

                                for (JComboBox box : MEMBERS_BOX) {
                                    if (box != MEMBERS_BOX[i]) {
                                        box.removeItem(MEMBERS_BOX[i].getSelectedItem().toString());
                                    }
                                }
                            }
                        }
                    }
                    refreshPlayer = false;
                }
            }
        });

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
        tree.setBackground(new Color(240, 240, 240));

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

        buttonRemoveBuild = CompCreator.createButton("Build entfernen", "Entfernt einen Build des Spielers", 680, 500, 175);
        buttonRemoveBuild.setEnabled(false);
        frame.add(buttonRemoveBuild);

        buttonEditMode.addActionListener(e -> {
            // Edit mode: false
            if (buttonEditMode.getText().equals("Bearbeitung aktivieren")) {
                editMode = true;
                buttonEditMode.setText("Bearbeitung deaktivieren");
                buttonEditMode.setForeground(EDIT_ACTIVE);

                if (!boxPlayer.getSelectedItem().toString().isEmpty()) {
                    buttonAddBuild.setEnabled(true);
                    buttonRemoveBuild.setEnabled(true);

                    if (clan.getPlayer(boxPlayer.getSelectedItem().toString()).getBuilds().size() == MAX_BUILDS) {
                        buttonAddBuild.setEnabled(false);
                    }
                }
            } else if (buttonEditMode.getText().equals("Bearbeitung deaktivieren")) {
                editMode = false;
                buttonEditMode.setForeground(EDIT_INACTIVE);
                buttonEditMode.setText("Bearbeitung aktivieren");

                if (!boxPlayer.getSelectedItem().toString().isEmpty()) {
                    buttonAddBuild.setEnabled(false);
                    buttonRemoveBuild.setEnabled(false);
                }
            }
        });

        buttonAddBuild.addActionListener(e -> {
            if (clan.getPlayer(boxPlayer.getSelectedItem().toString()).getBuilds().size() == MAX_BUILDS) {
                return;
            }

            JFrame frame = new JFrame("Konfiguration");
            frame.setSize(200, 150);
            frame.setLayout(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JLabel labelBuild = new JLabel("Build");
            labelBuild.setBounds(15, 15, 50, 20);
            JComboBox boxBuilds = new JComboBox<>(new SortedComboBoxModel<>());
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
                Player player = clan.getPlayer(boxPlayer.getSelectedItem().toString());

                if (player != null) {
                    for (CW_Build build : CW_Build.values()) {
                        if (build.toString().equals(boxBuilds.getSelectedItem().toString())) {
                            player.addBuild(new Build(build, boxDoppler.isSelected()));

                            if (player.getBuilds().size() == MAX_BUILDS) {
                                frame.setVisible(false);
                                buttonAddBuild.setEnabled(false);
                            }
                        }
                    }

                    updateTree(tree, root);
                }
            });

            button.setVisible(true);
            frame.add(button);

            frame.add(labelBuild);
            frame.add(boxBuilds);
            frame.add(labelDoppler);
            frame.add(boxDoppler);

            frame.setVisible(true);
        });

        tree.addMouseListener(new NodeClickEvent(tree, this));

        boxPlayer.addItemListener(e -> {
            // Change name of root
            root.setUserObject(boxPlayer.getSelectedItem());

            updateTree(tree, root);

            if (editMode) {
                if (boxPlayer.getSelectedItem().toString().isEmpty()) {
                    buttonAddBuild.setEnabled(false);
                    buttonRemovePlayer.setEnabled(false);
                } else {
                    buttonAddBuild.setEnabled(true);
                    buttonRemovePlayer.setEnabled(true);
                }
            }

            if (boxPlayer.getSelectedItem().toString().isEmpty()) {
                buttonRemovePlayer.setEnabled(false);
            } else {
                buttonRemovePlayer.setEnabled(true);
            }

            if (clan.getPlayer(boxPlayer.getSelectedItem().toString()).getBuilds().size() == MAX_BUILDS) {
                buttonAddBuild.setEnabled(false);
            }
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
        buttonRemoveTeam.setEnabled(false);
        panelTeamBuilder.add(buttonRemoveTeam);
        buttonRenameTeam = CompCreator.createButton("Umbennen", "Ändert den Namen des aktuell ausgewählten Teams", 235, 40);
        buttonRenameTeam.setEnabled(false);
        panelTeamBuilder.add(buttonRenameTeam);

        labelRating = CompCreator.createLabel("Rating:", 335, 10);
        panelTeamBuilder.add(labelRating);

        boxRating = CompCreator.createRatingSelection(410, 10);
        boxRating.setEnabled(false);
        panelTeamBuilder.add(boxRating);

        boxTeam.addItemListener(e -> {
            if (!refreshTeams && e.getStateChange() == ItemEvent.SELECTED) {
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

                refreshPlayer = true;
                for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
                    MEMBERS_BOX[i].removeAllItems();

                    MEMBERS_BOX[i].addItem("");

                    for (String player : AVAILABLE_PLAYER) {
                        MEMBERS_BOX[i].addItem(player);
                    }
                }

                for (BuildOverview overview : MEMBERS_BUILDS) {
                    overview.setPlayer(null);
                }

                if (currentPlayerCombination != null) {
                    for (int i = 0; i < currentPlayerCombination.getPlayers().length; i++) {
                        Player player = currentPlayerCombination.getPlayers()[i];

                        if (player != null) {
                            System.out.println((i + 1) + " bekommt " + player.getName());
                            MEMBERS_BOX[i].setSelectedItem(player.getName());
                            System.out.println("Lerne " + player.getName() + " in " + (i + 1));
                            SAVED_PLAYER[i] = player.getName();
                            MEMBERS_BUILDS[i].setPlayer(player);

                            for (JComboBox box : MEMBERS_BOX) {
                                if (box != MEMBERS_BOX[i]) {
                                    box.removeItem(MEMBERS_BOX[i].getSelectedItem().toString());
                                }
                            }
                        }
                    }
                }

                refreshPlayer = false;
                System.out.println("----------------");

                panelPlayerSelection.setVisible(teamSelected);
            }

            if (boxTeam.getSelectedItem().toString().isEmpty()) {
                buttonRenameTeam.setEnabled(false);
                buttonRemoveTeam.setEnabled(false);
            } else {
                buttonRenameTeam.setEnabled(true);
                buttonRemoveTeam.setEnabled(true);
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

                if (MEMBERS_BOX[0].getItemCount() == 0) {
                    for (JComboBox box : MEMBERS_BOX) {
                        for (String player : AVAILABLE_PLAYER) {
                            box.addItem(player);
                        }
                    }
                }
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
            if (!currentTeam.isEmpty()) {
                String newName = JOptionPane.showInputDialog(null, "<html>Geben Sie den neuen Namen des Teams <b>" + currentTeam + "</b> ein:</html>");

                if (!newName.isEmpty()) {
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
            }
        });

        // Player selection
        panelPlayerSelection = new JPanel();
        panelPlayerSelection.setLayout(null);
        panelPlayerSelection.setBorder(new TitledBorder("Spielerauswahl"));
        panelPlayerSelection.setBounds(50, 75, 450, 250);
        panelPlayerSelection.setVisible(false);

        panelTeamBuilder.add(panelPlayerSelection);

        for (Player player : clan.getMembers()) {
            AVAILABLE_PLAYER.add(player.getName());
        }

        for (Player p : clan.getMembers()) {
            STATES.put(p.getName(), true);
        }

        for (int i = 0; i < MAX_PLAYER_SIZE; i++) {
            JLabel label = CompCreator.createLabel("Spieler " + (i + 1) + ":", 25, 25 + (i * 35));
            MEMBERS_LABEL[i] = label;

            SortedComboBoxModel<String> model = new SortedComboBoxModel<String>(AVAILABLE_PLAYER.toArray(new String[0]));

            JComboBox<String> member = CompCreator.createBox(model, 75, 25 + (i * 35));
            MEMBERS_BOX[i] = member;

            BuildOverview overview = new BuildOverview(this);
            overview.setBounds(225, 25 + (i * 35) - 8, 200, 32);
            MEMBERS_BUILDS[i] = overview;

            member.addItem("");

            for (String player : AVAILABLE_PLAYER) {
                member.addItem(player);
            }

            int finalI = i;

            member.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (Window.currentPlayerCombination != null) {
                        if (member.getSelectedItem() != null && !refreshPlayer) {
                            //
                            if (!member.getSelectedItem().toString().isEmpty()) {
                                String selectedMember = member.getSelectedItem().toString();

                                AVAILABLE_PLAYER.remove(clan.getPlayer(selectedMember));

                                refreshPlayer = true;

                                for (JComboBox box : MEMBERS_BOX) {
                                    if (box != member) {
                                        // Other boxes

                                        if (SAVED_PLAYER[finalI] != null) {
                                            if (!SAVED_PLAYER[finalI].isEmpty()) {
                                                box.addItem(SAVED_PLAYER[finalI]);
                                            }
                                        }

                                        box.removeItem(selectedMember);
                                    }
                                }

                                System.out.println("Lerne " + selectedMember + " in " + (finalI + 1));
                                SAVED_PLAYER[finalI] = selectedMember;
                                Window.currentPlayerCombination.setPlayer(finalI, clan.getPlayer(selectedMember));
                                System.out.println((finalI + 1) + " bekommt " + clan.getPlayer(selectedMember).getName());

                                System.out.println("Setze Spieler auf " + (finalI + 1));
                                MEMBERS_BUILDS[finalI].setPlayer(clan.getPlayer(selectedMember));

                                refreshPlayer = false;
                            } else {
                                // Reset player
                                refreshPlayer = true;

                                for (JComboBox box : MEMBERS_BOX) {
                                    if (box != member) {
                                        if (SAVED_PLAYER[finalI] != null) {
                                            if (!SAVED_PLAYER[finalI].isEmpty()) {
                                                box.addItem(SAVED_PLAYER[finalI]);
                                            }
                                        }
                                    }
                                }

                                // Clear last player
                                SAVED_PLAYER[finalI] = "";
                                Window.currentPlayerCombination.setPlayer(finalI, null);

                                MEMBERS_BUILDS[finalI].setPlayer(null);

                                refreshPlayer = false;
                            }
                        }
                    }
                }
            });

            panelPlayerSelection.add(label);
            panelPlayerSelection.add(member);
            panelPlayerSelection.add(overview);
        }

        TABS.put("TeamBuilder", panelTeamBuilder);

        tabbedPane = CompCreator.createTabbedPane(300, 80, 555, 392, TABS);
        frame.add(tabbedPane);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                buttonEditMode.setVisible(true);
                buttonAddBuild.setVisible(true);
                buttonRemoveBuild.setVisible(true);
            } else {
                buttonEditMode.setVisible(false);
                buttonAddBuild.setVisible(false);
                buttonRemoveBuild.setVisible(false);
            }
        });
    }

    private void updateTree(JTree tree, DefaultMutableTreeNode root) {
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

    public static Comparator<Player> comparePlayer = Comparator.comparing(Player::getName);
}

class ImageTreeCellRenderer implements TreeCellRenderer {

    private final JLabel label;

    private Window window;

    ImageTreeCellRenderer(Window window) {
        this.window = window;
        label = new JLabel();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

        if(((DefaultMutableTreeNode) value).getParent() == null) {
            JLabel playerName = new JLabel(userObject.toString());
            playerName.setFont(playerName.getFont().deriveFont(20f).deriveFont(Font.BOLD));

            return playerName;
        }

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

                label.setFont(label.getFont().deriveFont(14f));
            }
        } else {
            label.setText("");
        }

        label.setSize(200, tree.getRowHeight());

        if (label.getText().contains("Doppler")) {
            return label;
        } else {
            JLabel test = new JLabel("           " + label.getText()) {
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

                    Player player = window.clan.getPlayer(window.getBoxPlayer().getSelectedItem().toString());
                    if (player != null) {
                        for (Build build : player.getBuilds()) {
                            if (build.getArt().toString().equals(userObject.toString())) {
                                BufferedImage image = window.getImage(build.getArt());

                                g.drawImage(image, 0, -5, 60, 60, null);
                            }
                        }
                    }
                }
            };
            test.setBounds(label.getBounds().x, label.getBounds().y, label.getWidth(), label.getHeight());
            test.setFont(test.getFont().deriveFont(18f));

            return test;
        }
    }
}