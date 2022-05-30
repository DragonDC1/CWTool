package de.fida.cwtool.gui.events;

import de.fida.cwtool.CW_Build;
import de.fida.cwtool.Player;
import de.fida.cwtool.gui.Window;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Finnegan Kaiser | 30.05.2022 (07:52)
 **/

public class NodeClickEvent implements MouseListener {

    private JTree tree;

    private Window window;

    public NodeClickEvent(JTree tree, Window window) {
        this.tree = tree;
        this.window = window;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1) {
            if (window.editMode) {
                String name = selPath.getPath()[selPath.getPathCount() - 1].toString();

                if (name.contains("Doppler")) {

                    if (window.isChecked(name)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                        node.setUserObject(window.UNCHECKED + " Doppler");

                        Player player = window.clan.getPlayer(window.boxPlayer.getSelectedItem().toString());

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
                        node.setUserObject(window.CHECKED + " Doppler");

                        Player player = window.clan.getPlayer(window.boxPlayer.getSelectedItem().toString());

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

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
