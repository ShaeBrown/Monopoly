package monopoly.protobuffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import monopoly.protobuffer.PlayerDataProto.Player;
import monopoly.protobuffer.PlayerDataProto.PlayerList;

public class PlayerData {
    final static String fileName = "playerdata";

    // Returns PlayerList if successfully loaded player data
    // Otherwise returns null and creates an empty player data file
    public static PlayerList loadPlayerList() throws IOException {
        PlayerList pl = null;

        // Read the existing player list.
        try {
            pl = PlayerList.parseFrom(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            createEmptyDataFile();
            return null;
        }

        // Check that the existing player list isn't null and that there are at least 2 players
        if (pl != null) {
            if (pl.getPCount() > 1) {
                return pl;
            }
        }

        createEmptyDataFile();
        return null;
    }

    // Saves one player to the data file
    public static void savePlayer(String name, int token, boolean ai, int money, int location,
            int jailCards, boolean inJail, boolean myTurn) throws IOException {

        System.out.println("Saving player: " + name);
        String fileName = "playerdata";
        PlayerList.Builder playerListBuilder = PlayerList.newBuilder();

        // Read the existing player list.
        try {
            playerListBuilder.mergeFrom(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Add a player.
        Player.Builder person = Player.newBuilder();
        person.setName(name);
        person.setToken(token + 1);
        person.setAi(ai);
        person.setMoney(money);
        person.setLocation(location);
        person.setJailFreeCards(jailCards);
        person.setInJail(inJail);
        person.setMyTurn(myTurn);
        person.build();
        playerListBuilder.addP(person);

        // Write the new address book back to disk.
        FileOutputStream output = new FileOutputStream(fileName);
        playerListBuilder.build().writeTo(output);
        output.close();
    }

    // Creates an empty data file
    public static void createEmptyDataFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
