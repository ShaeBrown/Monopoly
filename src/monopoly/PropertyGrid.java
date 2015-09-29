package monopoly;

public class PropertyGrid extends BuyablePropertyClass{
    
     int property_house_unit_cost;   //Cost to build a house on the property
     int houses_built;               //How many houses are current built on this property? 0 = no houses built; 1-4 = number of houses built; 5 = hotel built
     int[] property_rent;            //Rent for different number of houses built on the property. This should be an array of precisely 6 elements.

    /*Constructor*/
    public PropertyGrid(PropertyGroup property_group,
                             String property_name,
                             int property_price,
                             int property_mortgage,
                             int property_house_unit_cost,
                             int[] property_rent)
    {
        super(property_group, property_name, property_price, property_mortgage);
        this.property_house_unit_cost = property_house_unit_cost;
        this.houses_built = 0;
        this.property_rent = property_rent;
    }
    
    /*Add more class methods below?*/
    
    public int getRentPrice()
    {
        return this.property_rent[houses_built];
    }
    
    /*Do this when a player lands on this grid*/
    public void landingFunction(Player landed)
    {
        
    }
}

    
