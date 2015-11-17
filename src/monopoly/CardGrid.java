/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;


/**
 * A Chance or Community Chest grid
 * @author Jacob
 */
public class CardGrid extends Grid
{

    /**
     * The type of card grid, Chance or Community Chest
     */
    public enum CardType { CHANCECARD,  COMCHEST };
    

    /**
     * The type of card grid, Chance or Community Chest
     */
    CardType type;
    
    /**
     * Creates a new Chance or Community Chest grid
     * @param type Chance or CommunityChest grid
     */
    public CardGrid(CardType type)
    {
        this.type = type;
    }
    
    /**
     * Draws a card from the corresponding grid and performs the action
     * @param player the player that landed on the grid
     */
    @Override
    public void landingFunction(AbstractPlayer player)
    {
        EventCard card;
        if (type == CardType.CHANCECARD)
        {
            card = Game.deck.drawChanceCard(player);
        }
        else
        {
            card = Game.deck.drawCommunityChestCard(player);
        }
        if (card != null)
            Game.player_controller.displayCard(type, card.title + "\n" + card.desc);
        else
            System.err.println("Drawn card from " + type + " was null");
    }

}
