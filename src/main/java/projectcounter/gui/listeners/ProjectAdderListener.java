
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import projectcounter.gui.TabbedGui;

public class ProjectAdderListener implements ActionListener {
    private TabbedGui cardGui;
    private JTextField textField;
    
    public ProjectAdderListener(TabbedGui cardGui, JTextField textField) {
        this.cardGui = cardGui;
        this.textField = textField;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.cardGui.addNewProject(this.textField.getText());
        this.textField.setText("");
    }
    
}
