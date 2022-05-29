package de.fida.cwtool;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class BuildCombination implements Serializable {
    @Serial
    private static final long serialVersionUID = 2983901898052264780L;
    String name;
    List<Category> builds = new ArrayList<>();
    Rating rating;

    public BuildCombination (String name, List<Category> builds, Rating rating) {
        this.name = name;
        this.builds = builds;
        this.rating = rating;
    }

    // FIXME: 29.05.2022 Raus mit die Viecher
    public BuildCombination (List<Category> builds, Rating rating) {
        this.builds = builds;
        this.rating = rating;
    }

    public BuildCombination (String name) {
        this.name = name;
    }

    // FIXME: 29.05.2022 add/remove noch hinzufügen

    /*
     *  Fügt der Kombination einen neuen Build hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn der Build schon in der Kombination ist
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addBuild (Category build) {
        if (this.builds.contains(build))
            return false;
        else
            return this.builds.add(build);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildCombination that = (BuildCombination) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(builds);
    }

    public String toString () {
        return builds.toString() + rating;
    }

    public List<Category> getBuilds() {
        return builds;
    }

    public String getName() {
        return name;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
