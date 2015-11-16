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
 *
 * @author shaebrown
 */
public class AIPlayer extends AbstractPlayer {
    
    final int MINMONEY = 500; // the amount the player must have for them to want to buy properties or houses
    
    public AIPlayer ()
    {
        super();
    }

    @Override
    public int rollDie() {
        int diceRoll = Game.dice.newRoll();
        Game.dice_controller.displayRoll();
        return diceRoll;
    }

    @Override
    public void propertyDecision() {
        /*this is how the AI decides whether or not to buy property when it is landed on
        A simple strategy is to buy every property which is landed on. This could be made more complex later
        */
         BuyableGrid landed = (BuyableGrid) getCurrentGrid();
        if (getMoney() > MINMONEY && getMoney() >= landed.getPrice())
        {
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

    @Override
    public void finalizeTurn() {
        try {
            //wait a few seconds until proceeding
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
               this.buyHouse(least_houses);
                    
            }
        }
    }

    @Override
    public void beginTurn() {
        Game.current_player = this;
        Game.player_controller.updateMenu(this); 
        buyHouseDecision();
        //this is where the AI player could decide to buy a house??
        
    
    }
    
}
