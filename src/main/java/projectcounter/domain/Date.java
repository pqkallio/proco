
package projectcounter.domain;

public class Date {
    private int day;
    private int month;
    private int year;
    
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        
        Date comp = (Date)obj;
        
        if (this.year != comp.getYear()) {
            return false;
        }
        
        if (this.month != comp.getMonth()) {
            return false;
        }
        
        if (this.day != comp.getDay()) {
            return false;
        }
        
        return true;    
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.day;
        hash = 37 * hash + this.month;
        hash = 37 * hash + this.year;
        return hash;
    }
    
    @Override
    public String toString() {
        return this.day + "." + this.month + "." + this.year;
    }
}
