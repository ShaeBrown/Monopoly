/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.gui;
import java.awt.Point;
import java.util.HashMap;
import monopoly.Grid;
import javax.swing.JButton;

public class GridController {
    
    final int PANELSIZE = 1020;
    Grid[] grid_object;
    HashMap<Grid,JButton> grid_buttons;
    JButton[] grid_button;
    
    public GridController(Grid[] g) {
        this.grid_object = g;
        this.grid_buttons = new HashMap();
    }
    
    public void addButtons(JButton[] buttons) {
        this.grid_button = buttons;
        for (int i = 0 ; i < buttons.length; i++) {
            grid_buttons.put(grid_object[i], buttons[i]);
        }
    }
    
    public Point getPlayerPosition(int i) {
        /* Because players will overlap, we somehow have to keep track of the occupants
        on a square. If there is more than one occupant we must rearrange the token locations accordingly
        */
        JButton button = grid_button[i];
        Point button_location = button.getLocationOnScreen();
        return new Point(button_location);
    }
    
}
