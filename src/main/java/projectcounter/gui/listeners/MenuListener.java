
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import projectcounter.gui.TabbedGui;

public class MenuListener implements ActionListener {
    private TabbedGui gui;
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private TabChangeListener tabListener;
    private JMenuItem open;
    private JMenuItem close;
    private JMenuItem exit;
    private JCheckBoxMenuItem alwaysOnTop;
    
    public MenuListener(TabbedGui gui, JTabbedPane tabbedPane, 
            TabChangeListener tabListener, JMenuItem open, JMenuItem close, 
            JMenuItem exit, JCheckBoxMenuItem setAlwaysOnTop) {
        this.gui = gui;
        this.frame = this.gui.getFrame();
        this.tabbedPane = tabbedPane;
        this.tabListener = tabListener;
        this.open = open;
        this.close = close;
        this.exit = exit;
        this.alwaysOnTop = setAlwaysOnTop;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if ( ae.getSource() == open ) {
            this.gui.openClosedProjectCounters();
        } else if ( ae.getSource() == close ) {
            closeTab();
        } else if ( ae.getSource() == exit ) {
            exitProgram();
        } else if ( ae.getSource() == alwaysOnTop ) {
            setAlwaysOnTop();
        }
    }

    private void closeTab() {
        int index = this.tabListener.getTabSelected();
        
        for (String counterName : this.gui.getProjectCounters().keySet()) {
            if ( counterName.equals(this.tabbedPane.getTitleAt(index)) ) {
                this.gui.getProjectCounters().get(counterName).setActive(false);
            }
        }
        
        this.tabbedPane.remove(index);
    }

    private void exitProgram() {
        int quit = JOptionPane.showConfirmDialog(this.frame, "Sure to quit?", 
                "Exit", JOptionPane.OK_CANCEL_OPTION);
        
        if ( quit == 0 ) {
            try {
                if ( this.gui.saveProjectCounters() ) {
                    System.exit(0);
                }
            } catch ( Exception ex) {
                Logger.getLogger(MenuListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void setAlwaysOnTop() {
        if ( this.alwaysOnTop.getState() ) {
            this.frame.setAlwaysOnTop(true);
        } else {
            this.frame.setAlwaysOnTop(false);
        }
    }
    
}
