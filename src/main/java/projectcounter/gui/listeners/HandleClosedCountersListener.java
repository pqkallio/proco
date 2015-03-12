
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import projectcounter.domain.ProjectCounter;
import projectcounter.gui.TabbedGui;

public class HandleClosedCountersListener implements ActionListener {
    private TabbedGui gui;
    private JFrame openFileFrame;
    private JList list;
    private JButton openCounters;
    private JButton deleteCounters;
    
    public HandleClosedCountersListener(TabbedGui gui, JFrame openFileFrame, JList list, JButton openCounters, JButton deleteCounters) {
        this.gui = gui;
        this.openFileFrame = openFileFrame;
        this.list = list;
        this.openCounters = openCounters;
        this.deleteCounters = deleteCounters;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        List<ProjectCounter> counters = new ArrayList();
        DefaultListModel listModel = (DefaultListModel)this.list.getModel();

        for (Object object : this.list.getSelectedValuesList()) {
            String counterName = (String)object;

            ProjectCounter counter = this.gui.getProjectCounters().get(counterName);

            counters.add(counter);
        }
        
        if ( ae.getSource() == this.openCounters ) {
            for (ProjectCounter projectCounter : counters) {
                projectCounter.setActive(true);
                listModel.remove(listModel.indexOf(projectCounter.getName()));
                this.gui.addExistingProject(projectCounter);
            }
        } else if ( ae.getSource() == this.deleteCounters ) {
            if ( JOptionPane.showConfirmDialog(this.openFileFrame, 
                    "Are you sure you want to delete " + counters.size() 
                    + " counters?\n(This action cannot be undone)", 
                    "Delete counters", JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.QUESTION_MESSAGE) == 0 ) {
                this.gui.getProjectCounters();
                
                for (ProjectCounter projectCounter : counters) {
                    listModel.remove(listModel.indexOf(projectCounter.getName()));
                    this.gui.getProjectCounters().remove(projectCounter.getName());
                }
            }
        }
        
        this.list.setModel(listModel);
        this.openFileFrame.revalidate();
        this.gui.getFrame().revalidate();
    }
    
}
