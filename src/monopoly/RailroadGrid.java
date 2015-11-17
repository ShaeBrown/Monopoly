package monopoly;

/**
 * The railroad grids
 * 
 */
public class RailroadGrid extends BuyableGrid
{
    
    int property_rent_base; 
    
    /*Constructor*/

    /**
     * Create a new RailRoad grid
     * @param property_name the name of the railroad
     */

    public RailroadGrid(String property_name)
    {
        super(PropertyGroup.RAILROAD, property_name, 200, 100);
        this.property_rent_base = 25;
    }
    
    /*Add more class methods below?*/
    
    @Override
    public int getRentPrice()
    { 
        /*
        ALGORITHM DESCRIPTION:
            Check how many *OTHER* railroad grids this owner has. Let the result be stored in Count.
            Then return property_rent_base * 2^Count
        */

        return property_rent_base * 2^(getOwner().getNumOfOwnedType(this)-1);
    }
    
}
