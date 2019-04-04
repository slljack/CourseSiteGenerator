package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class Lecture  {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty section;
    private final StringProperty days;
    private final StringProperty time;
    private final StringProperty room;

    /**
     * Constructor initializes both the TA name and email.
     */
    public Lecture() {
        section = new SimpleStringProperty("section");
        days = new SimpleStringProperty("day");
        time = new SimpleStringProperty("time");
        room = new SimpleStringProperty("room");
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getSection() {
        return section.get();
    }

    public void setSection(String initSection) {
        section.set(initSection);
    }

    public String getDays() {
        return days.get();
    }

    public void setDays(String initDays) {
        days.set(initDays);
    }
    
    public String getTime() {
        return time.get();
    }
    
    public void setTime(String initTime) {
        time.set(initTime);
    }
    
    public String getRoom(){
        return room.get();
    }
    
    public void setRoom(String initRoom){
        room.set(initRoom);
    }
}