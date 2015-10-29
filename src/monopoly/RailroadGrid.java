package monopoly;

public class RailroadGrid extends BuyableGrid
{
    
    int property_rent_base; 
    
    /*Constructor*/
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

        return 0;
    }
    
}
