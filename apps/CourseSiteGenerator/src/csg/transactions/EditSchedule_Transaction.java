package csg.transactions;

import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.M_LECTURE_TABLE;
import csg.data.Lecture;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;
import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;

/**
 *
 * @author McKillaGorilla
 */
public class EditSchedule_Transaction implements jTPS_Transaction {
    Lecture lecToEdit;
    String oldSection, newSection;
    String oldDay, newDay;
    String oldTime, newTime;
    String oldRoom, newRoom;
    TableView lecTV;
    
    public EditSchedule_Transaction(TableView lecTable, Lecture initLecToEdit, 
            String section, String day, String time, String room) {
        lecToEdit = initLecToEdit;
        oldSection = initLecToEdit.getSection();
        oldDay = initLecToEdit.getDays();
        oldTime = initLecToEdit.getTime();
        oldRoom = initLecToEdit.getRoom();
        newSection = section;
        newDay = day;
        newTime = time;
        newRoom = room;
        lecTV = lecTable;
    }


    @Override
    public void doTransaction() {
        lecToEdit.setSection(newSection);
        lecToEdit.setDays(newDay);
        lecToEdit.setTime(newTime);
        lecToEdit.setRoom(newRoom);
        lecTV.refresh();
    }

    @Override
    public void undoTransaction() {
        lecToEdit.setSection(oldSection);
        lecToEdit.setDays(oldDay);
        lecToEdit.setTime(oldTime);
        lecToEdit.setRoom(oldRoom);
        lecTV.refresh();
    }
}