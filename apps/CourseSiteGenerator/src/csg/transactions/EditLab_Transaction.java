package csg.transactions;

import csg.data.Lab;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;
import javafx.scene.control.TableView;

/**
 *
 * @author McKillaGorilla
 */
public class EditLab_Transaction implements jTPS_Transaction {
    Lab labToEdit;
    String oldSection, newSection;
    String oldDay, newDay;
    String oldRoom, newRoom;
    String oldTa1, newTa1;
    String oldTa2, newTa2;
    TableView labTV;
    
    public EditLab_Transaction(TableView labTable, Lab initLabToEdit, 
            String section, String day, String room, String ta1, String ta2) {
        labToEdit = initLabToEdit;
        oldSection = initLabToEdit.getSection();
        oldDay = initLabToEdit.getDays();
        oldRoom = initLabToEdit.getRoom();
        oldTa1 = initLabToEdit.getTA1();
        oldTa2 = initLabToEdit.getTA2();
        newSection = section;
        newDay = day;
        newRoom = room;
        newTa1 = ta1;
        newTa2 = ta2;
        labTV = labTable;
        
    }


    @Override
    public void doTransaction() {
        labToEdit.setSection(newSection);
        labToEdit.setDays(newDay);
        labToEdit.setRoom(newRoom);
        labToEdit.setTA1(newTa1);
        labToEdit.setTA2(newTa2);
        labTV.refresh();
    }

    @Override
    public void undoTransaction() {
        labToEdit.setSection(oldSection);
        labToEdit.setDays(oldDay);
        labToEdit.setRoom(oldRoom);
        labToEdit.setTA1(oldTa1);
        labToEdit.setTA2(oldTa2);
        labTV.refresh();
    }
}