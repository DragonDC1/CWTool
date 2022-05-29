package de.fida.cwtool;

import java.util.*;

public class PlayerCombination {
    String name;
    List<Player> players = new ArrayList<>();
    Rating rating;

    public PlayerCombination (String name, List<Player> players, Rating rating) {
        this.name = name;
        this.players = players;
        this.rating = rating;
    }

    // FIXME: 29.05.2022 Raus mit die Viecher
    public PlayerCombination (List<Player> players, Rating rating) {
        this.players = players;
        this.rating = rating;
    }

    public PlayerCombination (String name) {
        this.name = name;
    }

    /*
     *  Fügt der Kombination einen neuen Spieler hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn der Spieler schon in der Kombination ist
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addPlayer (Player player) {
        if (this.players.contains(player))
            return false;
        else
            return this.players.add(player);
    }

    /*
     *  Entfernt einen Spieler aus der Kombination
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn der Spieler nicht in der Kombination ist
     */
    public boolean removePlayer (Player player) {
        return this.players.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCombination that = (PlayerCombination) o;
        return Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }

    public String toString () {
        return players.toString() + rating;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }
}
