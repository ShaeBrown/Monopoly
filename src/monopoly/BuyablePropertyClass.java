/*
    The BuyablePropertyClass hosts all the common data needed by PropertyGrid, RailroadGrid, and UtilityGrid.
    Do not call the constructor of this class on its own, it should be automatically called by any of the subclasses extending this class.
*/

package monopoly;

public class BuyablePropertyClass {
    
    enum PropertyGroup {RED, YELLOW, GREEN, BLUE, BROWN, LIGHTBLUE, MAGENTA, ORANGE, UTILITY, RAILROAD}
    
    Player owner;                   //Which player owns this?
    PropertyGroup property_group;   //Which property group does this belongs to?
    String property_name;           //Name of the property?
    int property_price;             //Cost to buy the initial property  
    int property_mortgage;          //Mortgage value of the property
    
    /*Constructor*/
    public BuyablePropertyClass(PropertyGroup property_group,
                           String property_name,
                           int property_price,
                           int property_mortgage)
    {
        this.owner = null;
        this.property_group = property_group;
        this.property_name = property_name;
        this.property_price = property_price;
        this.property_mortgage = property_mortgage;
    }
    
    /*Add more class methods below?*/
    
    public Player getOwner()
    {
        return this.owner;
    }
    
    public void setOwner(Player owner)
    {
        this.owner = owner;
    }
    
    public PropertyGroup getPropertyGroup()
    {
        return this.property_group;
    }
    
    public void setPropertyGroup(PropertyGroup property_group)
    {
        this.property_group = property_group;
    }
    
    public String getName()
    {
        return this.property_name;
    }
    
    public void setName(String property_name)
    {
        this.property_name = property_name;
    }
    
    public int getPrice()
    {
        return this.property_price;
    }
    
    public void setPrice(int property_price)
    {
        this.property_price = property_price;
    }
    
    public int getMortgage()
    {
        return this.property_mortgage;
    }
    
    public void setMortgage(int property_mortgage)
    {
        this.property_mortgage = property_mortgage;
    }
}


