/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import monopoly.Grid;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import monopoly.BuyableGrid;
import monopoly.Game;
import monopoly.Player;

public final class GridController implements ActionListener {
    
    final int PANELSIZE = 1020;
    Grid[] grid_object;
    HashMap<JButton,Grid> grid_buttons;
    HashMap<BuyableGrid, ImageIcon> property_cards;
    JButton[] grid_button;
    JPanel board, objects;
    
    public GridController(Grid[] g) {
        this.grid_object = g;
        this.grid_buttons = new HashMap();
        this.property_cards = new HashMap();
        setPropertyCards();
    }
    
    public Grid getGrid(int i) {
        return grid_object[i];
    }
    
    public void addLayer(JPanel objects) {
        this.objects = objects;
    }
    
    public void addButtons(JButton[] buttons) {
        this.grid_button = buttons;
        for (int i = 0 ; i < buttons.length; i++) {
            grid_buttons.put(buttons[i],grid_object[i]);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton pressed = (JButton) event.getSource();
        BuyableGrid grid = (BuyableGrid)grid_buttons.get(pressed);
        changeProperty(grid);
    }
    
    public void changeProperty(BuyableGrid grid) {
        ImageIcon i = getPropertyCard(grid);
        String owner;
        if (grid.isBuyable()) {
            owner = "Unowned";
        }
        else {
            owner = "Owner: " + grid.getOwner().getName();
        }
        Game.player_controller.setProperty(i,owner);
    }
    
    public ImageIcon getPropertyCard(BuyableGrid g) {
        return property_cards.get(g);
    }
    
    public void setPropertyCards(){
        for (int i = 0; i < grid_object.length; i ++) {
            if (grid_object[i] instanceof BuyableGrid) {
                ImageIcon image = new ImageIcon(getClass().getResource("/monopoly/gui/img/propertycards/"+ grid_object[i].getName() +".jpg"));
                property_cards.put((BuyableGrid)grid_object[i],image);
            }
        }
    }
    
    public LinkedList<Player> getOccupants(int i) {
        return grid_object[i].getOccupants();
    }
    
    public Point setPlayerPosition(int i) {
        JButton grid = grid_button[i];
        Point point = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),objects);
        return point;
    }
    
    public void setPlayersPositions(JButton[] buttons, int i) {
        JButton grid = grid_button[i];
        LinkedList<Player> occupants = getOccupants(i);
        int height = grid.getHeight();
        int width = grid.getWidth();
        Point grid_corner = SwingUtilities.convertPoint(grid.getParent(),grid.getLocation(),objects);
        Point offset = new Point(grid.getX() + width/(occupants.size()+1), grid.getY() + height/(occupants.size())+1);
        offset = SwingUtilities.convertPoint(grid.getParent(), offset, objects);
        offset.setLocation(Math.abs(grid_corner.x - offset.x), Math.abs(grid_corner.y - offset.y));
        Point right_corner = SwingUtilities.convertPoint(grid.getParent(), grid.getX() + width, grid.getY() + height, objects);
        int x = grid_corner.x;
        int y = grid_corner.y;
        for (JButton b : buttons) {
            b.setLocation(grid_corner);
            if (x + offset.x < right_corner.x - b.getWidth()/1.5) {
                x += offset.x;
            }
            if (y + offset.y < right_corner.y - b.getHeight()/1.5) {
                y += offset.y;
            }
            grid_corner.setLocation(x,y);
        }
    }
    
}
