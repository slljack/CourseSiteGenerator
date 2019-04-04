package csg.transactions;

import csg.data.Recitation;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;
import javafx.scene.control.TableView;

/**
 *
 * @author McKillaGorilla
 */
public class EditRecitation_Transaction implements jTPS_Transaction {
    Recitation recToEdit;
    String oldSection, newSection;
    String oldDay, newDay;
    String oldRoom, newRoom;
    String oldTa1, newTa1;
    String oldTa2, newTa2;
    TableView recTV;
    
    public EditRecitation_Transaction(TableView recTable, Recitation initLecToEdit, 
            String section, String day, String room, String ta1, String ta2) {
        recToEdit = initLecToEdit;
        oldSection = initLecToEdit.getSection();
        oldDay = initLecToEdit.getDays();
        oldRoom = initLecToEdit.getRoom();
        oldTa1 = initLecToEdit.getTA1();
        oldTa2 = initLecToEdit.getTA2();
        newSection = section;
        newDay = day;
        newRoom = room;
        newTa1 = ta1;
        newTa2 = ta2;
        recTV = recTable;
        
    }


    @Override
    public void doTransaction() {
        recToEdit.setSection(newSection);
        recToEdit.setDays(newDay);
        recToEdit.setRoom(newRoom);
        recToEdit.setTA1(newTa1);
        recToEdit.setTA2(newTa2);
        recTV.refresh();
    }

    @Override
    public void undoTransaction() {
        recToEdit.setSection(oldSection);
        recToEdit.setDays(oldDay);
        recToEdit.setRoom(oldRoom);
        recToEdit.setTA1(oldTa1);
        recToEdit.setTA2(oldTa2);
        recTV.refresh();
    }
}