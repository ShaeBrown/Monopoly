package monopoly;

public class UtilityGrid extends BuyableGrid
{
    
    /*Constructor*/
    public UtilityGrid(String property_name)
    {
        super(PropertyGroup.UTILITY, property_name, 150, 75);
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
