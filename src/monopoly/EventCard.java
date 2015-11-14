/*
    The EventCard class represents Chance and Community Chest cards. 
    There are several types of cards as defined by ActionType, each type has a unique function:

    -GAINMONEY type adds a fixed amount to a player's bank (defined by the variable "amount").
    -LOSEMONEY type deducts a fixed amount from a player's bank (defined by the variable "amount").
    -GAINMONEY_FROMEVERYONE type collects a fixed amount for every other player in the game (defined by the variable "amount"), and adds the total to the using player's bank.
    -LOSEMONEY_TOEVERYONE type deducts a fixed amount from the using player's bank (defined by the variable "amount"), and give it to every other player in the game
    -LOSEMONEYPROPERTY type deducts an amount based on the number houses and hotels the player has.
    -MOVETO type "teleports" a player to another grid on the board
        -An example card is "Advance to GO" (defined by the variable "amount" as an array index to the location).
    -MOVETO_CLOSEST type "teleports" a player to the closest grid of type PropertyGroup (defined by the variable property_group). 
        -An example card is "Move a player to the nearest railroad station".
    -GOBACK type moves a player back by a fixed number of grids (defined by the variable "amount").
        -An example card is "Go back 3 spaces".
    -GOTOJAIL type simply sends the card user to jail. 
    -JAILFREECARD type gives the using player a get out of jail free card. 
    -HOUSEREPAIR type deducts a fixed amount from the user's bank for every house built (defined by the variable "amount"), as well as a seperate fixed amount for every hotel built (defined by the variable "amount_secondary")

    Just like the Grid class, useCard() function of this class should be called when a card is used. This function then calls the appropriate handling function, depending on the card's action type.
*/

package monopoly;

import java.util.List;

import monopoly.Game;
import monopoly.Game.GRIDNUM;

public class EventCard {
    
    public enum ActionType {GAINMONEY, LOSEMONEY, GAINMONEY_FROMEVERYONE, LOSEMONEY_TOEVERYONE, LOSEMONEYPROPERTY, MOVETO, MOVETO_CLOSEST, GOBACK, GOTOJAIL, JAILFREECARD, HOUSEREPAIR}
    
    ActionType action_type;                 //What kind of action will be performed by this card?
    String title, desc;                     //Title and description of the card
    int amount;                             //How much to gain, lose, index of grid to moveto, etc...
    int amount_secondary;                   //Used by HOUSEREPAIR only
    
    /*Constructor for most action types*/
    public EventCard(String title, String desc, ActionType action_type, int amount)
    {
        /*typechecking*/
        switch(action_type)
        {
            /*These cases are left "empty" on purpose. The code under "case GOBACK" will run if action_type is ANY of these cases*/
            case GAINMONEY:
            case LOSEMONEY:
            case GAINMONEY_FROMEVERYONE:
            case LOSEMONEY_TOEVERYONE:
            case LOSEMONEYPROPERTY:
            case MOVETO:
            case GOBACK:
                this.action_type = action_type;
                this.title = title;
                this.desc = desc;
                this.amount = amount;
                break;
            default:
                System.out.println("Wrong constructor called. Found: " +action_type +" Expected GAINMONEY, LOSEMONEY... whatever");
                break;
                
        }
    }
    
    /*Constructor for HOUSEREPAIR*/
    public EventCard(String title, String desc, ActionType action_type, int amount, int amount_secondary)
    {
        /*typechecking*/
        switch(action_type)
        {
            case HOUSEREPAIR:
                this.action_type = action_type;
                this.title = title;
                this.desc = desc;
                this.amount = amount;
                this.amount_secondary = amount_secondary;
                break;
            default:
                System.out.println("Wrong constructor called. Found: " +action_type +" Expected HOUSEREPAIR");
                break;
                
        }
    }
    
     /*Constructor intended for GOTOJAIL and JAILFREECARD*/
    public EventCard(String title, String desc, ActionType action_type)
    {
        /*typechecking*/
        switch(action_type)
        {
            case JAILFREECARD:
			case GOTOJAIL:
                this.action_type = action_type;
                this.title = title;
                this.desc = desc;
                break;
            default:
                System.out.println("Wrong constructor called. Found: " +action_type +" Expected GOTOJAIL");
                break;
        }
    }
    
    /*insert more methods below*/
    
