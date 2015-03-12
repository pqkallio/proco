
package projectcounter.gui.listeners;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectionListener implements ListSelectionListener {
    private JButton openButton;
    private JButton delete;
    private JList list;
    
    public SelectionListener(JButton openButton, JButton delete, JList list) {
        this.openButton = openButton;
        this.delete = delete;
        this.list = list;
    }

    @Override
    public void valueChanged(ListSelectionEvent ae) {
        if ( ae.getValueIsAdjusting() == false ) {
            
            if ( !this.list.getSelectedValuesList().isEmpty() ) {
                this.openButton.setEnabled(true);
                this.delete.setEnabled(true);
            } else {
                this.openButton.setEnabled(false);
                this.delete.setEnabled(false);
            }
        }
    }
    
    public List<Object> getValues() {
        return this.list.getSelectedValuesList();
    }
    
}
