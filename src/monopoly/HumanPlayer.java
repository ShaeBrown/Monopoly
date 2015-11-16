package monopoly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class HumanPlayer extends AbstractPlayer{
        /*New Player constructor*/
    public HumanPlayer ()
    {
        super();
    }
    
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
    
    @Override
    public void propertyDecision() 
    {
        Game.player_controller.buyProperty(this);
    }
    
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
        //this is where we could check for any trading requests,
        //or where we could ask the player to decide what they want to do
        //to get out of jail
    }
    
}
