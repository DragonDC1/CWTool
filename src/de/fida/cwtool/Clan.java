package de.fida.cwtool;

import de.fida.cwtool.util.ListComperator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bag.CollectionSortedBag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Array;
import java.util.*;

public class Clan implements Serializable {
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
    public boolean addBuildCombination (List<Category> builds, Rating rating) {
        if (this.buildCombinations.contains(new BuildCombination(builds, rating)))
            return false;
        else
            return this.buildCombinations.add(new BuildCombination(builds, rating));
    }

    /*
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
     *  Fügt eine neue Spielerkombination hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn nicht alle Spieler im Clan sind,
     *  die Kombination bereits existiert
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addPlayerCombination(List<Player> player, Rating rating) {
        // Überprüfe, ob alle player im Clan
        for (Player p : player) {
            if (!this.members.contains(p))
                return false;
        }

        if (this.playerCombinations.contains(new PlayerCombination(player, rating)))
            return false;
        else
            return this.playerCombinations.add(new PlayerCombination(player, rating));
    }

    /*
     *  Entfernt eine bestehende Spielerkombination
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     *  oder nicht entfernt werden konnte
     */
    public boolean removePlayerCombination (List<Player> players) {
        for (PlayerCombination p : playerCombinations) {
            if(CollectionUtils.isEqualCollection(p.getPlayer(), players)) {
                return buildCombinations.remove(p);
            }
        }
        return false;
    }

    /*
     *  Ändert das Rating einer Spielerkombination
     *  Gibt true zurück, wenn erfolgreich geändert
     *  Gibt false zurück, wenn die Kombination nicht vorhanden ist
     */
    public boolean updateRatingPlayerCombination (List<Player> players, Rating newRating) {
        for (PlayerCombination p : playerCombinations) {
            if(CollectionUtils.isEqualCollection(p.getPlayer(), players)) {
                p.setRating(newRating);
                return true;
            }
        }
        return false;
    }

    public Player getPlayer(String name) {
        Player p = members.stream().filter(player -> name.equals(player.getName())).findAny().orElse(null);

        return p;
    }

    /*
     *  Gibt eine Liste aller SPieler mit all ihren Builds mit Doppler zurück
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
}
