package de.fida.cwtool;

import de.fida.cwtool.Clan;
import de.fida.cwtool.gui.Window;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Debug {
    public static void main(String[] args) throws Exception {
        Clan jkdo = loadClan("clan.txt");

        /*URL url = new URL("https://raw.githubusercontent.com/DragonDC1/CWTool/master/files/clan.txt");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();*/

        /*Player finn = new Player("DragonDC_");
        finn.addBuild(new Build(CW_Build.DOG, false));
        finn.addBuild(new Build(CW_Build.RETCHER, false));
        finn.addBuild(new Build(CW_Build.SCORP, false));

        Player daniel = new Player("Daniel_St00");
        daniel.addBuild(new Build(CW_Build.DOG, true));
        daniel.addBuild(new Build(CW_Build.SPINNE, false));
        daniel.addBuild(new Build(CW_Build.PHOON, false));

        Player saft = new Player("Saftbube_TPR55");
        saft.addBuild(new Build(CW_Build.SCORP, true));
        saft.addBuild(new Build(CW_Build.DOG, true));
        saft.addBuild(new Build(CW_Build.PORC, true));
        saft.addBuild(new Build(CW_Build.SPINNE, true));

        Player kaiser = new Player("KaiserNicholas");
        kaiser.addBuild(new Build(CW_Build.PHOON, false));
        kaiser.addBuild(new Build(CW_Build.PORC, true));
        kaiser.addBuild(new Build(CW_Build.SPINNE, false));

        // System.out.println(player);

        //player.removeBuild(new Build(CW_Build.SCORP, true));

// Test
        // fejfoIUHFOWAFGsiofhweigbsifgew
        // efijewoiguoghgodshunfgoweaignoerihg

        //player.updateDoppler(CW_Build.DOG, false);


        //System.out.println(player);
        List<Player> players = new ArrayList<>();
        players.add(finn);*/
        //Clan jkdo = new Clan("JKDO");

        System.out.println(jkdo);
        System.out.println(jkdo.getAllDopplerBuilds());

        System.out.println(jkdo);
        System.out.println(jkdo.getPlayer("DragonDC_"));
        /*jkdo.addPlayerCombination("Standard");
        jkdo.getPlayerCombination("Standard").addPlayer(finn);
        jkdo.getPlayerCombination("Standard").addPlayer(daniel);
        jkdo.getPlayerCombination("Standard").addPlayer(saft);
        jkdo.getPlayerCombination("Standard").addPlayer(kaiser);
        System.out.println(jkdo.getPlayerCombination("Standard"));*/
        /*jkdo.addBuildCombination(List.of(Category.RANGE, Category.PORC, Category.RANGE, Category.MELEE), Rating.SEHR_GUT);
        System.out.println(jkdo.getBuildCombinations());
        jkdo.changeRatingBuildCombination(List.of(Category.RANGE, Category.PORC, Category.RANGE, Category.MELEE), Rating.GOLD);
        System.out.println(jkdo.getBuildCombinations());
        jkdo.addBuildCombination(List.of(Category.SPINNE, Category.RANGE, Category.PORC, Category.MELEE), Rating.PAK4);
        System.out.println(jkdo.getBuildCombinations());
        jkdo.changeRatingBuildCombination(List.of(Category.SPINNE, Category.RANGE, Category.PORC, Category.MELEE), Rating.SCHLECHT);
        System.out.println(jkdo.getBuildCombinations());
        jkdo.removeBuildCombination(List.of(Category.SPINNE, Category.RANGE, Category.PORC, Category.MELEE));
        System.out.println(jkdo.getBuildCombinations());
        System.out.println(jkdo.addBuildCombination(List.of(Category.RANGE, Category.PORC, Category.RANGE, Category.MELEE), Rating.GUT));
        System.out.println(jkdo.getBuildCombinations());*/

        Window window = new Window();
        window.init(jkdo);
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
