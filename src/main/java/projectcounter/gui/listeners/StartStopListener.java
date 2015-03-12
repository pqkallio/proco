
package projectcounter.gui.listeners;

import java.awt.Color;
import projectcounter.domain.ProjectCounter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import projectcounter.domain.Date;
import projectcounter.gui.TabbedGui;

public class StartStopListener implements ActionListener {
    private ProjectCounter projectCounter;
    private TabbedGui gui;
    private JButton startStopButton;
    private boolean isOn;
    
    public StartStopListener(TabbedGui gui, JButton startStopButton, ProjectCounter projectCounter) {
        this.gui = gui;
        this.startStopButton = startStopButton;
        this.projectCounter = projectCounter;
        this.isOn = false;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if ( !this.isOn ) {
            this.startStopButton.setText(TabbedGui.getSTOP());
            this.startStopButton.setBackground(new Color(255, 100, 100));
            this.isOn = true;
            this.projectCounter.start();
            Date hodie = new Date(Calendar.getInstance().get(Calendar.DATE), 
                    Calendar.getInstance().get(Calendar.MONTH) + 1, 
                    Calendar.getInstance().get(Calendar.YEAR));
            
            if ( !this.projectCounter.getDates().contains(hodie) ) {
                this.projectCounter.getDates().add(hodie);
            }
            
        } else {
            this.startStopButton.setText(TabbedGui.getSTART());
            this.startStopButton.setBackground(new Color(50, 255, 100));
            this.isOn = false;
            this.projectCounter.stop();
        }
    }
    
}
