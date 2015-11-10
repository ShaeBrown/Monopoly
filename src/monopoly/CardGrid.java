/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;


/**
 *
 * @author Jacob
 */
public class CardGrid extends Grid
{
    public enum CardType {CHANCECARD, COMCHEST};
    
    Deck deck; //should be instantiated reference to the deck used in the game
    CardType type; //indicates the type of card
    
    public CardGrid(CardType type)
    {
        this.type = type;
    }
    
    @Override
    public void landingFunction(Player player)
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
        Game.player_controller.displayCard(type, card.title + "\n" + card.desc);
    }

}
