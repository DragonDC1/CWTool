package de.fida.cwtool;

import de.fida.cwtool.Build;
import de.fida.cwtool.CW_Build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    String name;                                        // Spielername
    private List<Build> builds = new ArrayList<>();     // Liste aller Builds

    public Player (String name) {
        this.name = name;
    }

    public Player(String name, Build build) {
        this.name = name;
        this.builds.add(build);
    }

    /*
     *  Fügt einem Spieler einen neuen Build hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn bereits in Liste der Builds vorhanden oder
     *  der Build nicht hinzugefügt werden konnte
     */
    public boolean addBuild(Build build) {
        if(this.builds.contains(build))
            return false;
        else
            return this.builds.add(build);
    }

    /*
     *  Entfernt einen Build von einem Spieler
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, der Build nicht entfernt werden konnte
     */
    public boolean removeBuild(Build build) {
        return this.builds.remove(build);
    }

    /*
     *  Ändert, ob auf einem Build ein Doppler verbaut werden kann
     */

    public void updateDoppler(CW_Build build, boolean doppler) {
        // Iteriere durch alle Builds
        for(Build b : builds) {
            if(b.getArt() == build) {
                b.updateDoppler(doppler);
            }
        }
    }

    public String toString() {
       return name + ": " + builds.toString();
    }


    public String getName() {
        return this.name;
    }

    public List<Build> getBuilds() {
        return builds;
    }
}
