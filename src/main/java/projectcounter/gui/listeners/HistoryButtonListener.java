
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import projectcounter.domain.ProjectCounter;
import projectcounter.gui.TabbedGui;

public class HistoryButtonListener implements ActionListener {
    private TabbedGui gui;
    private ProjectCounter counter;
    
    public HistoryButtonListener(TabbedGui gui, ProjectCounter counter) {
        this.gui = gui;
        this.counter = counter;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.gui.showHistory(this.counter);
    }
    
}
