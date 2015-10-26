/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Component;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import monopoly.BuyableGrid;

import monopoly.Player;
/**
 *
 * @author shaebrown
 */
public class PlayerController {
    
    private HashMap<Player, JButton> player_buttons;
    private HashMap<String, Component> menu_components;
    private List<Player> player_list;
    private GridController grid_controller;
    private JPanel player_menu;
    
    public PlayerController (List<Player> players, GridController gc) {
        this.player_list = players;
        this.grid_controller = gc;
        player_buttons = new HashMap<>();
        menu_components = new HashMap<>();
    }
    
    
    public void updateMenu(Player p) {
       JLabel name = (JLabel) menu_components.get("Name");
       name.setText(p.getName());
       name.setIcon(player_buttons.get(p).getIcon());
       
       JLabel money = (JLabel) menu_components.get("Money");
       money.setText("$" + p.getMoney());
       
       JList properties = (JList) menu_components.get("Property");
       DefaultListModel list = (DefaultListModel) properties.getModel();
       list.removeAllElements();
       for (BuyableGrid g: p.getProperties()) {
           list.addElement(g);
       }
    }
    
    public void initMenu(JPanel player_menu) {
        this.player_menu = player_menu;
        
        JLabel PlayerName = new JLabel();
        PlayerName.setHorizontalAlignment(SwingConstants.CENTER);
        PlayerName.setFont(new java.awt.Font("Ubuntu", 1, 30)); // NOI18N
        PlayerName.setText("PlayerName");

        player_menu.add(PlayerName);
        
        JLabel Money = new JLabel();
        Money.setFont(new java.awt.Font("Ubuntu", 1, 16));
        Money.setText("Players Money");
        
        player_menu.add(Money);

        DefaultListModel property_list = new DefaultListModel();
        JList properties = new JList(property_list);
        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(properties);
        
        player_menu.add(scrollPane);
        
        
        JLabel property_info = new JLabel();
        
        menu_components.put("Money", Money);
        menu_components.put("Name", PlayerName);
        menu_components.put("Property", properties);

        
        
    }
    
    public void buyProperty(Player p) {
        BuyableGrid g = (BuyableGrid) grid_controller.getGrid(p.getLocation());

        int buy = JOptionPane.showConfirmDialog(player_menu, "Would you like to buy " + g.getName() + " for $" + g.getPrice() + "?", null,  JOptionPane.YES_NO_OPTION);
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
        LinkedList<Player> occupants = grid_controller.getOccupants(location);
        if (occupants.size() == 1) {
            Point point = grid_controller.setPlayerPosition(location);
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
        grid_controller.setPlayersPositions(buttons, players.get(0).getLocation());
    }
    
    public void initPlayers(JPanel object_layer) {
        for (Player p : player_list) {
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
        }
        updatePositions(new LinkedList(player_list));
    }
    public ImageIcon getPlayerToken(Player p) {
        return new ImageIcon(getClass().getResource("/monopoly/gui/img/tokens/"+ p.getToken() +".png"));
    }
    
}
