package monopoly.protobuffer;
import java.io.*;

import monopoly.protobuffer.PlayerDataProto.Player;
import monopoly.protobuffer.PlayerDataProto.PlayerList;


public class SaveProto {
	
	public static void savePlayerData(String name, int token, boolean ai) throws IOException {
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
    person.setToken(token);
    
// TODO
    person.setAi(ai);
    person.setMoney(1500);
    person.setLocation(0);
    person.setJailFreeCards(0);
    person.setInJail(false);
    
    person.build();
    playerListBuilder.addP(person);

	  // Write the new address book back to disk.
	  FileOutputStream output = new FileOutputStream(fileName);
	  playerListBuilder.build().writeTo(output);
	  output.close();
	}	
}
