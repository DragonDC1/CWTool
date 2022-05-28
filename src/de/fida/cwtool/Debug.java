package de.fida.cwtool;

import de.fida.cwtool.Clan;
import de.fida.cwtool.gui.Window;

import java.io.*;
import java.util.*;

public class Debug {
    public static void main(String[] args) throws Exception {
        //Clan clan = loadClan("player.ser");

        Player player = new Player("Test");
        player.addBuild(new Build(CW_Build.DOG, true));
        player.addBuild(new Build(CW_Build.PHOON, false));
        player.addBuild(new Build(CW_Build.SCORP, true));

        Player player2 = new Player("Hallo");
        player2.addBuild(new Build(CW_Build.DOG, false));
        player2.addBuild(new Build(CW_Build.RETCHER, true));

        System.out.println(player);


        //player.removeBuild(new Build(CW_Build.SCORP, true));

// Test

        //player.updateDoppler(CW_Build.DOG, false);

        System.out.println(player);
        List<Player> players = new ArrayList<>();
        players.add(player);
        Clan clan = new Clan("Test", players);
        clan.addMember(player2);

        System.out.println(clan.getAllDopplerBuilds());

        System.out.println(clan);
        System.out.println(clan.getPlayer("Test"));
        clan.getAllDopplerBuilds();

        Window window = new Window();
        window.init(clan);

        clan.saveToFile("player.ser");
    }

    private static Clan loadClan(String name) {
        try {
            File file = new File("files/" + name);

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Clan clan = (Clan) ois.readObject();

            ois.close();

            return clan;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
