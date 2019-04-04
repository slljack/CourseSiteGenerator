package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.M_LAB_TABLE;
import static csg.CourseSiteGeneratorPropertyType.M_LECTURE_TABLE;
import static csg.CourseSiteGeneratorPropertyType.M_RECITATION_TABLE;
import static csg.CourseSiteGeneratorPropertyType.OH_EMAIL_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.OH_FOOLPROOF_SETTINGS;
import static csg.CourseSiteGeneratorPropertyType.OH_NAME_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.OH_NO_SELECTED_CONTENT;
import static csg.CourseSiteGeneratorPropertyType.OH_NO_SELECTED_TITLE;
import static csg.CourseSiteGeneratorPropertyType.OH_NO_TA_SELECTED_CONTENT;
import static csg.CourseSiteGeneratorPropertyType.OH_NO_TA_SELECTED_TITLE;
import static csg.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.OH_TAS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.OH_TA_EDIT_DIALOG;
import static csg.CourseSiteGeneratorPropertyType.SCH_Add_DATEPICKER;
import static csg.CourseSiteGeneratorPropertyType.SCH_LINK_TF;
import static csg.CourseSiteGeneratorPropertyType.SCH_SI_TABLE;
import static csg.CourseSiteGeneratorPropertyType.SCH_TITLE_TF;
import static csg.CourseSiteGeneratorPropertyType.SCH_TOPIC_TF;
import static csg.CourseSiteGeneratorPropertyType.SCH_TYPE_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_HOME_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_HWS_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_EMAIL;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_HP;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_NAME;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_ROOM;
import static csg.CourseSiteGeneratorPropertyType.S_NUMBER_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_SCHEDULE_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_SEMESTER_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_STYLE_FOUR_IMAGE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.S_STYLE_ONE_IMAGE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.S_SUBJECT_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_SYLLABUS_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_YEAR_COMBOBOX;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.Schedule;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.AddLab_Transaction;
import csg.transactions.AddLecture_Transaction;
import csg.transactions.AddRecitation_Transaction;
import csg.transactions.AddSchedule_Transaction;
import csg.transactions.AddSubjectComboBoxData_Transaction;
import csg.transactions.AddTA_Transaction;
import csg.transactions.CheckBox_Transaction;
import csg.transactions.EditLab_Transaction;
import csg.transactions.EditLecture_Transaction;
import csg.transactions.EditRecitation_Transaction;
import csg.transactions.EditTA_Transaction;
import csg.transactions.EditText_Transaction;
import csg.transactions.RemoveLab_Transaction;
import csg.transactions.RemoveLecture_Transaction;
import csg.transactions.RemoveRecitation_Transaction;
import csg.transactions.RemoveSchedule_Transaction;
import csg.transactions.ToggleOfficeHours_Transaction;
import csg.workspace.dialogs.TADialog;
import static djf.AppPropertyType.S_PROGRAM_DATA_NUMBER;
import static djf.AppPropertyType.S_PROGRAM_DATA_SUBJECT;
import static djf.AppPropertyType.S_STYLE_FAV_IMAGE;
import static djf.AppTemplate.PATH_IMAGES;
import static djf.AppTemplate.PATH_PROGRAMDATA;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursController {

    CourseSiteGeneratorApp app;
    String oldText;

    public OfficeHoursController(CourseSiteGeneratorApp initApp) {
        app = initApp;
        oldText = "";
    }
    
    public void processTimeRange(){
       CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
       data.initTimeRange();
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processImageFourFileChooser(){
        AppGUIModule gui = app.getGUIModule();
        FileChooser fc = new FileChooser();
        File selectted = fc.showOpenDialog(null);
        
        Image favicon = new Image(selectted.getPath(), 50, 50, false, true);
        ImageView faviconView = (ImageView)gui.getGUINode(S_STYLE_FOUR_IMAGE_VIEW);
        faviconView.setImage(favicon);
    }
    
    public void processImageOneFileChooser(){
         AppGUIModule gui = app.getGUIModule();
        FileChooser fc = new FileChooser();
        File selectted = fc.showOpenDialog(null);
        //selectted.getPath()
    }
    
    public void processImageTwoFileChooser(){
        AppGUIModule gui = app.getGUIModule();
        FileChooser fc = new FileChooser();
        File selectted = fc.showOpenDialog(null);
        //selectted.getPath()
    }
    
    public void processImageThreeFileChooser(){
        AppGUIModule gui = app.getGUIModule();
        FileChooser fc = new FileChooser();
        File selectted = fc.showOpenDialog(null);
        //selectted.getPath()
    }
    
    
    public void processAddSubject() throws IOException{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUIModule gui = app.getGUIModule();
        ComboBox subject = (ComboBox)gui.getGUINode(S_SUBJECT_COMBOBOX);
        String sItem = subject.getEditor().getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PROGRAMDATA + props.getProperty(S_PROGRAM_DATA_SUBJECT), true));
        PrintWriter out = new PrintWriter(bw);
        AddSubjectComboBoxData_Transaction addSubjectTransaction = new AddSubjectComboBoxData_Transaction(data, sItem);
        app.processTransaction(addSubjectTransaction);
        if(!sItem.equals("") && !subject.getItems().contains(sItem)){
            out.println(sItem);
            out.close();
            
        }
        
    }
    
    public void processAddNumber() throws IOException{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUIModule gui = app.getGUIModule();
        ComboBox number = (ComboBox)gui.getGUINode(S_NUMBER_COMBOBOX);
        String nItem = number.getEditor().getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PROGRAMDATA + props.getProperty(S_PROGRAM_DATA_NUMBER), true));
        PrintWriter out = new PrintWriter(bw);
        AddSubjectComboBoxData_Transaction addSubjectTransaction = new AddSubjectComboBoxData_Transaction(data, nItem);
        app.processTransaction(addSubjectTransaction);
        if(!nItem.equals("") && !number.getItems().contains(nItem)){
            out.println(nItem);
            out.close();
      }
        
        
    }       

    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processHwsCheckBox(){
         CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
         AppGUIModule gui = app.getGUIModule();
         CheckBox hwsCB = (CheckBox)gui.getGUINode(S_HWS_CHECK_BOX);
         
            String name = hwsCB.getText();
            CheckBox_Transaction checkBox = new CheckBox_Transaction(data, name);
                app.processTransaction(checkBox);
    }
    
    public void processHomeCheckBox(){
         CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
         AppGUIModule gui = app.getGUIModule();
         CheckBox homeCb = (CheckBox)gui.getGUINode(S_HOME_CHECK_BOX);
         
            String name = homeCb.getText();
            CheckBox_Transaction checkBox = new CheckBox_Transaction(data, name);
                app.processTransaction(checkBox);
    }
    
    public void processSyllabusCheckBox(){
         CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
         AppGUIModule gui = app.getGUIModule();
         CheckBox syllabusCB = (CheckBox)gui.getGUINode(S_SYLLABUS_CHECK_BOX);
         
            String name = syllabusCB.getText();
            CheckBox_Transaction checkBox = new CheckBox_Transaction(data, name);
                app.processTransaction(checkBox);
    }
    
    public void processScheduleCheckBox(){
         CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
         AppGUIModule gui = app.getGUIModule();
         CheckBox scheduleCB = (CheckBox)gui.getGUINode(S_SCHEDULE_CHECK_BOX);
         
            String name = scheduleCB.getText();
            CheckBox_Transaction checkBox = new CheckBox_Transaction(data, name);
                app.processTransaction(checkBox);
    }
    
    /*public void processText(){
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_NAME));
        TextField emailTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_EMAIL));
        TextField roomTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_ROOM));
        TextField linkTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_HP));
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text = nameTF.getText();
            EditText_Transaction addTitle = new EditText_Transaction(data, text);
                app.processTransaction(addTitle);
    }*/
    
    public void processEditTextArea(boolean done, String oldText, String newText, TextArea textArea){
        if(!done){
            this.oldText = oldText;
        }
        
        else{
            if(!this.oldText.equals(newText)){
                EditText_Transaction transaction = new EditText_Transaction(textArea, this.oldText, newText);
                app.processTransaction(transaction);
            }
        }
    }
    
    

    public void processEditTA() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }
    
    public void processEditLecture(int col, String value){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> lecTable = (TableView<Lecture>) gui.getGUINode(M_LECTURE_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Lecture lec = lecTable.getSelectionModel().getSelectedItem();
            String section = lec.getSection();
            String day = lec.getDays();
            String time = lec.getTime();
            String room = lec.getRoom();
            
            if(col == 0)
                section = value;
            else if(col == 1)
                day = value;
            else if(col == 2)
                time = value;
            else
                room = value;
            
            EditLecture_Transaction editLecTransaction = new EditLecture_Transaction(lecTable, lec, section, day, time, room);
            app.processTransaction(editLecTransaction);
            
    }
    
    public void processEditRecitation(int col, String value){
        AppGUIModule gui = app.getGUIModule();
        TableView<Recitation> recTable = (TableView<Recitation>) gui.getGUINode(M_RECITATION_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Recitation rec = recTable.getSelectionModel().getSelectedItem();
            String section = rec.getSection();
            String day = rec.getDays();
            String room = rec.getRoom();
            String ta1 = rec.getTA1();
            String ta2 = rec.getTA2();
            
            if(col == 0)
                section = value;
            else if(col == 1)
                day = value;
            else if(col == 2)
                room = value;
            else if(col == 3)
                ta1 = value;
            else
                ta2 = value;
            
            EditRecitation_Transaction editRecTransaction = new EditRecitation_Transaction(recTable, rec, section, day, room, ta1, ta2);
            app.processTransaction(editRecTransaction);
    }
    
    public void processEditLab(int col, String value){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lab> labTable = (TableView<Lab>) gui.getGUINode(M_LAB_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Lab lab = labTable.getSelectionModel().getSelectedItem();
            String section = lab.getSection();
            String day = lab.getDays();
            String room = lab.getRoom();
            String ta1 = lab.getTA1();
            String ta2 = lab.getTA2();
            
            if(col == 0)
                section = value;
            else if(col == 1)
                day = value;
            else if(col == 2)
                room = value;
            else if(col == 3)
                ta1 = value;
            else
                ta2 = value;
            
            EditLab_Transaction editLabTransaction = new EditLab_Transaction(labTable, lab, section, day, room, ta1, ta2);
            app.processTransaction(editLabTransaction);
    }
    
    public void processAddLecture(){
        AppGUIModule gui = app.getGUIModule();
        TableView lecTable = (TableView) gui.getGUINode(M_LECTURE_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Lecture lec = new Lecture();
            AddLecture_Transaction addLecTransaction = new AddLecture_Transaction(data, lec);
            app.processTransaction(addLecTransaction);
    }
    
    public void processRemoveLecture(){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> lecTable = (TableView<Lecture>) gui.getGUINode(M_LECTURE_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
        Lecture lec = lecTable.getSelectionModel().getSelectedItem();
        RemoveLecture_Transaction removeLecTransaction = new RemoveLecture_Transaction(data, lec);
        app.processTransaction(removeLecTransaction);
    }
    
    public void processAddRecitation(){
        AppGUIModule gui = app.getGUIModule();
        TableView<Recitation> recTable = (TableView<Recitation>) gui.getGUINode(M_RECITATION_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Recitation rec = new Recitation();
            AddRecitation_Transaction addRecTransaction = new AddRecitation_Transaction(data, rec);
            app.processTransaction(addRecTransaction);
    }
    
    public void processRemoveRecitation(){
        AppGUIModule gui = app.getGUIModule();
        TableView<Recitation> recTable = (TableView<Recitation>) gui.getGUINode(M_RECITATION_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Recitation rec = recTable.getSelectionModel().getSelectedItem();
            RemoveRecitation_Transaction removeRecTransaction = new RemoveRecitation_Transaction(data, rec);
            app.processTransaction(removeRecTransaction);
    }
    
    public void processAddLab(){
       AppGUIModule gui = app.getGUIModule();
       TableView<Lab> labTable = (TableView<Lab>) gui.getGUINode(M_LAB_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Lab lab = new Lab();
            AddLab_Transaction addLabTransaction = new AddLab_Transaction(data, lab);
            app.processTransaction(addLabTransaction);
    }
    
    public void processRemoveLab(){
        AppGUIModule gui = app.getGUIModule();
       TableView<Lab> labTable = (TableView<Lab>) gui.getGUINode(M_LAB_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Lab lab = labTable.getSelectionModel().getSelectedItem();
            RemoveLab_Transaction removeLabTransaction = new RemoveLab_Transaction(data, lab);
            app.processTransaction(removeLabTransaction);
    }
    
    public void processRemoveTA(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
        if(ta != null){
            data.removeTA(ta);
        }
        else{
            Stage window = app.getGUIModule().getWindow();
            AppDialogsFacade.showMessageDialog(window, OH_NO_SELECTED_TITLE, OH_NO_SELECTED_CONTENT);
        }
    }
    
    public void processAddSchedule(){
        AppGUIModule gui = app.getGUIModule();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        String type = ((ComboBox)gui.getGUINode(SCH_TYPE_COMBOBOX)).getSelectionModel().getSelectedItem() + "";
        String date = ((DatePicker)gui.getGUINode(SCH_Add_DATEPICKER)).getValue() + "";
        String title = ((TextField)gui.getGUINode(SCH_TITLE_TF)).getText();
        String topic = ((TextField)gui.getGUINode(SCH_TOPIC_TF)).getText();
        String link = ((TextField)gui.getGUINode(SCH_LINK_TF)).getText();
        
        Schedule sch = new Schedule(type, date, title, topic, link);
        AddSchedule_Transaction addSchTransaction = new AddSchedule_Transaction(data, sch);
        app.processTransaction(addSchTransaction);
    }
    
    public void processRemoveSchedule(){
        AppGUIModule gui = app.getGUIModule();
       TableView<Schedule> schTable = (TableView<Schedule>) gui.getGUINode(SCH_SI_TABLE);
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
            Schedule sch = schTable.getSelectionModel().getSelectedItem();
            RemoveSchedule_Transaction removeSchTransaction = new RemoveSchedule_Transaction(data, sch);
            app.processTransaction(removeSchTransaction);
    }

    public void processSelectAllTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CourseSiteGeneratorData data = (CourseSiteGeneratorData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
}