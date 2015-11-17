package monopoly.protobuffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import monopoly.protobuffer.PlayerDataProto.PlayerList;

public class LoadProto {
	final static String fileName = "playerdata"; 
	
	// Returns PlayerList if successfully loaded player data
	// Otherwise returns null and creates an empty player data file
	public static PlayerList loadPlayerData() throws IOException {
    PlayerList pl = null;
    
    // Read the existing player list.
    try {
      pl = PlayerList.parseFrom(new FileInputStream(fileName));  
    } catch (FileNotFoundException e) {
//    	e.printStackTrace();
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
