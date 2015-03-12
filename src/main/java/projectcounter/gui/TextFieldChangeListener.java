
package projectcounter.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JTextField;

class TextFieldChangeListener implements KeyListener {
    private JButton createButton;
    
    public TextFieldChangeListener(JButton createButton) {
        this.createButton = createButton;
    }

    @Override
    public void keyTyped(KeyEvent ae) {}

    @Override
    public void keyPressed(KeyEvent ae) {}

    @Override
    public void keyReleased(KeyEvent ae) {
        JTextField field = (JTextField)ae.getSource();
        
        if ( field.getText().length() < 1 ) {
            if ( this.createButton.isEnabled() ) {
                this.createButton.setEnabled(false);
            }
        } else {
            if ( !this.createButton.isEnabled() ) {
                this.createButton.setEnabled(true);
            }
        }
    }
    
    
    
}
