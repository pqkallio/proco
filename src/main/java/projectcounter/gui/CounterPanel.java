
package projectcounter.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import projectcounter.domain.ProjectCounter;
import projectcounter.gui.listeners.HistoryButtonListener;
import projectcounter.gui.listeners.StartStopListener;

public class CounterPanel extends JPanel implements Comparable<CounterPanel> {
    private final static int FRAME_WIDTH = TabbedGui.getFRAME_WIDTH() / 2;
    private final static int FRAME_HEIGHT = TabbedGui.getFRAME_HEIGHT() / 2;
    private ProjectCounter pc;
    private TabbedGui gui;
    
    public CounterPanel(ProjectCounter pc, TabbedGui gui) {
        this.pc = pc;
        this.gui = gui;
        
        add(createContent());
    }

    private JPanel createContent() {
        JPanel outerCounterPanel = new JPanel();
        JPanel innerCounterPanelCenter = innerCounterPanelCenter();
        JPanel innerCounterPanelSouth = innerCounterPanelSouth();
        
        BorderLayout outerLayoutCenter = new BorderLayout(5, 0);
        
        outerCounterPanel.setLayout(outerLayoutCenter);
        
        outerCounterPanel.add(innerCounterPanelCenter, BorderLayout.CENTER);
        outerCounterPanel.add(innerCounterPanelSouth, BorderLayout.SOUTH);
        
        return outerCounterPanel;
    }

    private JPanel innerCounterPanelCenter() {
        JPanel innerCounterPanelCenter = new JPanel();
        GridBagLayout innerLayoutCenter = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        innerCounterPanelCenter.setLayout(innerLayoutCenter);
        
        int[] rowHeights = {FRAME_HEIGHT / 4, FRAME_HEIGHT / 4};
        innerLayoutCenter.rowHeights = rowHeights;
        
        JLabel yearsWeeksDays = new JLabel(pc.getYearWeekDay());
        yearsWeeksDays.setFont(TabbedGui.getCOUNTER_FONT());
        
        JLabel hoursMinutesSeconds = new JLabel(pc.getHourMinuteSecond());
        hoursMinutesSeconds.setFont(TabbedGui.getCOUNTER_FONT());
        
        gbc.insets = new Insets(2, 0, 3, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        innerLayoutCenter.setConstraints(yearsWeeksDays, gbc);
        innerCounterPanelCenter.add(yearsWeeksDays);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        innerLayoutCenter.setConstraints(hoursMinutesSeconds, gbc);
        innerCounterPanelCenter.add(hoursMinutesSeconds);
        
        pc.setDayCounter(yearsWeeksDays);
        pc.setHourCounter(hoursMinutesSeconds);
        
        return innerCounterPanelCenter;
    }

    private JPanel innerCounterPanelSouth() {
        JPanel innerCounterPanelSouth = new JPanel();
        
        GridBagLayout innerLayoutSouth = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        innerCounterPanelSouth.setLayout(innerLayoutSouth);
        
        int[] columnWidths = {FRAME_WIDTH / 2, FRAME_WIDTH / 2};
        innerLayoutSouth.columnWidths = columnWidths;
        
        JButton startStopButton = new JButton("START");
        JButton historyButton = new JButton("History");
        
        startStopButton.setFont(TabbedGui.getBUTTON_FONT());
        startStopButton.setFont(startStopButton.getFont().deriveFont(Font.BOLD));
        startStopButton.setBackground(new Color(50, 255, 100));
        startStopButton.addActionListener(new StartStopListener(gui, startStopButton, pc));
        
        historyButton.setFont(TabbedGui.getBUTTON_FONT());
        historyButton.addActionListener(new HistoryButtonListener(gui, pc));
        
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 4, 4, 4);
        
        gbc.gridx = GridBagConstraints.RELATIVE;
        innerLayoutSouth.setConstraints(startStopButton, gbc);
        innerCounterPanelSouth.add(startStopButton);
        
        gbc.gridx = GridBagConstraints.RELATIVE;
        innerLayoutSouth.setConstraints(historyButton, gbc);
        innerCounterPanelSouth.add(historyButton);
        
        return innerCounterPanelSouth;
    }

    public ProjectCounter getPc() {
        return pc;
    }

    @Override
    public int compareTo(CounterPanel comparedTo) {
        return this.pc.getName().compareToIgnoreCase(comparedTo.getPc().getName());
    }
}
