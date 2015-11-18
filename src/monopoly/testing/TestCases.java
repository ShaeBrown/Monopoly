package monopoly.testing;

import monopoly.*;
import monopoly.AbstractPlayer.*;
import monopoly.EventCard.*;
import monopoly.BuyableGrid.*;

import junit.framework.*;
import static org.junit.Assert.*;
import org.junit.Test;

/*WARNING: These test cases will most likely crash with our current version, 
    as many entities are no longer independent and requires initialization of 
    many other game features to work properly.
*/

public class TestCases 
{
    @Test
    public void testPlayer() 
    {
       	HumanPlayer p = new HumanPlayer();
		
	//Testing Tokens
	assertEquals(PlayerToken.DOG, p.getToken());
	p.setToken(PlayerToken.BATTLESHIP);
	assertEquals(PlayerToken.BATTLESHIP, p.getToken());
		
	//Testing playername
	assertEquals("", p.getName());
	p.setName("SampleGuy");
	assertEquals("SampleGuy", p.getName());
		
        //Testing money functions
	assertEquals(0, p.getMoney());
	p.setMoney(100);
	assertEquals(100, p.getMoney());
	p.addMoney(15);
	assertEquals(115, p.getMoney());
	p.removeMoney(20);
	assertEquals(95, p.getMoney());
	
	//Testing player location functions
	assertEquals(0, p.getLocation());
	p.setLocation(5);
	assertEquals(5, p.getLocation());
	p.advance(20);
	assertEquals(25, p.getLocation());
	p.advance(16);
	assertEquals(1, p.getLocation());
	assertEquals(295, p.getMoney());
	
        //Test Jail Free Cards
	assertEquals(0, p.getNumberOfJailFreeCards());
	p.addJailFreeCard();
        p.addJailFreeCard();
	assertEquals(2, p.getNumberOfJailFreeCards());
		
	//Test Jailing functions
	assertFalse(p.isInJail());
	p.setJailStatus(true);
	assertTrue(p.isInJail());		
    }

    
    @Test
    public void testEventCard() 
    {
	EventCard card;
	HumanPlayer p = new HumanPlayer();
	p.setMoney(100);
	p.setLocation(39);
	
	//Testing a GAINMONEY card on a player
	card = new EventCard("You gained $50!", "", ActionType.GAINMONEY, 50);
	card.useCard(null, p);
	assertEquals(150, p.getMoney());
	
	//Testing a MOVETO card on a player
	card = new EventCard("Advance to Grid 3", "",ActionType.MOVETO, 3);
	card.useCard(null, p);
	assertEquals(350, p.getMoney());
	assertEquals(3, p.getLocation());
	card = new EventCard("Advance to Grid 5", "",ActionType.MOVETO, 5);
	card.useCard(null, p);
	assertEquals(350, p.getMoney());
	assertEquals(5, p.getLocation());
	
	//Testing Go To Jail card
	card = new EventCard("Go to Jail", "", ActionType.GOTOJAIL);
	assertTrue(p.isInJail());
    }
    
    @Test
    public void testBuyablePropertyRents() 
    {
	PropertyGrid g = new PropertyGrid(PropertyGroup.BROWN, "House", 60, 30, (new int[]{2, 10, 30, 90, 160, 250}));
	
	//Testing Rent Pricing
	assertEquals(2, g.getRentPrice());
	g.addHouse();
	assertEquals(10, g.getRentPrice());
	g.addHouse();
	g.addHouse();
	g.addHouse();
	assertEquals(160, g.getRentPrice());
	g.addHotel();
	assertEquals(250, g.getRentPrice());
	
	//Testing Landing Function
	HumanPlayer p = new HumanPlayer();
	p.addMoney(1000);
	HumanPlayer q = new HumanPlayer();
	q.addMoney(1000);
		
	//Test deduction of rent upon landing
	g.setOwner(p);
	g.landingFunction(q);
	assertEquals(1250, p.getMoney());
	assertEquals(750, q.getMoney());
    }
    
    
    @Test
    public void testDice() 
    {
        Dice d = new Dice();
        
        //Roll the dice for large number of times, since rng is used
        for(int i=0; i<1000; i++)
        {
            d.newRoll();
            
            //Make sure both dices generates values within the correct range
            assertTrue(d.getRoll1() >= 1 && d.getRoll1() <= 6);
            assertTrue(d.getRoll2() >= 1 && d.getRoll2() <= 6);
            
            //Testing "doubles"
            assertTrue((d.isDoubles() && (d.getRoll1() == d.getRoll2())) ||
                        (!d.isDoubles() && (d.getRoll1() != d.getRoll2())));
            
            //Testing getRoll()
            assertEquals(d.getRoll(), d.getRoll1() + d.getRoll2());
        }

        
    }

}
