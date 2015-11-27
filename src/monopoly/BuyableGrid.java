/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;
/**
 * A grid a player can purchase
 * @author Jacob
 */
public abstract class BuyableGrid extends Grid
{

    /**
     * The group the BuyableGrid belongs to
     * Either the grid is a RailRoad, Utility, or part of a colour group
     */
    public enum PropertyGroup {

        /**
         * Kentucky, Indiana, Illonios
         */
        RED(3),

        /**
         * Atlantic, Ventinor, Marvin
         */
        YELLOW(3),

        /**
         * Pennsylvania, North Carolina, Pacific
         */
        GREEN(3),

        /**
         * Park Place, Boardwalk
         */
        BLUE(2),

        /**
         * Baltic, Mediterranean
         */
        BROWN(2),

        /**
         * Connecticut, Vermont, Oriental
         */
        LIGHTBLUE(3),

        /**
         * St Charles, States, Virginia
         */
        MAGENTA(3),

        /**
         * St James, Tenessee, New York
         */
        ORANGE(3),

        /**
         * Electric, Water Works
         */
        UTILITY(2),

        /**
         * Reading, Pennsylvania, B and O, Short Line
         */
        RAILROAD(4);
        
        /**
         * The number of grid's in each group
         */
        final int MAX;
        PropertyGroup(int max) {
            this.MAX = max;
        }
    
    }
    
    /**
     * The owner of the property, null if unowned
     */
    protected AbstractPlayer owner;                   

    /**
     * the group the grid is in (BLUE, UTILITY, ETC..)
     */
    public PropertyGroup property_group; 
    
    /**
     * The cost to buy the grid
     */
    protected int property_price;            

    /**
     * The amount the grid could be mortgaged for
     */
    protected int property_mortgage;   
    
    /*Constructor*/

    /**
     * Create a BuyableGrid
     * @param property_group the group the grid is in
     * @param name the name of the grid
     * @param property_price the price to buy the grid
     * @param property_mortgage the amount the grid can be morgaged for
     */

    public BuyableGrid(PropertyGroup property_group, String name, int property_price, int property_mortgage)
    {
        this.owner = null;
        this.name = name;
        this.property_group = property_group;
        this.property_price = property_price;
        this.property_mortgage = property_mortgage;
    }
    
    /**
     * Get the grid's rent price
     * The price that must be payed when landing on it
     * @return the rent price
     */
    public abstract int getRentPrice();
    
    /**
     * Get the owner of the grid
     * @return the owner
     */
    public AbstractPlayer getOwner()
    {
        return this.owner;
    }
    
    /**
     * Whether the grid is buyable or not,
     * Does someone else already own it?
     * @return Whether the grid is buyable or not
     */
    public boolean isBuyable() {
        return this.owner == null;
    }
    
    @Override
    public String toString() 
    {
        return this.name;
    }
    
    /**
     * Sets the grid's owner
     * @param owner the new owner of the grid
     */
    public void setOwner(AbstractPlayer owner)
    {
        this.owner = owner;
    }
    
    /**
     *
     * @return the group the grid is in
     */
    public PropertyGroup getPropertyGroup()
    {
        return this.property_group;
    }
    
    /**
     * Set the grid's group
     * @param property_group the group to assign the grid to
     */
    public void setPropertyGroup(PropertyGroup property_group)
    {
        this.property_group = property_group;
    }
        
    /**
     * Get the price to buy the grid
     * @return the price of the grid
     */
    public int getPrice()
    {
        return this.property_price;
    }
    
    /**
     * Set the price to buy the grid
     * @param property_price the price of the grid
     */
    public void setPrice(int property_price)
    {
        this.property_price = property_price;
    }
    
    /**
     * Get the amount of money the grid can be mortgaged for
     * @return the mortgage price
     */
    public int getMortgage()
    {
        return this.property_mortgage;
    }
    
    /**
     * Set the amount the grid can be mortgaged for
     * @param property_mortgage the amount the grid can be mortgaged for
     */
    public void setMortgage(int property_mortgage)
    {
        this.property_mortgage = property_mortgage;
    }
    
        /*Do this when a player lands on this grid*/

    /**
     * If the grid is buyable, it calls the property decision for the corresponsing
     * child classes. Otherwise, a dialogue will be displayed describing the rent which must be payed
     * @param landed the player that landed on the grid
     */

    @Override
    public void landingFunction(AbstractPlayer landed)
    {
        if (isBuyable()) {
            // If player has enough money?
            landed.propertyDecision();
        }
        else if (!getOccupants().contains(getOwner())) { //if the owner is not currently on the property
            
            int rent = getRentPrice();
            if (landed.getMoney() >= rent)
            {
                landed.removeMoney(rent);
                getOwner().addMoney(rent);
                Game.dialog_controller.displayRent(getOwner(), landed, this);
            }
            else
            {
                //morgage property??
            }
        }
    }
}
