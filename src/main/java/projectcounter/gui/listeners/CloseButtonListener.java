
package projectcounter.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class CloseButtonListener implements ActionListener {
    private JFrame frame;
    
    public CloseButtonListener(JFrame openFileFrame) {
        this.frame = openFileFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.dispose();
    }
    
}
