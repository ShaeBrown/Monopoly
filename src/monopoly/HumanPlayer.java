package monopoly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class HumanPlayer extends AbstractPlayer{
        /*New Player constructor*/
    public HumanPlayer ()
    {
        super();
    }
    
    public AbstractPlayer clone()
    {
        HumanPlayer p = new HumanPlayer();
        p.setName(this.name);
        p.setToken(this.token);
        p.setMoney(this.money); 
        p.setNumberOfJailFreeCards(this.jail_free_cards);
        p.setLocation(this.location);
        p.setJailStatus(this.isInJail());
        p.setProperties((LinkedList<BuyableGrid>)this.property.clone());
        num_of_owned_type = new HashMap<>();
        for (BuyableGrid.PropertyGroup group: BuyableGrid.PropertyGroup.values()) {
            num_of_owned_type.put(group, 0);
        }
        return p;
    }
    
}
