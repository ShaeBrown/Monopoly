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
    
    public void landingFunction(Player player)
    {
        //here a card will be drawn from the deck and it's action will be performed on the player
        if (type == CardType.CHANCECARD)
        {
            
        }
        else
        {
            
        }
    }

}
