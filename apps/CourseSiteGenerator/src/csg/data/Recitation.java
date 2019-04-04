package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class Recitation{
    private final StringProperty section;
    private final StringProperty days;
    private final StringProperty room;
    private final StringProperty ta1;
    private final StringProperty ta2;

   
    public Recitation() {
        section = new SimpleStringProperty("section");
        days = new SimpleStringProperty("day");
        ta1 = new SimpleStringProperty("ta1");
        ta2 = new SimpleStringProperty("ta2");
        room = new SimpleStringProperty("room");
    }


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
    
    
    public String getRoom(){
        return room.get();
    }
    
    public void setRoom(String initRoom){
        room.set(initRoom);
    }
    
    public String getTA1() {
        return ta1.get();
    }
    
    public void setTA1(String initTa1) {
        ta1.set(initTa1);
    }
    
    public String getTA2() {
        return ta2.get();
    }
    
    public void setTA2(String initTa2) {
        ta2.set(initTa2);
    }
}