
package projectcounter.gui.listeners;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderChangeListener implements ChangeListener {
    private JFrame frame;
    private JSlider slider;
    
    public SliderChangeListener(JFrame frame, JSlider slider) {
        this.frame = frame;
        this.slider = slider;
    }
    
    @Override
    public void stateChanged(ChangeEvent ce) {
        this.frame.setOpacity((float)this.slider.getValue()/100);
    }
    
}
