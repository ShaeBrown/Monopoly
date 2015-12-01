package monopoly;

/**
 * The properties you can buy and build on
 * 
 */
public class PropertyGrid extends BuyableGrid
{   
    int property_house_unit_cost;   //Cost to build a house on the property
    int houses;                     //How many houses are current built on this property? 0 = no houses built; 1-4 = number of houses built; 5 = hotel built
    int hotels;                     //How many hotels are on this property?
    int[] property_rent;            //Rent for different number of houses built on the property. This should be an array of precisely 6 elements.

    /**
     * The maximum number of houses
     */
    public final int MAX_NUMBER_HOUSES = 4;
    
    public final int MAX_NUMBER_HOTELS = 1;
    
    /*Constructor*/

    /**
     * Creates a new property grid
     * @param property_group the group of the property
     * @param property_name the name of the property
     * @param property_price the price to buy the property
     * @param property_mortgage the amount the property can be mortgaged for
     * @param property_rent the cost of rent with 0,1,2,3,4 houses or hotel
     */

    public PropertyGrid(PropertyGroup property_group, String property_name, int property_price, int property_mortgage, int[] property_rent)
    {
        super(property_group, property_name, property_price, property_mortgage);
        this.houses = this.hotels = 0;
        this.property_rent = property_rent;
        switch (property_group) {
            case BROWN:
            case LIGHTBLUE:
                property_house_unit_cost = 50;
            case MAGENTA:
            case ORANGE:
                property_house_unit_cost = 100;
            case RED:
            case YELLOW:
                property_house_unit_cost = 150;
            case GREEN:
            case BLUE:
                property_house_unit_cost = 200;
                
        }
    }
    
    /*Add more class methods below?*/
    
    @Override
    public int getRentPrice()
    {
        return this.property_rent[houses];
    }
    
    /**
     * Get the price to purchase a house for the property
     * @return the price
     */
    public int getHousePrice() {
    	return this.property_house_unit_cost;
    }
    
    /**
     * Get the number of houses on the property
     * @return the number of houses on the property
     */
    public int getCurrentHouses() {
    	return this.houses;
    }
    
    /**
     * Get the number of hotels on the property
     * @return the number of hotels on the property
     */
    public int getCurrentHotels() {
    	return this.hotels;
    }
    
    /**
     * Add a house to the property.
     * Updates GUI with the new house
     */
    public void addHouse()
    {
        this.houses++;
        Game.object_controller.addHouseorHotelIcon(this);
    }
    
    /**
     * Add a hotel to the property.
     */
    public void addHotel()
    {
        if (this.houses >= MAX_NUMBER_HOUSES && this.hotels < MAX_NUMBER_HOTELS)
        {
            this.houses -= MAX_NUMBER_HOUSES;
            this.hotels++;
            Game.object_controller.addHouseorHotelIcon(this);
        }
    }
}

    
