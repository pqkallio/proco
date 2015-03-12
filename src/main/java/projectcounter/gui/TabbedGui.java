
package projectcounter.gui;

import projectcounter.gui.listeners.ProjectAdderListener;
import projectcounter.gui.listeners.OpenButtonListener;
import projectcounter.gui.listeners.InactivationListener;
import projectcounter.gui.listeners.HandleClosedCountersListener;
import projectcounter.gui.listeners.SliderChangeListener;
import projectcounter.gui.listeners.TabChangeListener;
import projectcounter.gui.listeners.CloseButtonListener;
import projectcounter.gui.listeners.MenuListener;
import projectcounter.gui.listeners.ExitButtonListener;
import projectcounter.gui.listeners.SelectionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionListener;
import projectcounter.domain.Date;
import projectcounter.domain.ProjectCounter;

public class TabbedGui implements Runnable {
    private final static int FRAME_WIDTH = 220;
    private final static int FRAME_HEIGHT = 220;
    private final static Font HEADING_FONT = new Font(Font.SERIF, Font.BOLD, 16);
    private final static Font COUNTER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private final static Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final static String START = "START";
    private final static String STOP = "STOP";
    private final static int[] LAYOUT_COLUMN_WIDTHS = {10,10,10,10,10,10,10,10,10,10,10};
    private final static int[] LAYOUT_ROW_HEIGHTS = {10,10,10,10};
    private final static Insets LAYOUT_INSETS = new Insets(2, 5, 2, 5);
    private final static String SAVE_FILE_NAME = "/savefiles/saved_project_counters.txt";
    private final JSlider OPACITY_SLIDER = new JSlider();
    private final JButton EXIT_BUTTON = new JButton("Exit");
    private final JButton MAKE_INACTIVE_BUTTON = new JButton("Inactivate");
    private final JButton OPEN_BUTTON = new JButton("Open");
    private static JLabel YEAR_DETERMINER = new JLabel("y");
    private static JLabel WEEK_DETERMINER = new JLabel("w");
    private static JLabel DAY_DETERMINER = new JLabel("d");
    private static JLabel TIME_DIVIDER = new JLabel(":");
    private JMenuItem closeFile;
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private Map<String, ProjectCounter> projectCounters;
    private TabChangeListener tabListener;
    
