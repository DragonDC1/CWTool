package de.fida.cwtool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Clan implements Serializable {
    private String name;                                // Clanname
    private List<Player> members = new ArrayList<>();   // Liste der Mitglieder

    public Clan (String name) {
        this.name = name;
    }

    public Clan (String name, Player player) {
        this.name = name;
        this.members.add(player);
    }

    public Clan (String name, List<Player> players) {
        this.name = name;
        this.members = players;
    }

    /*
     *  Fügt einem Clan ein neues Mitglied hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn bereits in Liste der Mitglieder vorhanden oder
     *  das Mitglied nicht hinzugefügt werden konnte
     */
    public boolean addMember(Player player) {
        if(this.members.contains(player))
            return false;
        else
            this.members.add(player);
        return true;
    }

    /*
     *  Entfernt ein Mitglied aus einem Clan
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, der Spieler nicht entfernt werden konnte
     */
    public boolean removeMember(Player player) {
        return this.members.remove(player);
    }

    public Player getPlayer(String name) {
        Player p = members.stream().filter(player -> name.equals(player.getName())).findAny().orElse(null);

        if(p == null) {
            throw new RuntimeException("Den Spieler gibts ned!");
        }

        return p;
    }

    public List<Player> getAllDopplerBuilds() {
        List<Player> dopplerBuilds = new ArrayList<>();
        for (Player p : members) {
            List<Build> builds = p.getBuilds();
            Player p_copy = new Player(p.getName());
            //dopplerBuilds.add(new Player(p.getName()));
            boolean hasDoppler = false;
            for (Build b : builds) {
                if(b.isDoppler()) {
                    p_copy.addBuild(b);
                    hasDoppler = true;
                }
            }
            if(hasDoppler)
                dopplerBuilds.add(p_copy);
        }

        return dopplerBuilds;
    }

    public String toString() {
        return name + ": " + members.toString();
    }

    public void saveToFile(String name) {
        try {
            File file = new File("files/" + name);

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);

            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public List<Player> getMembers() {
        return members;
    }
}
