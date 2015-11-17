package monopoly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * A player controlled by a human
 * 
 */
public class HumanPlayer extends AbstractPlayer{
 

    /**
     * Creates a new player
     */

    public HumanPlayer ()
    {
        super();
    }
    
    /**
     * Waits for the player to click the dice buttons
     * @return the dice roll
     */
    @Override
    public int rollDie()
    {
        
        /* The code below waits for the rolling player to click the dice buttons
        Instead of a empty while loop which wasn't working I took a solution from
        http://stackoverflow.com/questions/8409609/java-empty-while-loops
        it works okay for now, but please change it if you have a better solution
        */
        Object LOCK = Game.dice_controller.getLock();
        synchronized (LOCK) {
            while (Game.dice_controller.isEnabled()) {
                try { LOCK.wait(); }
                catch (InterruptedException e) {
                // treat interrupt as exit request
                    break;
                }
            }
        }
        
        return Game.dice.getRoll();
    }
    
    /**
     * Opens a dialog to ask the player if they want to buy the property
     */
    @Override
    public void propertyDecision() 
    {
        Game.player_controller.buyProperty(this);
    }
    
    /**
     * Clones a human player
     * @return the newly cloned player
     */
    public AbstractPlayer clone()
    {
        HumanPlayer p = new HumanPlayer();
        p.setName(this.name);
        p.setToken(this.token);
        p.setMoney(this.money); 
        p.setNumberOfJailFreeCards(this.jail_free_cards);
        p.setLocation(this.location);
        p.setJailStatus(this.isInJail());
        p.setProperties((LinkedList<BuyableGrid>)this.property.clone());
        return p;
    }

    @Override
    public void finalizeTurn() {
        Game.dice_controller.enable();
    }

    @Override
    public void beginTurn() {
        Game.current_player = this;
        Game.player_controller.updateMenu(this); 
        if (isInJail())
            jailDecision();
        //this is where we could check for any trading requests,
        //or where we could ask the player to decide what they want to do
        //to get out of jail
    }

    @Override
    public void jailDecision() {
       //Open a dialog
    }
    
}
