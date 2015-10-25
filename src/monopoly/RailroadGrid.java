package monopoly;

public class RailroadGrid extends BuyableGrid
{
    
    int property_rent_base; 
    
    /*Constructor*/
    public RailroadGrid(String property_name, int property_price, int property_mortgage)
    {
        super(PropertyGroup.RAILROAD, property_name, property_price, property_mortgage);
        this.property_rent_base = 25;
    }
    
    /*Add more class methods below?*/
    
    public int getRentPrice()
    { 
        /*
        ALGORITHM DESCRIPTION:
            Check how many *OTHER* railroad grids this owner has. Let the result be stored in Count.
            Then return property_rent_base * 2^Count
        */

        return 0;
    }
    
    /*Do this when a player lands on this grid*/
    public void landingFunction(Player landed)
    {
        
    }
    
}
