/* This class represents the deck of cards for the Chance cards and the Community Chest cards.
 * 
 * The only public methods that should be called are:
 * drawChanceCard(Player p) - draws the top card off the chance stack
 * drawCommunityChestCard(Player p) - draws the top card off the community chest stack
 * 
 */
package monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	
	List<AbstractPlayer> playerList;						// Reference to the list of players in the game
	
	public Deck(List<AbstractPlayer> playerList) {
		this.playerList = playerList;
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
    chance_cards[9] = new EventCard("Make general repairs on all your property", "For each house pay $25 - For each hotel $100", ActionType.HOUSEREPAIR, 25, 100);
    chance_cards[10] = new EventCard("Pay poor tax of $25", "", ActionType.LOSEMONEY, 15);
    chance_cards[11] = new EventCard("Take a trip to Reading Railroad", "If you pass Go, collect $200", ActionType.MOVETO, GRIDNUM.ReadingRailroad.getNum());
    chance_cards[12] = new EventCard("Take a walk on the Boardwalk", "Advance token to Boardwalk", ActionType.MOVETO, GRIDNUM.Boardwalk.getNum());
    chance_cards[13] = new EventCard("You have been elected Chariman of the Board", "Pay each player $50", ActionType.LOSEMONEY_TOEVERYONE, 50);
    chance_cards[14] = new EventCard("Your building and loan matures", "Collect $150", ActionType.GAINMONEY, 150);
    chance_cards[15] = new EventCard("You have won a crossword competition", "Collect $100", ActionType.GAINMONEY, 100);		
	}
	
	private void initializeCommunityChest() {
    /*http://monopoly.wikia.com/wiki/Community_Chest*/
    comchest_cards[0] = new EventCard("Advance to Go", "Collect $200", ActionType.MOVETO, 0);
    comchest_cards[1] = new EventCard("Bank Error", "Collect $200", ActionType.GAINMONEY, 200);
    comchest_cards[2] = new EventCard("Doctor's fees", "Pay $50", ActionType.LOSEMONEY, 50);
    comchest_cards[3] = new EventCard("From sale of stock you get $50", "", ActionType.GAINMONEY, 50);
    comchest_cards[4] = new EventCard("Get Out of Jail Free", "This card may be kept until needed or sold", ActionType.JAILFREECARD);
    comchest_cards[5] = new EventCard("Go to Jail", "Go directly to Jail - Do not pass GO, do not collect $200", ActionType.GOTOJAIL);
    comchest_cards[6] = new EventCard("Grand Opera Night", "Collect $50 from every player for opening night seats", ActionType.GAINMONEY_FROMEVERYONE, 50);
    comchest_cards[7] = new EventCard("Holiday Xmas Fund matures", "Collect $100", ActionType.GAINMONEY, 100);
    comchest_cards[8] = new EventCard("Income tax refund", "Collect $20", ActionType.GAINMONEY, 20);
    comchest_cards[9] = new EventCard("It is your birthday", "Collect $10 from each player", ActionType.GAINMONEY_FROMEVERYONE, 10);
    comchest_cards[10] = new EventCard("Life insurance matures", "Collect $100", ActionType.GAINMONEY, 100);
    comchest_cards[11] = new EventCard("Pay hospital fees of $100", "", ActionType.LOSEMONEY, 100);
    comchest_cards[12] = new EventCard("Pay school fees of $150", "", ActionType.LOSEMONEY, 150);
    comchest_cards[13] = new EventCard("Receive $25 consultancy fee", "", ActionType.GAINMONEY, 25);
    comchest_cards[14] = new EventCard("You are assessed for street repairs", "Pay $40 per house and $115 per hotel", ActionType.HOUSEREPAIR, 45, 115);
    comchest_cards[15] = new EventCard("You have won second prize in a beauty contest", "Collect $10", ActionType.GAINMONEY, 10);
    comchest_cards[16] = new EventCard("You inherit $100", "", ActionType.GAINMONEY, 100);
	}
	
	// Randomly put all of the chance cards into the stack.
	private void shuffleChance() {
		List<EventCard> randomChanceCards = new ArrayList<>();
		
		for (EventCard c : chance_cards) {
			randomChanceCards.add(c);
		}
		Collections.shuffle(randomChanceCards);
		
		for (EventCard c: randomChanceCards) {
			chanceStack.push(c);
		}
	}
	
	// Randomly put all of the community chest cards into the stack.
	private void shuffleCommunityChest() {
		List<EventCard> randomCommunityChestCards = new ArrayList<>();
		
		for (EventCard c : comchest_cards) {
			randomCommunityChestCards.add(c);
		}
		Collections.shuffle(randomCommunityChestCards);
		
		for (EventCard c: randomCommunityChestCards) {
			communityChestStack.push(c);
		}	
	}
	
	// Draw the top card off the chance stack.
	public EventCard drawChanceCard(AbstractPlayer p) {
		System.out.println("Drawing chance card.");
		
		if (chanceStack.size() <= 0) {
			shuffleChance();
		}
		
		EventCard c = chanceStack.pop();
		
		if (c != null)
			return c.useCard(playerList, p);
                
                return null;
	}
	
	// Draw the top card off the community chest stack.
	public EventCard drawCommunityChestCard(AbstractPlayer p) {
		System.out.println("Drawing community chest card.");
		
		if (communityChestStack.size() <= 0) {
			shuffleCommunityChest();
		}
		
		EventCard c = communityChestStack.pop();
		
		if (c != null)
			return c.useCard(playerList, p);
                
                return null;
	}
}