    public TabbedGui() throws IOException {
        this.frame = new JFrame();
        this.projectCounters = new HashMap<>();
        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.tabListener = new TabChangeListener(this, this.frame);
                
        YEAR_DETERMINER.setFont(COUNTER_FONT);
        WEEK_DETERMINER.setFont(COUNTER_FONT);
        DAY_DETERMINER.setFont(COUNTER_FONT);
        TIME_DIVIDER.setFont(COUNTER_FONT);
        
        setUpOpacitySlider();
        
        this.EXIT_BUTTON.addActionListener(new ExitButtonListener(this.frame, this));
        this.MAKE_INACTIVE_BUTTON.addActionListener(new InactivationListener(this, this.tabbedPane, this.tabListener));
        this.OPEN_BUTTON.addActionListener(new OpenButtonListener(this));
        
//        InputStream configStream = getClass().getResourceAsStream(SAVE_FILE);
//        
//        try {
//            this.buffer = new BufferedReader(new InputStreamReader(configStream, "UTF-8"));
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(TabbedGui.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        if ( !loadSavedCounters() ) {
            JOptionPane.showMessageDialog(this.frame, "Unable to load saved counters", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setUpOpacitySlider() {
        this.OPACITY_SLIDER.setMinimum(20);
        this.OPACITY_SLIDER.setMaximum(100);
        this.OPACITY_SLIDER.setMinorTickSpacing(10);
        this.OPACITY_SLIDER.setMajorTickSpacing(20);
        this.OPACITY_SLIDER.setPaintLabels(true);
        this.OPACITY_SLIDER.setPaintTicks(true);
        this.OPACITY_SLIDER.setValue(100);
        this.OPACITY_SLIDER.addChangeListener(new SliderChangeListener(this.frame, this.OPACITY_SLIDER));
    }

    public static String getSAVE_FILE_NAME() {
        return SAVE_FILE_NAME;
    }
    
    private boolean loadSavedCounters() throws IOException {
        Scanner reader;
        
        try {
            InputStream is = this.getClass().getResourceAsStream(SAVE_FILE_NAME);
            reader = new Scanner(is);
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog(this.frame, "Unable to load saved counters: " + e.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] attributes = line.split(":");
            
            String counterName = attributes[0];
            int counterTicks = Integer.parseInt(attributes[1]);
            boolean CounterIsActive = Boolean.parseBoolean(attributes[2]);
            
            String[] createdOnAsArray = attributes[3].split("/");
            int createdOnDay = Integer.parseInt(createdOnAsArray[0]);
            int createdOnMonth = Integer.parseInt(createdOnAsArray[1]);
            int createdOnYear = Integer.parseInt(createdOnAsArray[2]);
            
            Date createdOn = new Date(createdOnDay, createdOnMonth,
                    createdOnYear);
            
            ProjectCounter counter = new ProjectCounter(counterName, counterTicks, CounterIsActive);
            counter.setCreatedOn(createdOn);
            
            this.projectCounters.put(counter.getName(), counter);
            
            if ( attributes.length > 4 ) {
                for (int i = 4; i < attributes.length; i++) {
                    String[] dateAsArray = attributes[i].split("/");
                    
                    int day = Integer.parseInt(dateAsArray[0]);
                    int month = Integer.parseInt(dateAsArray[1]);
                    int year = Integer.parseInt(dateAsArray[2]);
                    
                    Date date = new Date(day, month, year);
                    counter.addDate(date);
                }
            }
        }
        
        reader.close();
        return true;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public static int getFRAME_WIDTH() {
        return FRAME_WIDTH;
    }

    public static int getFRAME_HEIGHT() {
        return FRAME_HEIGHT;
    }

    public static Font getCOUNTER_FONT() {
        return COUNTER_FONT;
    }
    
    public static Font getBUTTON_FONT() {
        return BUTTON_FONT;
    }

    public static String getSTART() {
        return START;
    }

    public static String getSTOP() {
        return STOP;
    }

    public Map<String, ProjectCounter> getProjectCounters() {
        return this.projectCounters;
    }
    
    @Override
    public void run() {
        this.frame.setAlwaysOnTop(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        this.frame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.frame.setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT * 3));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screenSize.getWidth();
        int screenHeight = (int)screenSize.getHeight();
        this.frame.setResizable(false);
        
        this.frame.setLocation(screenWidth - FRAME_WIDTH - 10, 
                screenHeight / 2 - FRAME_HEIGHT / 2);
        this.frame.setUndecorated(true);
        JMenuBar menuBar = createMenuBar();
        this.frame.setJMenuBar(menuBar);
        createComponents();
        
        this.frame.pack();
        this.frame.setVisible(true);
        
    }

    private void createComponents() {
        BorderLayout baseLayout = new BorderLayout(0, 0);
        this.frame.getContentPane().setLayout(baseLayout);
        
        List<ProjectCounter> loadedProjects = new ArrayList<>();
        
        for (ProjectCounter pC : this.projectCounters.values()) {
            loadedProjects.add(pC);
        }
        
        Collections.sort(loadedProjects);
        
        for (ProjectCounter project : loadedProjects) {
            if ( project.isActive() ) {
                JPanel projectPanel = createNewProjectCounterTab(project);
                this.tabbedPane.addTab(project.getName(), projectPanel);
            }
        }
        
        this.tabbedPane.addTab("+", projectAdder());
        
        JScrollPane scrollPane = new JScrollPane(this.tabbedPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        this.tabbedPane.addChangeListener(this.tabListener);
        
        this.frame.getContentPane().add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel projectAdder() {
        JPanel projectAdder = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        projectAdder.setLayout(layout);
        
        layout.columnWidths = LAYOUT_COLUMN_WIDTHS;
        layout.rowHeights = LAYOUT_ROW_HEIGHTS;
        
        JLabel message = new JLabel("Enter project's name:");
        JTextField projectsName = new JTextField();
        JButton createButton = new JButton("Create");
        createButton.setEnabled(false);
        
        createButton.addActionListener(new ProjectAdderListener(this, projectsName));
        projectsName.addKeyListener(new TextFieldChangeListener(createButton));
        
        gbc.insets = LAYOUT_INSETS;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        layout.setConstraints(message, gbc);
        projectAdder.add(message);
        
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(projectsName, gbc);
        projectAdder.add(projectsName);
        
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(createButton, gbc);
        projectAdder.add(createButton);
        
        return projectAdder;
    }
    
    public void addNewProject(String projectName) {
        String name = projectName;
        
        if ( projectName.equals("") ) {
            name = "Project X";
        }
        
        ProjectCounter newProjectCounter = new ProjectCounter(name, 0, true);
        this.projectCounters.put(newProjectCounter.getName(), newProjectCounter);
        this.tabbedPane.addTab(newProjectCounter.getName(), 
                createNewProjectCounterTab(newProjectCounter));
        
        this.frame.revalidate();
        
        reorganizeTabs();
//        Map<String, Component> tabComponents = new HashMap<>();
//        
//        for (int i = 0; i < this.tabbedPane.getTabCount(); i++) {
//            tabComponents.put(this.tabbedPane.getTitleAt(i), this.tabbedPane.getComponentAt(i));
//        }
//        
//        List<String> componentNames = new ArrayList<>();
//        
//        for (String componentName : tabComponents.keySet()) {
//            componentNames.add(componentName);
//        }
//        
//        Collections.sort(componentNames);
//        
//        List<Component> componentsSorted = new ArrayList<>();
//        
//        for (String componentName : componentNames) {
//            for (String tabComponentName : tabComponents.keySet()) {
//                if ( componentName.equals(tabComponentName) ) {
//                    componentsSorted.add(tabComponents.get(tabComponentName));
//                }
//            }
//        }
        
    }
    
    public void addExistingProject(ProjectCounter counter) {
        this.tabbedPane.addTab(counter.getName(), 
                createNewProjectCounterTab(counter));
        
        reorganizeTabs();
    }

    public JPanel createNewProjectCounterTab(ProjectCounter project) {
        return new CounterPanel(project, this);
    }

    public boolean saveProjectCounters() throws IOException  {
        FileWriter writer = null;
        
        try {
            File saveFile = new File(this.getClass().getResource(SAVE_FILE_NAME).toURI());
            System.out.println(saveFile.toString());
            writer = new FileWriter(saveFile);
        } catch ( URISyntaxException | IOException e ) {
            JOptionPane.showMessageDialog(this.frame, "Unable to save counters: \n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        for (ProjectCounter pC : this.projectCounters.values()) {
            String line = "";

            line += pC.getName() + ":" + pC.getTicks() + ":" + pC.isActive();
            
            line += ":" + pC.getCreatedOn().getDay() + "/" 
                    + pC.getCreatedOn().getMonth() + "/" 
                    + pC.getCreatedOn().getYear();

            for (Date date : pC.getDates()) {
                line += ":" + date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
            }

            line += "\n";
            try {
                writer.write(line);
            } catch (IOException ex) {
                Logger.getLogger(TabbedGui.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        writer.close();
        return true;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
//        JPanel buttons = new JPanel();
        BoxLayout footerLayout = new BoxLayout(footer, BoxLayout.Y_AXIS);
//        BoxLayout buttonsLayout = new BoxLayout(buttons, BoxLayout.X_AXIS);
        footer.setLayout(footerLayout);
//        buttons.setLayout(buttonsLayout);
        
        footer.add(this.OPACITY_SLIDER);
        
//        buttons.add(this.EXIT_BUTTON);
//        buttons.add(this.MAKE_INACTIVE_BUTTON);
//        buttons.add(this.OPEN_BUTTON);
        
//        footer.add(buttons);
        
        return footer;
    }
    
    public void openClosedProjectCounters() {
        DefaultListModel listModel = new DefaultListModel();
        List<ProjectCounter> inactiveCounters = new ArrayList<>();
        
        for (ProjectCounter pC : this.projectCounters.values()) {
            if ( !pC.isActive() ) {
                inactiveCounters.add(pC);
            }
        }
        
        Collections.sort(inactiveCounters);
        
        for (ProjectCounter projectCounter : inactiveCounters) {
            listModel.addElement(projectCounter.getName());
        }
        
        JList closedProjectCounters = new JList(listModel);
        closedProjectCounters.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        closedProjectCounters.setLayoutOrientation(JList.VERTICAL_WRAP);
        
        JButton openCounters = new JButton("Open");
        JButton delete = new JButton("Delete");
        JButton close = new JButton("Close");
        
        openCounters.setEnabled(false);
        delete.setEnabled(false);
        
        JButton[] buttons = {openCounters, delete, close};
        
        ListSelectionListener selectionListener = new SelectionListener(openCounters, delete, closedProjectCounters);
        closedProjectCounters.addListSelectionListener(selectionListener);
        
        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(closedProjectCounters);
        
        JFrame openFileFrame = new JFrame("Closed project counters");
        BorderLayout layout = new BorderLayout(5, 0);
        openFileFrame.setLayout(layout);
        
        ActionListener counterHandlerListener = new HandleClosedCountersListener(this, openFileFrame, closedProjectCounters, openCounters, delete);
        openCounters.addActionListener(counterHandlerListener);
        delete.addActionListener(counterHandlerListener);
        close.addActionListener(new CloseButtonListener(openFileFrame));
        
        int frameWidth = 350;
        int frameHeight = 200;
        
        JPanel buttonPanel = new JPanel();
        GridBagLayout buttonLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        buttonPanel.setLayout(buttonLayout);
        
        int[] buttonLayoutWidths = {frameWidth/3, frameWidth/3, frameWidth/3};
        buttonLayout.columnWidths = buttonLayoutWidths;
        
        gbc.insets = new Insets(0, 1, 0, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        
        for (int i = 0; i < buttons.length; i++) {
            gbc.gridx = GridBagConstraints.RELATIVE;
            buttonLayout.setConstraints(buttons[i], gbc);
            buttonPanel.add(buttons[i]);
        }
        
        openFileFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        openFileFrame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        openFileFrame.setLocation(this.frame.getX() - frameWidth, this.frame.getY());
        
        openFileFrame.getContentPane().add(listScroller, BorderLayout.CENTER);
        openFileFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        openFileFrame.pack();
        openFileFrame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu optionsMenu = new JMenu("Options");
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        
        JMenuItem openFile = new JMenuItem("Inactive counters"
                + "", KeyEvent.VK_I);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, 
                ActionEvent.CTRL_MASK));
        
        this.closeFile = new JMenuItem("Close", KeyEvent.VK_C);
        this.closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 
                ActionEvent.CTRL_MASK));
        
        JMenuItem exitProgram = new JMenuItem("Exit", KeyEvent.VK_X);
        exitProgram.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 
                ActionEvent.ALT_MASK));
        
        JCheckBoxMenuItem setAlwaysOnTop = new JCheckBoxMenuItem("Always on top", true);
        setAlwaysOnTop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, 
                ActionEvent.CTRL_MASK));
        
        ActionListener menuListener = new MenuListener(this, this.tabbedPane, 
                this.tabListener, openFile, closeFile, exitProgram, 
                setAlwaysOnTop);
        
        openFile.addActionListener(menuListener);
        closeFile.addActionListener(menuListener);
        exitProgram.addActionListener(menuListener);
        setAlwaysOnTop.addActionListener(menuListener);
        
        fileMenu.add(openFile);
        fileMenu.add(closeFile);
        fileMenu.addSeparator();
        fileMenu.add(exitProgram);
        
        optionsMenu.add(setAlwaysOnTop);
        
        return menuBar;
    }

    public void showHistory(ProjectCounter counter) {
        List<Date> projectDates = counter.getDates();
        
        String[] columnNames = {"#", "Date"};
        Object[][] data = new Object[projectDates.size()][columnNames.length];
        JTable history = null;
        
        if ( !projectDates.isEmpty() ) {
            for (int i = 0; i < projectDates.size(); i++) {
                data[i][0] = i+1;
                data[i][1] = projectDates.get(i);
            }
            history = new JTable(data, columnNames);
        }
        
        JFrame projectHistory = new JFrame("Project history");
        int frameWidth = 220;
        int frameHeight = 250;
        
        BorderLayout layout = new BorderLayout(5, 0);
        projectHistory.getContentPane().setLayout(layout);
        
        GridBagLayout headingLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        JPanel heading = new JPanel();
        heading.setLayout(headingLayout);
        
        projectHistory.setDefaultCloseOperation(closeProjectHistory(this.frame));
        
        JLabel name = new JLabel("Project's name:");
        JLabel projectName = new JLabel(counter.getName());
        JLabel createdOn = new JLabel("Created on:");
        JLabel createdOnDate = new JLabel(counter.getCreatedOn().toString());
        
        int[] headingLayoutWidths = {frameWidth / 2, frameWidth / 2};
        int[] headingLayoutHeights = {15,15};
        
        headingLayout.columnWidths = headingLayoutWidths;
        headingLayout.rowHeights = headingLayoutHeights;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        headingLayout.setConstraints(name, gbc);
        heading.add(name);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        headingLayout.setConstraints(projectName, gbc);
        heading.add(projectName);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        headingLayout.setConstraints(createdOn, gbc);
        heading.add(createdOn);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        headingLayout.setConstraints(createdOnDate, gbc);
        heading.add(createdOnDate);
        
        projectHistory.setPreferredSize(new Dimension(frameWidth, frameHeight));
        
        projectHistory.setLocation(this.frame.getX() - frameWidth, this.frame.getY());
        
        JScrollPane pane;
        if ( history != null ) {
            pane = new JScrollPane(history);
            history.setFillsViewportHeight(true);
        } else {
            JLabel noHistory = new JLabel("No project history available");
            noHistory.setHorizontalTextPosition(JLabel.CENTER);
            noHistory.setVerticalTextPosition(JLabel.CENTER);
            pane = new JScrollPane(noHistory);
        }
        
        projectHistory.getContentPane().add(heading, BorderLayout.NORTH);
        projectHistory.getContentPane().add(pane, BorderLayout.CENTER);
        
        projectHistory.pack();
        projectHistory.setVisible(true);
    }
    
    public void closeFileMenuItemSetEnabled(boolean enabled) {
        this.closeFile.setEnabled(enabled);
    }

    private int closeProjectHistory(JFrame mainFrame) {
        return 2;
    }

    private void reorganizeTabs() {
        List<CounterPanel> counterPanels = new ArrayList<>();
        
        for (int i = 0; i < this.tabbedPane.getTabCount(); i++) {
            if ( this.tabbedPane.getComponentAt(i).getClass() == CounterPanel.class ) {
                CounterPanel panel = (CounterPanel)this.tabbedPane.getComponentAt(i);
                counterPanels.add(panel);
            }
        }
        
        Collections.sort(counterPanels);
        
        this.tabbedPane.removeChangeListener(tabListener);
        while ( this.tabbedPane.getTabCount() > 0 ) {
            this.tabbedPane.removeTabAt(0);
        }
        
        for (CounterPanel counterPanel : counterPanels) {
            this.tabbedPane.addTab(counterPanel.getPc().getName(), createNewProjectCounterTab(counterPanel.getPc()));
        }
        
        this.tabbedPane.addChangeListener(tabListener);
        this.tabbedPane.addTab("+", projectAdder());
    }
}
