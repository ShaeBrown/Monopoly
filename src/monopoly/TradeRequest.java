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
    
    public TradeRequest(BuyableGrid request, List<BuyableGrid> offer, int money_offer)
    {
        this.requester = offer.get(0).getOwner();
        this.requestee = request.getOwner();
        this.money_offer = money_offer;
    }
    
    public void send()
    {
        requestee.requests.add(this);
    }
    
}
