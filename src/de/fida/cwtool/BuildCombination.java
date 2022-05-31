package de.fida.cwtool;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class BuildCombination implements Serializable {
    @Serial
    private static final long serialVersionUID = 2983901898052264780L;
    String name;
    List<Category> listBuilds = new ArrayList<>();
    Rating rating;

    private Category[] builds = new Category[4];

    public BuildCombination (String name, List<Category> builds, Rating rating) {
        this.name = name;
        this.listBuilds = builds;
        this.rating = rating;
    }

    public BuildCombination (String name, Category[] builds, Rating rating) {
        this.name = name;
        this.builds = builds;
        this.rating = rating;
    }

    public BuildCombination (String name) {
        this.name = name;
    }

    /*
     *  Fügt der Kombination einen neuen Build hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn der Build schon in der Kombination ist
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addBuild (Category build) {
        if (this.listBuilds.contains(build))
            return false;
        else
            return this.listBuilds.add(build);
    }

    /*
     *  Entfernt einen Build aus der Kombination
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn der Build nicht in der Kombination ist
     */
    public boolean removeBuild (Category build) {
        return this.listBuilds.remove(build);
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
        return Objects.hash(listBuilds);
    }

    public String toString () {
        return listBuilds.toString() + rating;
    }

    public List<Category> getlistBuilds() {
        return listBuilds;
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

    public void setBuild(int index, Category build) {
        builds[index] = build;
    }

    public void setName(String name) {
        this.name = name;
    }
}
