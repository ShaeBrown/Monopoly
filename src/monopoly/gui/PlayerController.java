/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import monopoly.BuyableGrid;
import monopoly.Game;

import monopoly.Player;
/**
 *
 * @author shaebrown
 */
public final class PlayerController implements ListSelectionListener{
    
    private HashMap<Player, JButton> player_buttons;
    private HashMap<String, Component> menu_components;
    private List<Player> player_list;
    private JPanel player_menu;
    
    public PlayerController (List<Player> players, GridController gc) {
        this.player_list = players;
        player_buttons = new HashMap<>();
        menu_components = new HashMap<>();
    }
    
    
    
    public void updateMenu(Player p) {
       JLabel name = (JLabel) menu_components.get("Name");
       name.setText(p.getName());
       name.setIcon(player_buttons.get(p).getIcon());
       
       JLabel money = (JLabel) menu_components.get("Money");
       money.setText("<html>$" + p.getMoney() + "<br>Get out of Jail Free cards: " + p.getNumberOfJailFreeCards() + "</html>");
       
       JList properties = (JList) menu_components.get("Property");
       properties.clearSelection();
       DefaultListModel list = (DefaultListModel) properties.getModel();
       list.removeAllElements();
       for (BuyableGrid g: p.getProperties()) {
           list.addElement(g);
       }
       
       clearPropertyInfo();
    }
    
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
        
        menu_components.put("Money", Money);
        menu_components.put("Name", PlayerName);
        menu_components.put("Property", properties);
        menu_components.put("PropertyInfo", property_info);

        
        
    }
    
    public void setProperty(ImageIcon i, String name) {
        JLabel property_info = (JLabel) menu_components.get("PropertyInfo");
        property_info.setIcon(i);
        property_info.setText(name);
    }
    
    public void buyProperty(Player p) {
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

    public void updatePosition(Player p) {
        JButton button = player_buttons.get(p);
        int location = p.getLocation();
        LinkedList<Player> occupants = Game.grid_controller.getOccupants(location);
        if (occupants.size() == 1) {
            Point point = Game.grid_controller.setPlayerPosition(location);
            button.setLocation(point);
        }
        else {
            updatePositions(occupants);
        }
    }
    
    private void updatePositions(LinkedList<Player> players) {
        JButton[] buttons = new JButton[players.size()];
        for (int i = 0 ; i < buttons.length; i ++) {
            buttons[i] = player_buttons.get(players.get(i));
        }
        Game.grid_controller.setPlayersPositions(buttons, players.get(0).getLocation());
    }
    
    public void initPlayers(JPanel object_layer) {
        /* netbeans told me to change my for loop to this? */
        player_list.stream().forEach((p) -> {
            JButton button = new JButton();
            button.setBounds(920,920,75,75);
            button.setIcon(getPlayerToken(p));
            button.setPreferredSize(new java.awt.Dimension(75,75));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false); 
            button.setOpaque(false);
            object_layer.add(button, 1);
            player_buttons.put(p, button);
        });
        updatePositions(new LinkedList(player_list));
    }
    
    public ImageIcon getPlayerToken(Player p) {
        return new ImageIcon(getClass().getResource("/monopoly/gui/img/tokens/"+ p.getToken() +".png"));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        JList list = (JList) e.getSource();
        if (list.getSelectedValue() == null) {
            clearPropertyInfo();
            return;
        }
        Game.grid_controller.changeProperty((BuyableGrid)list.getSelectedValue());
    }
    
    public void clearPropertyInfo() {
       JLabel property_info = (JLabel) menu_components.get("PropertyInfo");
       property_info.setText("");
       property_info.setIcon(null);
    }
}
