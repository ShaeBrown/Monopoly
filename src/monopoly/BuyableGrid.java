/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;
/**
 *
 * @author Jacob
 */
public abstract class BuyableGrid extends Grid
{
    public enum PropertyGroup {RED, YELLOW, GREEN, BLUE, BROWN, LIGHTBLUE, MAGENTA, ORANGE, UTILITY, RAILROAD}
    
    protected Player owner;                   //Which player owns this?
    protected PropertyGroup property_group;   //Which property group does this belongs to?
    protected int property_price;             //Cost to buy the initial property  
    protected int property_mortgage;          //Mortgage value of the property
    /*Constructor*/
    public BuyableGrid(PropertyGroup property_group, String name, int property_price, int property_mortgage)
    {
        this.owner = null;
        this.name = name;
        this.property_group = property_group;
        this.property_price = property_price;
        this.property_mortgage = property_mortgage;
    }
    
    public abstract int getRentPrice();
    /*Add more class methods below?*/
    
    public Player getOwner()
    {
        return this.owner;
    }
    
    public boolean isBuyable() {
        return this.owner == null;
    }
    
    @Override
    public String toString() 
    {
        return this.name;
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
    
        /*Do this when a player lands on this grid*/
    @Override
    public void landingFunction(Player landed)
    {
        if (isBuyable()) {
            // If player has enough money?
            Game.player_controller.buyProperty(landed);
        }
        else {
            
            int rent = getRentPrice();
            if (landed.getMoney() >= rent)
            {
                landed.setMoney(landed.getMoney() -rent);
                getOwner().setMoney(getOwner().getMoney() + rent);
            }
            else
            {
                //morgage property??
            }
        }
    }
}
