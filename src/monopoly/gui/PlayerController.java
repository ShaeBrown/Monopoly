/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import monopoly.BuyableGrid;
import monopoly.CardGrid.CardType;
import monopoly.Game;
import monopoly.AbstractPlayer;
import monopoly.PropertyGrid;

/** 
 *  PlayerController
 * 
 *      - Initializes and updates player menu
 *      - Sets the property card in the menu and acts as a listener for the list of properties on the menu.
 *      - Opens a dialogue to buy properties
 *      - Controls button to buy houses/hotels
 * 
 */
public final class PlayerController implements ListSelectionListener {
    
    private final HashMap<AbstractPlayer, JButton> player_buttons;
    private final HashMap<String, Component> menu_components;
    private final List<AbstractPlayer> player_list;
    private JPanel player_menu;
    ImageIcon moneybags; // image for the chance/community cards.
    ImageIcon arrow;
    
    // The current PropertyGrid that is being displayed (if no property displayed then contains the last property displayed)
    private PropertyGrid selectedProperty;		
    
    public PlayerController (List<AbstractPlayer> players, GridController gc) {
        this.player_list = players;
        player_buttons = new HashMap<>();
        menu_components = new HashMap<>();
        this.moneybags = new ImageIcon(getClass().getResource("/monopoly/gui/img/moneybags.png"));
        this.arrow = new ImageIcon(getClass().getResource("/monopoly/gui/img/arrow.png"));
    }
    
    public void updateMenu(AbstractPlayer p) {
       JLabel name = (JLabel) menu_components.get("Name");
       
       name.setText(p.getName());
       name.setIcon(player_buttons.get(p).getIcon());
       
       updateStats(p);
       
       JList properties = (JList) menu_components.get("Property");
       properties.clearSelection();
       
       DefaultListModel list = (DefaultListModel) properties.getModel();
       
    /* Was getting an exception, I was thinking it was from remove all elements from 
       the list when the list was being accessed.
       Putting it in it's own thread which will run when all others events are finished
       such as accessing a property in the list, will provent that, Hopefully???? */
       
    SwingUtilities.invokeLater(new Runnable()
    {
        @Override
        public void run()
        {
            list.removeAllElements();
            for (BuyableGrid g : p.getProperties()) 
            {
                list.addElement(g);
            }
        }
    });
       
       clearPropertyInfo();
       clearBuyButton();
       clearHouseIcons();
    }
    
    public void updateStats(AbstractPlayer p) {
       JLabel money = (JLabel) menu_components.get("Money");
       money.setText("<html>$" + p.getMoney() + "<br>Get out of Jail Free cards: " + p.getNumberOfJailFreeCards() + "</html>");
    }
    
    /* Initializes all the menu compenents, and adds them to a hashtable, where
    their key's are strings such as
    "Name", "Money", "Property", "PropertyInfo"
    */
    public void initMenu(JPanel player_menu) {
        this.player_menu = player_menu;
        
        JLabel PlayerName = new JLabel();
        PlayerName.setHorizontalAlignment(SwingConstants.LEFT);
        PlayerName.setFont(new java.awt.Font("Ubuntu", 1, 30)); // NOI18N
        PlayerName.setText("PlayerName");

        player_menu.add(PlayerName);
        
        JLabel Money = new JLabel();
        Money.setFont(new java.awt.Font("Ubuntu", 1, 16));
        Money.setText("Players Money");
        Money.setSize(200, Money.getHeight());
        
        player_menu.add(Money);

        DefaultListModel property_list = new DefaultListModel();
        JList properties = new JList(property_list);
        properties.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(properties);
        scrollPane.setPreferredSize(new Dimension(250, 200));
        
        player_menu.add(scrollPane);
        
        JLabel property_info = new JLabel();
        property_info.setHorizontalTextPosition(JLabel.CENTER);
        property_info.setVerticalTextPosition(JLabel.TOP);
        
        player_menu.add(property_info);
      
        JPanel house_icons = new JPanel(new FlowLayout());
        house_icons.setOpaque(false);
        house_icons.setVisible(false);
        
        player_menu.add(house_icons);
        
        JButton buyHouseButton = new JButton();
        buyHouseButton.setBounds(920,920,100,40);
        buyHouseButton.setPreferredSize(new java.awt.Dimension(100,40));
        buyHouseButton.setContentAreaFilled(false);
        buyHouseButton.setFocusPainted(false); 
        buyHouseButton.setOpaque(false);  
        buyHouseButton.setFont(new java.awt.Font("Ubuntu", 1, 15));
        buyHouseButton.setMargin(new Insets(3, 3, 3, 3));
        buyHouseButton.setText("Buy House");
        buyHouseButton.addActionListener(new ActionListener() {
                @Override
        	public void actionPerformed(ActionEvent e) {
        		buyHouse();
	        }
	      });      
        
        player_menu.add(buyHouseButton);
        
        
        
        menu_components.put("Money", Money);
        menu_components.put("Name", PlayerName);
        menu_components.put("Property", properties);
        menu_components.put("PropertyInfo", property_info);
        menu_components.put("BuyHouse", buyHouseButton);    
        menu_components.put("HouseIcons", house_icons);
    }
    
