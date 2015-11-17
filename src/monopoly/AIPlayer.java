/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * AI Player
 * @author shaebrown
 */
public class AIPlayer extends AbstractPlayer {
    
    
    /**
     *  The amount the AI must have for to want to buy properties or houses
     */
    final int MINMONEY = 500; 
    
    /**
     * Creates a new AIPlayer
     */
    public AIPlayer ()
    {
        super();
    }

    /**
     * The command to make the AI roll the die, and display the die on the GUI
     * @return the die roll
     */
    @Override
    public int rollDie() {
        int diceRoll = Game.dice.newRoll();
        Game.dice_controller.displayRoll();
        return diceRoll;
    }

    /**
     * The command for the AI to make a decision to buy the property landed on
     * If the AI has more money than MINMONEY it will buy any property it lands on
     */
    @Override
    public void propertyDecision() {
        //This could be made more complex later
        BuyableGrid landed = (BuyableGrid) getCurrentGrid();
        int remaining_of_type = landed.property_group.MAX - this.getNumOfOwnedType(landed);
        if (getMoney() >= landed.getPrice())
        {
            if (getMoney() > MINMONEY || remaining_of_type == 1)
            this.buyProperty(landed);
        }
        //possibly add a notification that the AI bought this property
    }

    @Override
    public AbstractPlayer clone() {
        AIPlayer p = new AIPlayer();
        p.setName(this.name);
        p.setToken(this.token);
        p.setMoney(this.money); 
        p.setNumberOfJailFreeCards(this.jail_free_cards);
        p.setLocation(this.location);
        p.setJailStatus(this.isInJail());
        p.setProperties((LinkedList<BuyableGrid>)this.property.clone());
        return p;
    }

    /**
     * The AI performs any last tasks before its turn is over, 
     * such as pausing for a second
     */
    @Override
    public void finalizeTurn() {
        Game.dice_controller.enable();
        try {
            //wait a few seconds until proceeding
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The algorithm to decide which properties the AI should buy houses for
     */
    private void buyHouseDecision()
    {
        if (getMoney() < MINMONEY) return;
        for (BuyableGrid.PropertyGroup group : BuyableGrid.PropertyGroup.values())
        {
            if (group == BuyableGrid.PropertyGroup.UTILITY) break;
            if (group == BuyableGrid.PropertyGroup.RAILROAD) break;
            if (ownsAllType(group))
            {
               List<BuyableGrid> of_type = this.getPropertiesOfType(group);
               PropertyGrid least_houses = (PropertyGrid)of_type.get(0);
               for (BuyableGrid grid : of_type)
               {
                   PropertyGrid prop = (PropertyGrid)grid;
                   if (prop.getCurrentHouses() < least_houses.getCurrentHouses())
                       least_houses = prop;
               }
                if (getMoney() > least_houses.getHousePrice())
                    this.buyHouse(least_houses);
                    
            }
        }
    }

    /**
     * Tasks to perform when the AI begins its turn,
     * such as updating the menu, and deciding whether to buy houses
     */
    @Override
    public void beginTurn() {
        Game.dice_controller.enabled = false;
        Game.current_player = this;
        Game.player_controller.updateMenu(this); 
        buyHouseDecision();   
        if (isInJail())
            jailDecision();
    }

    @Override
    public void jailDecision() {
        //How will AI's decide to get out of jail
    }
    
}
