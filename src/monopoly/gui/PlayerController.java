/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import monopoly.Player;
/**
 *
 * @author shaebrown
 */
public class PlayerController {
    
    HashMap<Player, JButton> player_buttons;
    List<Player> player_list;
    GridController grid_controller;
    
    public PlayerController (List<Player> players, GridController gc) {
        this.player_list = players;
        this.grid_controller = gc;
        player_buttons = new HashMap<>();
    }
    
    public void updatePosition(Player p) {
        JButton players_button = player_buttons.get(p);
        int location = p.getLocation();
        Point point = grid_controller.getPlayerPosition(location);
        players_button.setLocation(point);
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
    }
    public ImageIcon getPlayerToken(Player p) {
        return new ImageIcon(getClass().getResource("/monopoly/gui/img/tokens/"+ p.getToken() +".png"));
    }
    
}