    // Displays the buy house button
    public void setBuyHouse(PropertyGrid g) {
      JButton button = (JButton) menu_components.get("BuyHouse");
      button.setVisible(true);
      selectedProperty = g;
    }
    
//TODO(nick): Implement logic that shows buy hotel when the property has 4 hotels
    /* Creates a dialogue asking if the player wants to buy a house */
    public void buyHouse() {
        
    	PropertyGrid g = selectedProperty;
    	AbstractPlayer p = g.getOwner();    	
    	
      ImageIcon icon = Game.grid_controller.getPropertyCard(g);
      String msg = "Would you like to buy a house for " + g.getName() + " for $" + g.getHousePrice() + "?\n";
      msg += "You currently have " + g.getCurrentHouses();
      if (g.getCurrentHouses() == 1) 
      	msg += " house.";
      else 
      	msg += " houses.";
      
      int buy = JOptionPane.showConfirmDialog(player_menu,  msg, "Buy House",  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
      
    	// tmp - can remove once I've implemented logic to only display button when necessary
    	if (g.getOwner() == null)
    		return;
      
      if (buy == JOptionPane.YES_OPTION) {
        if (p.getMoney() < g.getHousePrice()) {
        	// Not enough money to buy house
        } else {
        	if (g.getCurrentHouses() < g.MAX_NUMBER_HOUSES) {
        		p.buyHouse(g);
                        Game.grid_controller.addHouseIcon(g);
                        Game.grid_controller.changeProperty(g);
        	}
        }
      }
    }
    
    /* Passed a property image icon and a string,
       Displays it on the menu
    */
    public void setProperty(ImageIcon i, String name) {
        JLabel property_info = (JLabel) menu_components.get("PropertyInfo");
        property_info.setIcon(i);
        property_info.setText(name);
    }
    
    /* The number of houses to display under the property card */
    public void setHouseIcons(int houses) {
        JPanel panel = (JPanel) menu_components.get("HouseIcons");
        panel.setVisible(true);
        for (int i = 0; i < houses; i++) {
            JButton house = new JButton();
            ImageIcon house_icon = Game.grid_controller.house_icon;
            house.setIcon(house_icon);
            house.setBorderPainted(false);
            house.setContentAreaFilled(false);
            house.setFocusPainted(false); 
            house.setOpaque(false);
            house.setVisible(true);
            panel.add(house);
        }
        panel.repaint();
        panel.revalidate();
    }
    
    /* Creates a dialogue asking if the player wants to buy the Buyablegrid they
    just landed on
    */
    public void buyProperty(AbstractPlayer p) {
        assert(Game.grid_controller.getGrid(p.getLocation()) instanceof BuyableGrid);
        BuyableGrid g = (BuyableGrid) Game.grid_controller.getGrid(p.getLocation());
        ImageIcon icon = Game.grid_controller.getPropertyCard(g);
        int buy = JOptionPane.showConfirmDialog(player_menu,  "Would you like to buy " + g.getName() + " for $" + g.getPrice() + "?", "Buy Property",  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        if (buy == JOptionPane.YES_OPTION) {
          if (p.getMoney() < g.getPrice()) {
              
          }
          else {
            p.buyProperty(g);
          }
        }
        else {
          //auction or nothing?
        }
    }
    
    /* Displays the drawn card, the type is used for the dialog title, and the string is the message */
    public void displayCard(CardType type, String card) {
        if (type == CardType.CHANCECARD) 
            JOptionPane.showMessageDialog(player_menu, card, "CHANCE", JOptionPane.PLAIN_MESSAGE, moneybags);
        else
            JOptionPane.showMessageDialog(player_menu, card, "COMMUNITY CHEST", JOptionPane.PLAIN_MESSAGE, moneybags);
    }
    
    public void displayRent(AbstractPlayer owner, AbstractPlayer landed, BuyableGrid grid) {
        JPanel rentImages = new JPanel();
        rentImages.add(new JLabel(getPlayerToken(landed)));
        rentImages.add(new JLabel(arrow));
        rentImages.add(new JLabel(getPlayerToken(owner)));
        String msg = "You must pay " + owner.getName() + " $" + grid.getRentPrice() + " for landing on " + grid.getName();
        rentImages.add(new JLabel(msg));
        JOptionPane.showMessageDialog(null, rentImages, "PAY RENT", JOptionPane.PLAIN_MESSAGE);
    }

    /* Sets player on the GUI to their location */
    public void updatePosition(AbstractPlayer p) {
        JButton button = player_buttons.get(p);
        int location = p.getLocation();
        LinkedList<AbstractPlayer> occupants = Game.grid_controller.getOccupants(location);
        if (occupants.size() == 1) {
            Point point = Game.grid_controller.setPlayerPosition(location);
            button.setLocation(point);
        }
        else {
            updatePositions(occupants);
        }
    }
    
    
    /* Is called when the players new position is shared by other players
       will use the grid controller to properly space players out
    */
    private void updatePositions(LinkedList<AbstractPlayer> players) {
        JButton[] buttons = new JButton[players.size()];
        assert(players.size() == buttons.length);
        for (int i = 0 ; i < buttons.length; i ++) {
            buttons[i] = player_buttons.get(players.get(i));
        }
        Game.grid_controller.setPlayersPositions(buttons, players.get(0).getLocation());
    }
    
    /*
    Initalizes all players on Go when the game starts
    */
    public void initPlayers(JPanel object_layer) {
        for (AbstractPlayer p : player_list) {
            JButton button = new JButton();
            button.setBounds(920,920,75,75);
            button.setIcon(getPlayerToken(p));
            button.setPreferredSize(new java.awt.Dimension(75,75));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false); 
            button.setOpaque(false);
            object_layer.add(button);
            player_buttons.put(p, button);
        }
        updatePositions(new LinkedList(player_list));
    }
    
    /*
        Returns the appropriate ImageIcon for the players token
    */
    public ImageIcon getPlayerToken(AbstractPlayer p) {
        return new ImageIcon(getClass().getResource("/monopoly/gui/img/tokens/"+ p.getToken() +".png"));
    }

    /* 
    Handles when the selected owned property on the players menu is clicked/ changed
    */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        if (list.getSelectedIndex() == -1) { //nothing is selected, clear menu
            clearPropertyInfo();
            clearBuyButton();
            clearHouseIcons();
        }
        else {
            Game.grid_controller.changeProperty((BuyableGrid)list.getSelectedValue());
        }
    }
    
    /*Clears the property info on the menu */
    public void clearPropertyInfo() {
       JLabel property_info = (JLabel) menu_components.get("PropertyInfo");
       property_info.setText("");
       property_info.setIcon(null);
    }

    /* Sets the buy houses/hotels button to not be visible */
    public void clearBuyButton() {
    	JButton button = (JButton) menu_components.get("BuyHouse");
      button.setVisible(false);	
    }
    
    public void clearHouseIcons() {
        JPanel houses = (JPanel) menu_components.get("HouseIcons");
        houses.removeAll();
    }
}
