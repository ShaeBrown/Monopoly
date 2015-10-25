/* This class represents the deck of cards for the Chance cards and the Community Chest cards.
 * 
 * The only public methods that should be called are:
 * drawChanceCard() - draws the top card off the chance stack
 * drawCommunityChestCard() - draws the top card off the community chest stack
 * 
 */
package monopoly;

import java.util.Stack;

import monopoly.EventCard.ActionType;
import monopoly.Game.GRIDNUM;

public class Deck {
  final int CHANCECOUNT = 16;         //How many chance cards are in the game
  final int COMCHESTCOUNT = 17;       //How many community chest cards are in the game
	
  EventCard[] chance_cards;           //Array of EventCard that functions as a deck of chance cards
  EventCard[] comchest_cards;         //Array of EventCard that functions as a deck of community chest cards
  
	Stack<EventCard> chanceStack = new Stack<EventCard>();
	Stack<EventCard> communityChestStack = new Stack<EventCard>();
	
	public Deck() {
    chance_cards = new EventCard[CHANCECOUNT];
    comchest_cards = new EventCard[COMCHESTCOUNT];
    
    initializeChance();
		initializeCommunityChest();
		
		shuffleChance();
		shuffleCommunityChest();
	}
	
	// Create all of the chance cards.
	private void initializeChance() {
    chance_cards[0] = new EventCard("Advance to Go", "Collect $200", ActionType.MOVETO, GRIDNUM.GO.getNum());
    chance_cards[1] = new EventCard("Advance to Illinois Ave.", "If you pass Go, collect $200", ActionType.MOVETO, GRIDNUM.IllinoisAve.getNum());
    chance_cards[2] = new EventCard("Advance to St. Charles Place", "If you pass Go, collect $200", ActionType.MOVETO, GRIDNUM.StCharlesPlace.getNum());
    /*chance_cards[3] = 
    		new EventCard("Advance token to nearest Utility", 
    									"If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times the amount thrown.",
    									ActionType.MOVETO_CLOSEST,
    									PropertyGroup.UTILITY);
    chance_cards[4] = 
    		new EventCard("Advance token to nearest Railroad",
				    					"If unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which he/she is otherwise entitled.",
				    					ActionType.MOVETO_CLOSEST,
				    					PropertyGroup.RAILROAD);*/
    chance_cards[5] = new EventCard("Bank pays you divdend of $50", "", ActionType.GAINMONEY, 50);
    chance_cards[6] = new EventCard("Get out of Jail free", "This card may be kept until needed, or traded/sold", ActionType.JAILFREECARD);
    chance_cards[7] = new EventCard("Go Back 3 spaces", "", ActionType.GOBACK, 3);
    chance_cards[8] = new EventCard("Go to Jail", "Go directly to Jail - Do not pass GO, do not collect $200", ActionType.GOTOJAIL);
    chance_cards[9] = new EventCard("Make general repairs on all your property", "For each house pay $25 - For each hotel $100", ActionType.LOSEMONEYPROPERTY);
    chance_cards[10] = new EventCard("Pay poor tax of $25", "", ActionType.LOSEMONEY, 15);
    chance_cards[11] = new EventCard("Take a trip to Reading Railroad", "If you pass Go, collect $200", ActionType.MOVETO, GRIDNUM.ReadingRailroad.getNum());
    chance_cards[12] = new EventCard("Take a walk on the Boardwalk", "Advance token to Boardwalk", ActionType.MOVETO, GRIDNUM.Boardwalk.getNum());
    chance_cards[13] = new EventCard("You have been elected Chariman of the Board", "Pay each player $50", ActionType.LOSEMONEY_TOEVERYONE, 50);
    chance_cards[14] = new EventCard("Your building and loan matures", "Collect $150", ActionType.GAINMONEY, 150);
    chance_cards[15] = new EventCard("You have won a crossword competition", "Collect $100", ActionType.GAINMONEY, 100);		
	}
	
	private void initializeCommunityChest() {
    /*http://monopoly.wikia.com/wiki/Community_Chest*/
    
    comchest_cards[0] = new EventCard("Advance to Go", "Collect $200" ,ActionType.MOVETO, 0);
    /*fill more...*/
	}
	
	// Randomly put all of the chance cards into the stack.
	private void shuffleChance() {
		
	}
	
	// Randomly put all of the community chest cards into the stack.
	private void shuffleCommunityChest() {
		
	}
	
	// Draw the top card off the chance stack.
	public void drawChanceCard() {
		System.out.println("Drawing chance card.");
	}
	
	// Draw the top card off the community chest stack.
	public void drawCommunityChestCard() {
		System.out.println("Drawing community chest card.");
	}
}
