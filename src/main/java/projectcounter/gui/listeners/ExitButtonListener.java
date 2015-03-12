
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import projectcounter.gui.TabbedGui;

public class ExitButtonListener implements ActionListener {
    private JFrame frame;
    private TabbedGui gui;
    
    public ExitButtonListener(JFrame frame, TabbedGui gui) {
        this.frame = frame;
        this.gui = gui;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int quit = JOptionPane.showConfirmDialog(this.frame, "Sure to quit?", 
                "Exit", JOptionPane.OK_CANCEL_OPTION);
        
        if ( quit == 0 ) {
            try {
                if ( this.gui.saveProjectCounters() ) {
                    System.exit(0);
                }
            } catch ( Exception ex) {
                JOptionPane.showMessageDialog(this.frame, "Unable to save counters: \n" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
}
