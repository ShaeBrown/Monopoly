package monopoly;

/**
 * Luxury and Income Tax grids
 * 
 */
public class TaxGrid extends Grid
{
    
    String name;        //Name of tax
    int tax_amount;     //How much to tax
    
    /*Constructor*/

    /**
     * Create a new Tax grid
     * @param name the name of the grid
     * @param tax_amount the amount of tax it deducts
     */

    public TaxGrid(String name, int tax_amount)
    {
        this.name = name;
        this.tax_amount = tax_amount;
    }
    

    /**
     * Make the player pay the tax
     * @param landed the player who landed on the grid
     */

    public void landingFunction(AbstractPlayer landed)
    {
        landed.removeMoney(tax_amount);
        GenericGrid freeparking = (GenericGrid)Game.board_grids[Game.GRIDNUM.FreeParking.getNum()];
        freeparking.addToJackPot(tax_amount);
        Game.player_controller.displayMessage("You must pay $" + tax_amount + " of taxes.");
    }
}
