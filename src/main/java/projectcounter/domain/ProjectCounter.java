
package projectcounter.domain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.Timer;

public class ProjectCounter extends Timer implements ActionListener, Comparable<ProjectCounter> {
    private final static int TIMER_DELAY = 1000;
    private String projectName;
    private Date createdOn;
    private boolean active;
    private int ticks;
    private int seconds;
    private int minutes;
    private int hours;
    private int days;
    private int weeks;
    private int years;
    private List<Date> dates;
    private JLabel dayCounter;
    private JLabel hourCounter;
    
    public ProjectCounter(String projectName, int ticks, boolean active) {
        super(TIMER_DELAY, null);
        this.projectName = projectName;
        this.createdOn = new Date(Calendar.getInstance().get(Calendar.DATE), 
                Calendar.getInstance().get(Calendar.MONTH) + 1, 
                Calendar.getInstance().get(Calendar.YEAR));
        this.active = active;
        this.ticks = ticks;
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
        this.days = 0;
        this.weeks = 0;
        this.years = 0;
        this.dates = new ArrayList<>();
        
        convertTicksToTime();
        
        addActionListener(this);
        setInitialDelay(0);
        setDelay(1000);
    }

    public String getName() {
        return this.projectName;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public int getTicks() {
        return ticks;
    }
    
    public int getSeconds() {
        return this.seconds;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public int getHours() {
        return this.hours;
    }

    public int getDays() {
        return this.days;
    }

    public int getWeeks() {
        return this.weeks;
    }

    public int getYears() {
        return this.years;
    }

    public List<Date> getDates() {
        return dates;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setDayCounter(JLabel dayCounter) {
        this.dayCounter = dayCounter;
    }
    
    public void addDate(Date date) {
        this.dates.add(date);
    }
    
    private void convertTicksToTime() {
        this.seconds = this.ticks;
        
        while ( this.seconds > 59 ) {
            this.minutes++;
            this.seconds -= 60;
        }
        
        while ( this.minutes > 59 ) {
            this.hours++;
            this.minutes -= 60;
        }
        
        while ( this.hours > 23 ) {
            this.days++;
            this.hours -= 24;
        }
        
        while ( this.days > 6 ) {
            this.weeks++;
            this.days -= 7;
        }
        
        while ( this.weeks > 51 ) {
            this.years++;
            this.weeks -= 52;
        }
    }
    
    public void addTick() {
        this.ticks++;
        this.seconds++;
        
        if ( this.seconds > 59 ) {
            this.minutes++;
            this.seconds -= 60;
            
            if ( this.minutes > 59 ) {
                this.hours++;
                this.minutes -= 60;
                
                if ( this.hours > 23 ) {
                    this.days++;
                    this.hours -= 24;
                    
                    if ( this.days > 6 ) {
                        this.weeks++;
                        this.days -= 7;
                        
                        if ( this.weeks > 51 ) {
                            this.years++;
                            this.weeks -= 52;
                        }
                    }
                }
            }
        }
    }

    public String getYearWeekDay() {
        String string = "";
        
        if ( this.years > 0 ) {
            string += this.years + "y " + this.weeks + "w " 
                    + this.days + "d";
        }
        
        if ( this.years < 1 && this.weeks > 0 ) {
            string += this.weeks + "w " + this.days + "d";
        }
        
        if ( this.years < 1 && this.weeks < 1 && this.days > 0 ) {
            string += this.days + "d";
        }
        
        if ( string.equals("")) {
            string = "0y 0w 0d";
        }
        
        return string;
    }
    
    public String getHourMinuteSecond() {
        return formatTime(hours) + ":" + formatTime(minutes) + ":" 
                + formatTime(seconds);
    }
    
    private String formatTime(int value) {
        if ( value < 10 ) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        addTick();
        this.dayCounter.setText(getYearWeekDay());
        this.hourCounter.setText(getHourMinuteSecond());
    }
    
    @Override
    public String toString() {
        String string = "";
        
        if ( this.years > 0 ) {
            string += this.years + " y, " + this.weeks + " w, " 
                    + this.days + " d, ";
        }
        
        if ( this.years < 1 && this.weeks > 0 ) {
            string += this.weeks + " w, " + this.days + " d, ";
        }
        
        if ( this.years < 1 && this.weeks < 1 && this.days > 0 ) {
            string += this.days + " d, ";
        }
        
        if ( this.hours < 10 ) {
            string += "0" + this.hours + ":";
        } else {
            string += this.hours + ":";
        }
        
        if ( this.minutes < 10 ) {
            string += "0" + this.minutes + ":";
        } else {
            string += this.minutes + ":";
        }
        
        if ( this.seconds < 10 ) {
            string += "0" + this.seconds;
        } else {
            string += this.seconds;
        }
        
        return string;
    }

    @Override
    public int compareTo(ProjectCounter comparedTo) {
        return this.projectName.compareTo(comparedTo.getName());
    }

    public void setHourCounter(JLabel hoursMinutesSeconds) {
        this.hourCounter = hoursMinutesSeconds;
    }
    
    public void setCreatedOn(Date date) {
        this.createdOn = date;
    }
    
}
