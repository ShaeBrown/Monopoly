package monopoly;

public class PropertyGrid extends BuyableGrid
{   
    int property_house_unit_cost;   //Cost to build a house on the property
    int houses;               //How many houses are current built on this property? 0 = no houses built; 1-4 = number of houses built; 5 = hotel built
    int hotels;
    int[] property_rent;            //Rent for different number of houses built on the property. This should be an array of precisely 6 elements.
    public final int MAX_NUMBER_HOUSES = 4;
    
    /*Constructor*/
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
    
    public int getHousePrice() {
    	return this.property_house_unit_cost;
    }
    
    public int getCurrentHouses() {
    	return this.houses;
    }
    
    public int getCurrentHotels() {
    	return this.hotels;
    }
    
    public void addHouse()
    {
        this.houses++;
        Game.grid_controller.addHouseIcon(this);
    }
    
    public void addHotel()
    {
        if (this.houses >= MAX_NUMBER_HOUSES)
        {
            this.houses -= MAX_NUMBER_HOUSES;
            this.hotels++;
        }
    }
}

    
