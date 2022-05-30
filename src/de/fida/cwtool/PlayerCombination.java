package de.fida.cwtool;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class PlayerCombination implements Serializable {
    @Serial
    private static final long serialVersionUID = 2704057640738783268L;
    String name;
    List<Player> listPlayers = new ArrayList<>();
    Rating rating;

    private Player[] players = new Player[4];

    public PlayerCombination(String name, List<Player> players, Rating rating) {
        this.name = name;
        this.listPlayers = players;
        this.rating = rating;
    }

    public PlayerCombination(String name) {
        this.name = name;
    }

    /*
     *  Fügt der Kombination einen neuen Spieler hinzu
     *  Gibt true zurück, wenn erfolgreich hinzugefügt
     *  Gibt false zurück, wenn der Spieler schon in der Kombination ist
     *  oder nicht hinzugefügt werden konnte
     */
    public boolean addPlayer(Player player) {
        if (this.listPlayers.contains(player)) return false;
        else return this.listPlayers.add(player);
    }

    /*
     *  Entfernt einen Spieler aus der Kombination
     *  Gibt true zurück, wenn erfolgreich entfernt
     *  Gibt false zurück, wenn der Spieler nicht in der Kombination ist
     */
    public boolean removePlayer(Player player) {
        return this.listPlayers.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCombination that = (PlayerCombination) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return listPlayers.toString() + rating;
    }

    public List<Player> getListPlayers() {
        return listPlayers;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(int index, Player player) {
        players[index] = player;
    }

    public Player[] getPlayers() {
        return players;
    }
}
