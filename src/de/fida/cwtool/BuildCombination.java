package de.fida.cwtool;

import java.io.Serializable;
import java.util.*;

public class BuildCombination implements Serializable {
    List<Category> builds = new ArrayList<>();
    Rating rating;

    public BuildCombination (List<Category> builds, Rating rating) {
        this.builds = builds;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildCombination that = (BuildCombination) o;
        return Objects.equals(builds, that.builds);
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

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
