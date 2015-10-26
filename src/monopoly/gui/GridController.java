/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import monopoly.Grid;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import monopoly.Player;

public class GridController {
    
    final int PANELSIZE = 1020;
    Grid[] grid_object;
    HashMap<Grid,JButton> grid_buttons;
    JButton[] grid_button;
    JPanel board, objects;
    
    public GridController(Grid[] g) {
        this.grid_object = g;
        this.grid_buttons = new HashMap();
    }
    
    public void addLayer(JPanel objects) {
        this.objects = objects;
    }
    
    public void addButtons(JButton[] buttons) {
        this.grid_button = buttons;
        for (int i = 0 ; i < buttons.length; i++) {
            grid_buttons.put(grid_object[i], buttons[i]);
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
                System.out.println(grid_corner);
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
