package csg.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.OH_ADD_TA_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.OH_EMAIL_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.OH_END_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.OH_NAME_TEXT_FIELD;
import static csg.CourseSiteGeneratorPropertyType.OH_START_COMBOBOX;
import csg.data.CourseSiteGeneratorData;
import csg.data.TimeSlot;
import static csg.workspace.style.OHStyle.CLASS_OH_TEXT_FIELD;
import static csg.workspace.style.OHStyle.CLASS_OH_TEXT_FIELD_ERROR;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class CourseSiteGeneratorFoolproofDesign implements FoolproofDesign {

    CourseSiteGeneratorApp app;

    public CourseSiteGeneratorFoolproofDesign(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        updateAddTAFoolproofDesign();
        updateEditTAFoolproofDesign();
        //timeRangeFoolproofDesign();
    }
    
    private void updateAddTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        // FOOLPROOF DESIGN STUFF FOR ADD TA BUTT ON
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        Button addTAButton = (Button) gui.getGUINode(OH_ADD_TA_BUTTON);

        // FIRST, IF NO TYPE IS SELECTED WE'LL JUST DISABLE
        // THE CONTROLS AND BE DONE WITH IT
        boolean isTypeSelected = data.isTATypeSelected();
        if (!isTypeSelected) {
            nameTextField.setDisable(true);
            emailTextField.setDisable(true);
            addTAButton.setDisable(true);
            return;
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            nameTextField.setDisable(false);
            emailTextField.setDisable(false);
            addTAButton.setDisable(false);
        }

        // NOW, IS THE USER-ENTERED DATA GOOD?
        boolean isLegalNewTA = data.isLegalNewTA(name, email);

        // ENABLE/DISABLE THE CONTROLS APPROPRIATELY
        addTAButton.setDisable(!isLegalNewTA);
        if (isLegalNewTA) {
            nameTextField.setOnAction(addTAButton.getOnAction());
            emailTextField.setOnAction(addTAButton.getOnAction());
        } else {
            nameTextField.setOnAction(null);
            emailTextField.setOnAction(null);
        }

        // UPDATE THE CONTROL TEXT DISPLAY APPROPRIATELY
        boolean isLegalNewName = data.isLegalNewName(name);
        boolean isLegalNewEmail = data.isLegalNewEmail(email);
        foolproofTextField(nameTextField, isLegalNewName);
        foolproofTextField(emailTextField, isLegalNewEmail);
    }
    
    private void updateEditTAFoolproofDesign() {
        
    }
    
    public void foolproofTextField(TextField textField, boolean hasLegalData) {
        if (hasLegalData) {
            textField.getStyleClass().remove(CLASS_OH_TEXT_FIELD_ERROR);
            if (!textField.getStyleClass().contains(CLASS_OH_TEXT_FIELD)) {
                textField.getStyleClass().add(CLASS_OH_TEXT_FIELD);
            }
        } else {
            textField.getStyleClass().remove(CLASS_OH_TEXT_FIELD);
            if (!textField.getStyleClass().contains(CLASS_OH_TEXT_FIELD_ERROR)) {
                textField.getStyleClass().add(CLASS_OH_TEXT_FIELD_ERROR);
            }
        }
    }
    
    /*public void timeRangeFoolproofDesign(){
        AppGUIModule gui = app.getGUIModule();
        
        ComboBox start = (ComboBox) gui.getGUINode(OH_START_COMBOBOX);
        ComboBox end = (ComboBox) gui.getGUINode(OH_END_COMBOBOX);
        String st = (String)start.getSelectionModel().getSelectedItem();
        String en = (String)end.getSelectionModel().getSelectedItem();
        
        CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        
        boolean isTimeRangeLegal = data.isTimeRangeLegal();
        ObservableList<String> startList = start.getItems();
        ObservableList<String> endList = end.getItems();
        ArrayList <TimeSlot> officeHoursTemp = data.getOfficeHoursHold();
        
        if(!isTimeRangeLegal){
            
            if()
            startList.clear();
            endList.clear();
            
            for(TimeSlot timeSlot : officeHoursTemp){
                
            if(convertForCompare(timeSlot.getStartTime()) < convertForCompare(en))
            startList.add(timeSlot.getStartTime());
   
            if(convertForCompare(timeSlot.getEndTime()) > convertForCompare(st))
            endList.add(timeSlot.getEndTime());
            
            
        }
            start.setItems(startList);
            end.setItems(endList);
    }
    }
    
    public int convertForCompare(String temp){
  
        Integer result = 0;
        if(temp == null){
            return 0;
        }
        else{
        
            if(temp.contains("am")){
        
            result = Integer.parseInt(temp.substring(0, temp.indexOf(":")) + temp.substring(temp.indexOf(":") + 1, temp.indexOf("a")));
                    }
        else{
            if(Integer.parseInt(temp.substring(0, temp.indexOf(":"))) == 12){
                result = Integer.parseInt(temp.substring(0, temp.indexOf(":")) + temp.substring(temp.indexOf(":") + 1, temp.indexOf("p")));
            }
            else{
            result = Integer.parseInt(temp.substring(0, temp.indexOf(":")) + temp.substring(temp.indexOf(":") + 1, temp.indexOf("p"))) + 1200;
            }
        }
            
            return result;
    }
}*/
}
