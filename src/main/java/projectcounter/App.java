package projectcounter;

import java.io.IOException;
import javax.swing.SwingUtilities;
import projectcounter.gui.TabbedGui;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        TabbedGui gui = new TabbedGui();
        
        SwingUtilities.invokeLater(gui);
    }
}
