package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class Schedule{
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty type;
    private final StringProperty days;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty link;

    /**
     * Constructor initializes both the TA name and email.
     */
    public Schedule(String initType, String initDays, String initTitle, String initTopic, String initLink) {
        type = new SimpleStringProperty(initType);
        days = new SimpleStringProperty(initDays);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getType() {
        return type.get();
    }

    public void setType(String initType) {
        type.set(initType);
    }

    public String getDays() {
        return days.get();
    }

    public void setDays(String initDays) {
        days.set(initDays);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setTime(String initTitle) {
        title.set(initTitle);
    }
    
    public String getTopic(){
        return topic.get();
    }
    
    public void setRoom(String initTopic){
        topic.set(initTopic);
    }
    
    public String getLink(){
        return link.get();
    }
    
    public void setLink(String initLink){
        link.set(initLink);
    }
}