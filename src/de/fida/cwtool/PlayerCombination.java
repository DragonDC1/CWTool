package de.fida.cwtool;

import java.util.*;

public class PlayerCombination {
    List<Player> player = new ArrayList<>();
    Rating rating;

    public PlayerCombination (List<Player> player, Rating rating) {
        this.player = player;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCombination that = (PlayerCombination) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    public String toString () {
        return player.toString() + rating;
    }

    public List<Player> getPlayer() {
        return player;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
