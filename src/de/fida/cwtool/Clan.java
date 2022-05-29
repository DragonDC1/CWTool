package de.fida.cwtool;

import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.util.*;

public class Clan implements Serializable {
    @Serial
    private static final long serialVersionUID = -7268115727182617157L;
    private String name;                                                    // Clanname
    private List<Player> members = new ArrayList<>();                       // Liste der Mitglieder
    private List<BuildCombination> buildCombinations = new ArrayList<>();   // Liste aller Build-Kombinationen
    private List<PlayerCombination> playerCombinations = new ArrayList<>(); // Liste aller Spieler-Kombinationen

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
     *  Gibt false zurück, wenn der Spieler nicht entfernt werden konnte
     */
    public boolean removeMember(Player player) {
        return this.members.remove(player);
    }

    /*
     *  Fügt eine neue Kombination aus Builds hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn die Kombination schon vorhanden ist
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addBuildCombination (String name, List<Category> builds, Rating rating) {
        if (this.buildCombinations.contains(new BuildCombination(name, builds, rating)))
            return false;
        else
            return this.buildCombinations.add(new BuildCombination(name, builds, rating));
    }

    /*
     *  Fügt eine neue Kombination aus Builds hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn die Kombination schon vorhanden ist
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addBuildCombination (String name) {
        if (this.buildCombinations.contains(new BuildCombination(name)))
            return false;
        else
            return this.buildCombinations.add(new BuildCombination(name));
    }

    /* LEGACY
     *  Entfernt eine bestehende Kombination aus Builds
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     *  oder nicht entfernt werden konnte
     */
    public boolean removeBuildCombination (List<Category> builds) {
        for (BuildCombination b : buildCombinations) {
            if(CollectionUtils.isEqualCollection(b.getBuilds(), builds)) {
                return buildCombinations.remove(b);
            }
        }
        return false;
    }

    /*
     *  Entfernt eine bestehende Kombination aus Builds
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     *  oder nicht entfernt werden konnte
     */
    public boolean removeBuildCombination (String name) {
        for (BuildCombination b : buildCombinations) {
            if(b.getName().equals(name)) {
                return buildCombinations.remove(b);
            }
        }
        return false;
    }

    /*  LEGACY
     *  Ändert das Rating einer Kombination
     *  Gibt true zurück, wenn erfolgreich geändert
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     */
    public boolean changeRatingBuildCombination (List<Category> builds, Rating newRating) {
        for (BuildCombination b : buildCombinations) {
            if(CollectionUtils.isEqualCollection(b.getBuilds(), builds)) {
                b.setRating(newRating);
                return true;
            }
        }
        return false;
    }

    /*
     *  Ändert das Rating einer Kombination
     *  Gibt true zurück, wenn erfolgreich geändert
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     */
    public boolean changeRatingBuildCombination (String name, Rating newRating) {
        for (BuildCombination b : buildCombinations) {
            if(b.getName().equals(name)) {
                b.setRating(newRating);
                return true;
            }
        }
        return false;
    }

    /*
     *  Fügt eine neue Spielerkombination hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn nicht alle Spieler im Clan sind,
     *  die Kombination bereits existiert
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addPlayerCombination (String name, List<Player> player, Rating rating) {
        // Überprüfe, ob alle player im Clan
        for (Player p : player) {
            if (!this.members.contains(p))
                return false;
        }

        if (this.playerCombinations.contains(new PlayerCombination(name, player, rating)))
            return false;
        else
            return this.playerCombinations.add(new PlayerCombination(name, player, rating));
    }

    /*
     *  Fügt eine neue, noch leere Spielerkombination hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefüg
     *  Gibt false zurück, wenn die Kombination bereits existiert
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addPlayerCombination (String name) {
        return this.playerCombinations.add(new PlayerCombination(name));
    }

    /*  LEGACY
     *  Entfernt eine bestehende Spielerkombination
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     *  oder nicht entfernt werden konnte
     */
    public boolean removePlayerCombination (List<Player> players) {
        for (PlayerCombination p : playerCombinations) {
            if(CollectionUtils.isEqualCollection(p.getPlayers(), players)) {
                return playerCombinations.remove(p);
            }
        }
        return false;
    }

    /*
     *  Entfernt eine bestehende Spielerkombination
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     *  oder nicht entfernt werden konnte
     */
    public boolean removePlayerCombination (String name) {
        for (PlayerCombination p : playerCombinations) {
            if(p.getName().equals(name)) {
                return playerCombinations.remove(p);
            }
        }
        return false;
    }

    /*  LEGACY
     *  Ändert das Rating einer Spielerkombination
     *  Gibt true zurück, wenn erfolgreich geändert
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     */
    public boolean updateRatingPlayerCombination (List<Player> players, Rating newRating) {
        for (PlayerCombination p : playerCombinations) {
            if(CollectionUtils.isEqualCollection(p.getPlayers(), players)) {
                p.setRating(newRating);
                return true;
            }
        }
        return false;
    }

    /*
     *  Ändert das Rating einer Spielerkombination
     *  Gibt true zurück, wenn erfolgreich geändert
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     */
    public boolean updateRatingPlayerCombination (String name, Rating newRating) {
        for (PlayerCombination p : playerCombinations) {
            if(p.getName().equals(name)) {
                p.setRating(newRating);
                return true;
            }
        }
        return false;
    }

    /*
     *  Gibt einen Spieler durch Eingabe des Namens zurück
     */
    public Player getPlayer (String name) {
        Player p = members.stream().filter(player -> name.equals(player.getName())).findAny().orElse(null);

        return p;
    }

    /*
     *  Gibt eine Spielerkombination durch Eingabe des Namens zurück
     */
    public PlayerCombination getPlayerCombination (String name) {
        PlayerCombination p = playerCombinations.stream().filter(playerCombination -> name.equals(playerCombination.getName())).findAny().orElse(null);
        return p;
    }

    /*
     *  Gibt eine Liste aller Spieler mit all ihren Builds mit Doppler zurück
     */
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

    public List<BuildCombination> getBuildCombinations() {
        return buildCombinations;
    }

    public List<PlayerCombination> getPlayerCombinations() {
        return playerCombinations;
    }
}
