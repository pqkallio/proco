
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;
import projectcounter.domain.ProjectCounter;
import projectcounter.gui.TabbedGui;

public class InactivationListener implements ActionListener {
    private JTabbedPane tabbedPane;
    private TabChangeListener tabListener;
    private TabbedGui gui;
    private ProjectCounter counter;
    
    public InactivationListener(TabbedGui gui, JTabbedPane tabbedPane, TabChangeListener tabListener) {
        this.tabbedPane = tabbedPane;
        this.tabListener = tabListener;
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        int index = this.tabListener.getTabSelected();
        
        for (String counterName : this.gui.getProjectCounters().keySet()) {
            if ( counterName.equals(this.tabbedPane.getTitleAt(index)) ) {
                this.gui.getProjectCounters().get(counterName).setActive(false);
            }
        }
        
        this.tabbedPane.remove(index);
    }
    
}
