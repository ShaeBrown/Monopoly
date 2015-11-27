/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

import java.util.List;

/**
 * Sends a request to trade a property to another player
 * @author shaebrown
 */
public class TradeRequest {
    
    AbstractPlayer requester;
    AbstractPlayer requestee;
    BuyableGrid request;
    List<BuyableGrid> offer;
    int money_offer;
    
    public TradeRequest(AbstractPlayer requester, BuyableGrid request, List<BuyableGrid> offer, int money_offer)
    {
        this.request = request;
        this.offer = offer;
        this.requester = requester;
        this.requestee = request.getOwner();
        this.money_offer = money_offer;
    }
    
    public void send()
    {
        if (requester.getMoney() < money_offer)
        {
            Game.dialog_controller.displayMessage("You cannot offer $" + money_offer + " you only have $" + requester.getMoney());
            return;
        }
        requestee.requests.add(this);
    }
    
    public void accept()
    {
        requester.addProperty(request);
        requestee.removeProperty(request);
        if (offer != null) {
            for (BuyableGrid g : offer)
            {
                requestee.addProperty(g);
                requester.removeProperty(g);
            }
        }
        requestee.addMoney(money_offer);
        requester.removeMoney(money_offer);
        requestee.requests.remove(this);
    }
    
    public void decline()
    {
        requestee.requests.remove(this);
    }
    
    public AbstractPlayer getRequester()
    {
        return requester;
    }
    
    public int getMoneyOffer()
    {
        return money_offer;
    }
    
    public List<BuyableGrid> getPropertyOffer()
    {
        return offer;
    }
    
    public BuyableGrid getRequestedProperty()
    {
        return request;
    }
    
}
