
package projectcounter.gui.listeners;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import projectcounter.gui.CounterPanel;
import projectcounter.gui.TabbedGui;

public class TabChangeListener implements ChangeListener {
    private int tabSelected;
    private TabbedGui gui;
    private JFrame frame;

    public TabChangeListener(TabbedGui gui, JFrame frame) {
        this.gui = gui;
        this.frame = frame;
    }
    
    @Override
    public void stateChanged(ChangeEvent ae) {
        JTabbedPane sourcePane = (JTabbedPane)ae.getSource();
        this.tabSelected = sourcePane.getSelectedIndex();
        
        for (int i = 0; i < sourcePane.getTabCount(); i++) {
            if ( i != this.tabSelected ) {
                if ( !sourcePane.getTitleAt(i).equals("+") ) {
                    if ( this.gui.getProjectCounters().get(sourcePane.getTitleAt(i)).isRunning() ) {
                        this.gui.getProjectCounters().get(sourcePane.getTitleAt(i)).stop();
                        
                        CounterPanel tabPanel = (CounterPanel)sourcePane.getComponentAt(i);
                        
                        Component[] tabPanelComponents = tabPanel.getComponents();
                        
                        for (int j = 0; j < tabPanelComponents.length; j++) {
                            JPanel aPanel = (JPanel)tabPanelComponents[j];
                            
                            Component[] aPanelComponents = aPanel.getComponents();
                            
                            for (int k = 0; k < aPanelComponents.length; k++) {
                                if (aPanelComponents[k].getClass() == JButton.class ) {
                                    JButton button = (JButton)aPanelComponents[k];

                                    if ( button.getText().equals(TabbedGui.getSTOP()) ) {
                                        button.setText(TabbedGui.getSTART());
                                        
                                        ActionListener[] buttonListeners = button.getActionListeners();
                                        
                                        for (int l = 0; l < buttonListeners.length; l++) {
                                            if (buttonListeners[l].getClass() == StartStopListener.class ) {
                                                StartStopListener listenerFound = (StartStopListener)buttonListeners[l];
                                                listenerFound.setIsOn(false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if ( sourcePane.getTitleAt(this.tabSelected).equals("+") ) {
            this.gui.closeFileMenuItemSetEnabled(false);
        } else {
            this.gui.closeFileMenuItemSetEnabled(true);
        }
        
        this.frame.pack();
        this.frame.revalidate();
    }

    public int getTabSelected() {
        return this.tabSelected;
    }
}