    /*Call the correct effect function depending on the action type of this card*/
    public EventCard useCard(List<AbstractPlayer> playerList, AbstractPlayer player) {
        switch(this.action_type)
        {
            /*These cases are left "empty" on purpose. The code under "case GOBACK" will run if action_type is ANY of these cases*/
            case GAINMONEY:
                gainmoney_effect(player);
                break;
            case LOSEMONEY:
                losemoney_effect(player);
                break;
            case GAINMONEY_FROMEVERYONE:
                gaminmoney_fromeveryone_effect(playerList, player);
                break;
            case LOSEMONEY_TOEVERYONE:
                losemoney_toeveryone_effect(playerList, player);
                break;
            case LOSEMONEYPROPERTY:
              losemoney_property(player);
              break;
            case MOVETO:
                moveto_effect(player);
                break;
            case MOVETO_CLOSEST:
                moveto_closest_effect(player);
                break;
            case GOBACK:
                goback_effect(player);
                break;
            case GOTOJAIL:
                gotojail_effect(player);
                break;
            case JAILFREECARD:
                jailfreecard_effect(player);
                break;
            case HOUSEREPAIR:
                houserepair_effect(player);
                break;
            default:
                System.out.println("Unexpected error?!");
                break;
        }
        return this;
    }
    
    /*What effect does a GAINMONEY card have on a player?*/
    private void gainmoney_effect(AbstractPlayer player) {
        player.addMoney(this.amount);
    }
    
    /*What effect does a LOSEMONEY card have on a player?*/
    private void losemoney_effect(AbstractPlayer player) {
        player.removeMoney(this.amount);
    }
    
    /*What effect does a GAINMONEY_FROMEVERYONE card have on a player?*/
    private void gaminmoney_fromeveryone_effect(List<AbstractPlayer> playerList, AbstractPlayer player) {
    	for (AbstractPlayer p : playerList) {
    		if (p.getMoney() < this.amount) {
// TODO: Remove player p because they're bankrupt
	      	player.addMoney(p.getMoney());
    		} else {		
	    		p.removeMoney(this.amount);
	      	player.addMoney(this.amount);
    		}
    	}    	
    }
    
    /*What effect does a LOSEMONEY_TOEVERYONE card have on a player?*/
    private void losemoney_toeveryone_effect(List<AbstractPlayer> playerList, AbstractPlayer player) {
    	int numPlayers = playerList.size();
    	
    	if (player.getMoney() < (this.amount * numPlayers)) {
// TODO: Remove player because they're bankrupt   		
    		// Distribute all the money the player has left
    		int leftover = player.getMoney() / numPlayers;
	    	for (AbstractPlayer p : playerList) {
	    		p.addMoney(leftover);
	    	}  
    	} else {  	
	    	for (AbstractPlayer p : playerList) {
	    		p.addMoney(this.amount);
	    		player.removeMoney(this.amount);
	    	}  
    	}
    }
    
    private void losemoney_property(AbstractPlayer player) {
    	// I don't think this is actually used? Same as HOUSE_REPAIR
    }
    
    /*What effect does a MOVETO card have on a player?*/
    // is this supposed to be relative or absolute??
    //the advance to GO card moves the player 0 spaces.
    private void moveto_effect(AbstractPlayer player) {
    	// Move player and if they pass go they collect $200
    	int location = player.getLocation() + this.amount;
      if(location > (Game.BOARDSIZE - 1)) {
      	location = location - (Game.BOARDSIZE - 1);
        player.addMoney(200);
      }    
      player.setLocation(location);
    }
    
    /*What effect does a MOVETO_CLOSEST card have on a player?*/
    private void moveto_closest_effect(AbstractPlayer player) {
// TODO: Implement this method
        /*
            for each grid in the gameboard
                if current grid is not the same type as property_group
                    continue
                else move the player to this location 
        */
    }
    
    /*What effect does a GOBACK card have on a player?*/
    private void goback_effect(AbstractPlayer player) {
    	int location = player.getLocation();
    	location -= 3;
    	if (location >= 0) {
        player.setLocation(location);
    	} else {
    		location = (Game.BOARDSIZE - location);
    	}
    }
    
    /*What effect does a GOTOJAIL card have on a player?*/
    private void gotojail_effect(AbstractPlayer player) {
// TODO: Add jail effect
    	player.setLocation(GRIDNUM.Jail.getNum());
    }
    
    /*What effect does a JAILFREECARD card have on a player?*/
    private void jailfreecard_effect(AbstractPlayer player) {
        player.addJailFreeCard();
    }
    
    /*What effect does a HOUSEREPAIR card have on a player?*/
    private void houserepair_effect(AbstractPlayer player) {
// TODO: Implement this method.
    }  
}
