package monopoly;

/**
 * The electric and water works grids
 * 
 */
public class UtilityGrid extends BuyableGrid
{
    
    /*Constructor*/

    /**
     * Create new Utility grid
     * @param property_name the name of the utility
     */

    public UtilityGrid(String property_name)
    {
        super(PropertyGroup.UTILITY, property_name, 150, 75);
    }
    
    /*Add more class methods below?*/
    
    @Override
    public int getRentPrice()
    {
        int diceroll = Game.dice.getRoll();   
        
        if (getOwner().ownsAllType(this))
            return 10 * diceroll;
        else
            return 4 * diceroll;
        /*
        ALGORITHM DESCRIPTION:
            Check if both Electric Company AND Water Works are owned. 
            If yes, return 10*diceroll
            If not, return 4*diceroll
        */
        
        
        
    }
    
    
}
