
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import projectcounter.gui.TabbedGui;

public class OpenButtonListener implements ActionListener {
    private TabbedGui gui;
    
    public OpenButtonListener(TabbedGui gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.gui.openClosedProjectCounters();
    }
    
}
