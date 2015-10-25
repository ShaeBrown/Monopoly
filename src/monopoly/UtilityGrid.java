package monopoly;

public class UtilityGrid extends BuyableGrid
{
    
    /*Constructor*/
    public UtilityGrid(String property_name, int property_price, int property_mortgage)
    {
        super(PropertyGroup.UTILITY, property_name, property_price, property_mortgage);
    }
    
    /*Add more class methods below?*/
    
    public int getRentPrice(int diceroll)
    {
        /*
        ALGORITHM DESCRIPTION:
            Check if both Electric Company AND Water Works are owned. 
            If yes, return 10*diceroll
            If not, return 4*diceroll
        */
        return 0;
    }
    
    /*Do this when a player lands on this grid*/
    public void landingFunction(Player landed)
    {
        
    }
    
}
