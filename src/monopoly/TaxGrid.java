package monopoly;

public class TaxGrid extends Grid
{
    
    String name;        //Name of tax
    int tax_amount;     //How much to tax
    
    /*Constructor*/
    public TaxGrid(String name, int tax_amount)
    {
        this.name = name;
        this.tax_amount = tax_amount;
    }
    
    /*Do this when a player lands on this grid*/
    public void landingFunction(Player landed)
    {
        
    }
}
