package csg.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.M_LAB_TABLE;
import static csg.CourseSiteGeneratorPropertyType.M_LECTURE_TABLE;
import static csg.CourseSiteGeneratorPropertyType.M_RECITATION_TABLE;
import static csg.CourseSiteGeneratorPropertyType.OH_ALL_RADIO_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.OH_END_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.OH_GRAD_RADIO_BUTTON;
import static csg.CourseSiteGeneratorPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.OH_START_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.OH_TAS_TABLE_VIEW;
import static csg.CourseSiteGeneratorPropertyType.SCH_SI_TABLE;
import static csg.CourseSiteGeneratorPropertyType.SY_ACA_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_DES_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_GRADED_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_GRADING_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_OUT_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_PRE_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_SPE_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_TEXT_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.SY_TOP_TEXT_AREA;
import static csg.CourseSiteGeneratorPropertyType.S_HOME_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_HWS_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_EMAIL;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_HP;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_NAME;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_OH;
import static csg.CourseSiteGeneratorPropertyType.S_INSTRUCTOR_ROOM;
import static csg.CourseSiteGeneratorPropertyType.S_NUMBER_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_SCHEDULE_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_SEMESTER_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_SUBJECT_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_SYLLABUS_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_YEAR_COMBOBOX;
import csg.data.TimeSlot.DayOfWeek;
import static djf.AppPropertyType.S_BANNER_TITLE_TEXT_FIELD;
import static djf.AppPropertyType.S_EXPORT_TEXT_LABEL;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 */
public class CourseSiteGeneratorData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteGeneratorApp app;
    
    // THESE ARE ALL THE TEACHING ASSISTANTS
    HashMap<TAType, ArrayList<TeachingAssistantPrototype>> allTAs;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistantPrototype> teachingAssistants;
    ObservableList<TimeSlot> officeHours;  
    ArrayList<TimeSlot> officeHoursHold;
    ArrayList<Lecture> lecList;
    ArrayList<Recitation> recList;
    ArrayList<Lab> labList;
    ArrayList<Schedule> schList;
    
    ObservableList<Lecture> lecs;
    ObservableList<Recitation> recs;
    ObservableList<Lab> labs;
    ObservableList<Schedule> schs;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 8;
    public static final int MAX_END_HOUR = 22;

    String subject;
    String number;
    String semester;
    String year;
    
    String exportD;
    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CourseSiteGeneratorData(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        AppGUIModule gui = app.getGUIModule();

        // SETUP THE DATA STRUCTURES
        allTAs = new HashMap();
        allTAs.put(TAType.Graduate, new ArrayList());
        allTAs.put(TAType.Undergraduate, new ArrayList());
        
        // GET THE LIST OF TAs FOR THE LEFT TABLE
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        teachingAssistants = taTableView.getItems();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        //
        lecList = new ArrayList<Lecture>();
        recList = new ArrayList<Recitation>();
        labList = new ArrayList<Lab>();
        schList = new ArrayList<Schedule>();
        //lecList = new ArrayList<Lecture>();
        
        //subject = (String)((ComboBox) gui.getGUINode(S_SUBJECT_COMBOBOX)).getSelectionModel().getSelectedItem();
        
        TableView<Lecture> lecTable = (TableView)gui.getGUINode(M_LECTURE_TABLE);
        TableView<Recitation> recTable = (TableView)gui.getGUINode(M_RECITATION_TABLE);
        TableView<Lab> labTable = (TableView)gui.getGUINode(M_LAB_TABLE);
        TableView<Schedule> schTable = (TableView)gui.getGUINode(SCH_SI_TABLE);
        exportD = ((Label)gui.getGUINode(S_EXPORT_TEXT_LABEL)).getText();

        
        lecs = lecTable.getItems();
        recs = recTable.getItems();
        labs = labTable.getItems();
        schs = schTable.getItems();
        
        
        resetOfficeHours();
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    
    public String getSubject(){
        AppGUIModule gui = app.getGUIModule();
        
        return (String)((ComboBox) gui.getGUINode(S_SUBJECT_COMBOBOX)).getSelectionModel().getSelectedItem();
    }
     public String getNumber(){
        AppGUIModule gui = app.getGUIModule();

        return (String)((ComboBox) gui.getGUINode(S_NUMBER_COMBOBOX)).getSelectionModel().getSelectedItem();
    }
      public String getSemester(){
        AppGUIModule gui = app.getGUIModule();
        return (String)((ComboBox) gui.getGUINode(S_SEMESTER_COMBOBOX)).getSelectionModel().getSelectedItem();
    }
       public Integer getYear(){
        AppGUIModule gui = app.getGUIModule();
        return (Integer)((ComboBox) gui.getGUINode(S_YEAR_COMBOBOX)).getSelectionModel().getSelectedItem();
    }
    
    // PRIVATE HELPER METHODS
    
    private void sortTAs() {
        Collections.sort(teachingAssistants);
    }
    
    public String getExportD(){
        return exportD;
    }
    public void setExportD(String export){
        exportD = export;
    }
    
    private void resetOfficeHours() {
        //THIS WILL STORE OUR OFFICE HOURS
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHours = officeHoursTableView.getItems(); 
        officeHoursHold = new ArrayList();
        officeHours.clear();
        for (int i = startHour; i <= endHour; i++) {
            TimeSlot timeSlot = new TimeSlot(   this.getTimeString(i, true),
                                                this.getTimeString(i, false));
            officeHours.add(timeSlot);
            officeHoursHold.add(timeSlot);
            
            TimeSlot halfTimeSlot = new TimeSlot(   this.getTimeString(i, false),
                                                    this.getTimeString(i+1, true));
            officeHours.add(halfTimeSlot);
            officeHoursHold.add(halfTimeSlot);
            
        }
        
        
        
        
        initTimeRange();
    }
    
    
    public void initTimeRange(){
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        
        
        ComboBox start = (ComboBox) gui.getGUINode(OH_START_COMBOBOX);
        ComboBox end = (ComboBox) gui.getGUINode(OH_END_COMBOBOX);
        ObservableList<String> startList = FXCollections.observableArrayList();
        ObservableList<String> endList = FXCollections.observableArrayList();
        Integer startTemp = 0;
        Integer endTemp = 0;
        Integer startS = 0;
        Integer endS = 0;
        for(TimeSlot timeSlot : officeHoursHold){
            
            startList.add(timeSlot.getStartTime());
            endList.add(timeSlot.getEndTime());
            
        }
        
        start.setItems(startList);
        end.setItems(endList);
    
            String st = (String)start.getSelectionModel().getSelectedItem();
            String en = (String)end.getSelectionModel().getSelectedItem();
            
            if(st != null && en != null){
        if(st.contains("am")){
        
            startTemp = Integer.parseInt(st.substring(0, st.indexOf(":")) + st.substring(st.indexOf(":") + 1, st.indexOf("a")));
                    }
        else{
            if(Integer.parseInt(st.substring(0, st.indexOf(":"))) == 12){
                startTemp = Integer.parseInt(st.substring(0, st.indexOf(":")) + st.substring(st.indexOf(":") + 1, st.indexOf("p")));
            }
            else{
            startTemp = Integer.parseInt(st.substring(0, st.indexOf(":")) + st.substring(st.indexOf(":") + 1, st.indexOf("p"))) + 1200;
            }
        }
                    
    
    if(en.contains("am")){
        endTemp = Integer.parseInt(en.substring(0, en.indexOf(":")) + en.substring(en.indexOf(":") + 1, en.indexOf("a")));
                    }
    else{
        if(Integer.parseInt(en.substring(0, en.indexOf(":"))) == 12){
                endTemp = Integer.parseInt(en.substring(0, en.indexOf(":")) + en.substring(en.indexOf(":") + 1, en.indexOf("p")));
            }
        else{
            endTemp = endTemp = Integer.parseInt(en.substring(0, en.indexOf(":")) + en.substring(en.indexOf(":")+ 1, en.indexOf("p"))) + 1200;
        }
    }
    
    officeHours.clear();
    
     for(TimeSlot  timeslot : officeHoursHold){
         String startHold = timeslot.getStartTime();
         String endHold = timeslot.getEndTime();
         //System.out.println(timeslot.getStartTime());
         
         //System.out.println(timeslot.getEndTime());
         if(timeslot.getStartTime().contains("am")){
        
            startS = Integer.parseInt(timeslot.getStartTime().substring(0, startHold.indexOf(":")) + timeslot.getStartTime().substring(startHold.indexOf(":") + 1, startHold.indexOf("a")));
                    }
         else{
             if(Integer.parseInt(timeslot.getStartTime().substring(0, startHold.indexOf(":"))) == 12){
                 startS = Integer.parseInt(timeslot.getStartTime().substring(0, startHold.indexOf(":")) + timeslot.getStartTime().substring(startHold.indexOf(":") + 1, startHold.indexOf("p")));
             }else{
         
            startS = Integer.parseInt(timeslot.getStartTime().substring(0, startHold.indexOf(":")) + timeslot.getStartTime().substring(startHold.indexOf(":") + 1, startHold.indexOf("p"))) + 1200;
             }
         }
         
    if(timeslot.getEndTime().contains("am")){
            endS = Integer.parseInt(timeslot.getEndTime().substring(0, endHold.indexOf(":")) + timeslot.getEndTime().substring(endHold.indexOf(":") + 1, endHold.indexOf("a")));
                    }
    else{
        if(Integer.parseInt(timeslot.getEndTime().substring(0, endHold.indexOf(":"))) == 12){
            endS = Integer.parseInt(timeslot.getEndTime().substring(0, endHold.indexOf(":")) + timeslot.getEndTime().substring(endHold.indexOf(":") + 1, endHold.indexOf("p")));
        }
        else{
                endS = Integer.parseInt(timeslot.getEndTime().substring(0, endHold.indexOf(":")) + timeslot.getEndTime().substring(endHold.indexOf(":") + 1, endHold.indexOf("p"))) + 1200;
        }
    }
    //System.out.println(startS);
            if(startS >= startTemp && endS <= endTemp ){
                
                officeHours.add(timeslot);
                //System.out.println("11");
            }
        }
     
    }
    }
    private String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    // METHODS TO OVERRIDE
        
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void reset() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        AppGUIModule gui = app.getGUIModule();
        
        for (TimeSlot timeSlot : officeHours) {
            timeSlot.reset();
        }
        
        ((ComboBox)gui.getGUINode(S_SUBJECT_COMBOBOX)).setValue("");
        ((ComboBox)gui.getGUINode(S_NUMBER_COMBOBOX)).setValue("");
        ((ComboBox)gui.getGUINode(S_SEMESTER_COMBOBOX)).setValue("");
        ((ComboBox)gui.getGUINode(S_YEAR_COMBOBOX)).setValue(2018);
        ((TextField)gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD)).setText("");
        ((Label)gui.getGUINode(S_EXPORT_TEXT_LABEL)).setText("");
        ((CheckBox)gui.getGUINode(S_HOME_CHECK_BOX)).setSelected(false);
        ((CheckBox)gui.getGUINode(S_SYLLABUS_CHECK_BOX)).setSelected(false);
        ((CheckBox)gui.getGUINode(S_SCHEDULE_CHECK_BOX)).setSelected(false);
        ((CheckBox)gui.getGUINode(S_HWS_CHECK_BOX)).setSelected(false);
        ((TextField)gui.getGUINode(S_INSTRUCTOR_NAME)).setText("");
        ((TextField)gui.getGUINode(S_INSTRUCTOR_ROOM)).setText("");
        ((TextField)gui.getGUINode(S_INSTRUCTOR_EMAIL)).setText("");
        ((TextField)gui.getGUINode(S_INSTRUCTOR_HP)).setText("");
        ((TextArea)gui.getGUINode(S_INSTRUCTOR_OH)).setText("");
        ((TextArea)gui.getGUINode(SY_DES_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_TOP_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_PRE_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_OUT_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_TEXT_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_GRADED_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_GRADING_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_ACA_TEXT_AREA)).setText("");
        ((TextArea)gui.getGUINode(SY_SPE_TEXT_AREA)).setText("");
        
        
        
        
    }
    
    // SERVICE METHODS
    
    public void initSubject(String subject){
        AppGUIModule gui = app.getGUIModule();
        ((ComboBox)gui.getGUINode(S_SUBJECT_COMBOBOX)).setValue(subject);
    }
    
    public void initNumber(String number){
        AppGUIModule gui = app.getGUIModule();
        ((ComboBox)gui.getGUINode(S_NUMBER_COMBOBOX)).setValue(number);
    }
    
    public void initSemester(String semester){
        AppGUIModule gui = app.getGUIModule();
        ((ComboBox)gui.getGUINode(S_SEMESTER_COMBOBOX)).setValue(semester);
    }
    
    public void initYear(Integer year){
        AppGUIModule gui = app.getGUIModule();
        ((ComboBox)gui.getGUINode(S_YEAR_COMBOBOX)).setValue(year);
    }
    
    public void initTitle(String title){
        AppGUIModule gui = app.getGUIModule();
        ((TextField)gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD)).setText(title);
    }
    
    public void initInstructorName(String name){
        AppGUIModule gui = app.getGUIModule();
        ((TextField)gui.getGUINode(S_INSTRUCTOR_NAME)).setText(name);
    }
    
    public void initInstructorRoom(String room){
        AppGUIModule gui = app.getGUIModule();
        ((TextField)gui.getGUINode(S_INSTRUCTOR_ROOM)).setText(room);
    }
    
    public void initInstructorLink(String link){
        AppGUIModule gui = app.getGUIModule();
        ((TextField)gui.getGUINode(S_INSTRUCTOR_HP)).setText(link);
    }
    
    public void initInstructorEmail(String email){
        AppGUIModule gui = app.getGUIModule();
        ((TextField)gui.getGUINode(S_INSTRUCTOR_EMAIL)).setText(email);
    }
    
    public void initInstructorHours(String hours){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(S_INSTRUCTOR_OH)).setText(hours);
    }
    
    public void initDescription(String description){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_DES_TEXT_AREA)).setText(description);
    }
    public void initTopics(String topics){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_TOP_TEXT_AREA)).setText(topics);
    }
    public void initPrerequisites(String prerequisites){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_PRE_TEXT_AREA)).setText(prerequisites);
    }
    public void initOutcomes(String outcomes){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_OUT_TEXT_AREA)).setText(outcomes);
    }
    public void initTextbooks(String textbooks){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_TEXT_TEXT_AREA)).setText(textbooks);
    }
    public void initGraded(String graded){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_GRADED_TEXT_AREA)).setText(graded);
    }
    public void initGrading(String graing){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_GRADING_TEXT_AREA)).setText(graing);
    }
    public void initAca(String aca){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_ACA_TEXT_AREA)).setText(aca);
    }
    public void initSpe(String spe){
        AppGUIModule gui = app.getGUIModule();
        ((TextArea)gui.getGUINode(SY_SPE_TEXT_AREA)).setText(spe);
    }
    
    
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if (initStartHour <= initEndHour) {
            // THESE ARE VALID HOURS SO KEEP THEM
            // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
            startHour = initStartHour;
            endHour = initEndHour;
        }
        resetOfficeHours();
    }
    
    public void addText(String text){
        AppGUIModule gui = app.getGUIModule();
        TextField tf = (TextField) gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD);
       
        
    }
    
    public void removeText(String text){
        AppGUIModule gui = app.getGUIModule();
        TextField tf = (TextField) gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD);
        
    }
    
    public void CheckBox(){
        
    }
    
    public void UncheckBox(){
        
    }
    
    public void addTA(TeachingAssistantPrototype ta) {
        if (!hasTA(ta)) {
            TAType taType = TAType.valueOf(ta.getType());
            ArrayList<TeachingAssistantPrototype> tas = allTAs.get(taType);
            tas.add(ta);
            this.updateTAs();
        }
    }

    public void addTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        addTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.addTA(dow, ta);
            }
        }
    }
    
    public void removeTA(TeachingAssistantPrototype ta) {
        // REMOVE THE TA FROM THE LIST OF TAs
        TAType taType = TAType.valueOf(ta.getType());
        allTAs.get(taType).remove(ta);
        
        // REMOVE THE TA FROM ALL OF THEIR OFFICE HOURS
        for (TimeSlot timeSlot : officeHours) {
            timeSlot.removeTA(ta);
        }
        
        // AND REFRESH THE TABLES
        this.updateTAs();
    }

    public void removeTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        removeTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.removeTA(dow, ta);
            }
        }    
    }
    
    public DayOfWeek getColumnDayOfWeek(int columnNumber) {
        return TimeSlot.DayOfWeek.values()[columnNumber-2];
    }

    public TeachingAssistantPrototype getTAWithName(String name) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getName().equals(name))
                return ta;
        }
        return null;
    }

    public TeachingAssistantPrototype getTAWithEmail(String email) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getEmail().equals(email))
                return ta;
        }
        return null;
    }

    public TimeSlot getTimeSlot(String startTime) {
        Iterator<TimeSlot> timeSlotsIterator = officeHours.iterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            String timeSlotStartTime = timeSlot.getStartTime().replace(":", "_");
            if (timeSlotStartTime.equals(startTime))
                return timeSlot;
        }
        return null;
    }

    public TAType getSelectedType() {
        RadioButton allRadio = (RadioButton)app.getGUIModule().getGUINode(OH_ALL_RADIO_BUTTON);
        if (allRadio.isSelected())
            return TAType.All;
        RadioButton gradRadio = (RadioButton)app.getGUIModule().getGUINode(OH_GRAD_RADIO_BUTTON);
        if (gradRadio.isSelected())
            return TAType.Graduate;
        else
            return TAType.Undergraduate;
    }

    public TeachingAssistantPrototype getSelectedTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TeachingAssistantPrototype> tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem();
    }
    
    public HashMap<TimeSlot, ArrayList<DayOfWeek>> getTATimeSlots(TeachingAssistantPrototype ta) {
        HashMap<TimeSlot, ArrayList<DayOfWeek>> timeSlots = new HashMap();
        for (TimeSlot timeSlot : officeHours) {
            if (timeSlot.hasTA(ta)) {
                ArrayList<DayOfWeek> daysForTA = timeSlot.getDaysForTA(ta);
                timeSlots.put(timeSlot, daysForTA);
            }
        }
        return timeSlots;
    }
    
    private boolean hasTA(TeachingAssistantPrototype testTA) {
        return allTAs.get(TAType.Graduate).contains(testTA)
                ||
                allTAs.get(TAType.Undergraduate).contains(testTA);
    }
    
    public boolean isTASelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isLegalNewTA(String name, String email) {
        if ((name.trim().length() > 0)
                && (email.trim().length() > 0)) {
            // MAKE SURE NO TA ALREADY HAS THE SAME NAME
            TAType type = this.getSelectedType();
            TeachingAssistantPrototype testTA = new TeachingAssistantPrototype(name, email, type);
            if (this.teachingAssistants.contains(testTA))
                return false;
            if (this.isLegalNewEmail(email)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegalNewName(String testName) {
        if (testName.trim().length() > 0) {
            for (TeachingAssistantPrototype testTA : this.teachingAssistants) {
                if (testTA.getName().equals(testName))
                    return false;
            }
            return true;
        }
        return false;
    }
    
    public boolean isLegalNewEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
            for (TeachingAssistantPrototype ta : this.teachingAssistants) {
                if (ta.getEmail().equals(email.trim()))
                    return false;
            }
            return true;
        }
        else return false;
    }
    
    public boolean isDayOfWeekColumn(int columnNumber) {
        return columnNumber >= 2;
    }
    
    public boolean isTATypeSelected() {
        AppGUIModule gui = app.getGUIModule();
        RadioButton allRadioButton = (RadioButton)gui.getGUINode(OH_ALL_RADIO_BUTTON);
        return !allRadioButton.isSelected();
    }
    
    public boolean isTimeRangeLegal(){
        AppGUIModule gui = app.getGUIModule();
        
        ComboBox start = (ComboBox) gui.getGUINode(OH_START_COMBOBOX);
        ComboBox end = (ComboBox) gui.getGUINode(OH_END_COMBOBOX);
        Integer startTemp = 0;
        Integer endTemp = 0;
        Integer startS = 0;
        Integer endS = 0;
        
        String st = (String)start.getSelectionModel().getSelectedItem();
        String en = (String)end.getSelectionModel().getSelectedItem();
            
            if(st != null){
        if(st.contains("am")){
        
            startTemp = Integer.parseInt(st.substring(0, st.indexOf(":")) + st.substring(st.indexOf(":") + 1, st.indexOf("a")));
                    }
        else{
            if(Integer.parseInt(st.substring(0, st.indexOf(":"))) == 12){
                startTemp = Integer.parseInt(st.substring(0, st.indexOf(":")) + st.substring(st.indexOf(":") + 1, st.indexOf("p")));
            }
            else{
            startTemp = Integer.parseInt(st.substring(0, st.indexOf(":")) + st.substring(st.indexOf(":") + 1, st.indexOf("p"))) + 1200;
            }
        }
            }      
    
            if(en != null){
    if(en.contains("am")){
        endTemp = Integer.parseInt(en.substring(0, en.indexOf(":")) + en.substring(en.indexOf(":") + 1, en.indexOf("a")));
                    }
    else{
        if(Integer.parseInt(en.substring(0, en.indexOf(":"))) == 12){
                endTemp = Integer.parseInt(en.substring(0, en.indexOf(":")) + en.substring(en.indexOf(":") + 1, en.indexOf("p")));
            }
        else{
            endTemp = endTemp = Integer.parseInt(en.substring(0, en.indexOf(":")) + en.substring(en.indexOf(":")+ 1, en.indexOf("p"))) + 1200;
        }
    }
    }
            
        if(startTemp != 0 || endTemp != 0)
                return (startTemp < endTemp);
            
        else
                return true;
    }
    
    public boolean isValidTAEdit(TeachingAssistantPrototype taToEdit, String name, String email) {
        if (!taToEdit.getName().equals(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        if (!taToEdit.getEmail().equals(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }

    public boolean isValidNameEdit(TeachingAssistantPrototype taToEdit, String name) {
        if (!taToEdit.getName().equals(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        return true;
    }

    public boolean isValidEmailEdit(TeachingAssistantPrototype taToEdit, String email) {
        if (!taToEdit.getEmail().equals(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }    

    public void updateTAs() {
        TAType type = getSelectedType();
        selectTAs(type);
    }
    
    public void selectTAs(TAType type) {
        teachingAssistants.clear();
        Iterator<TeachingAssistantPrototype> tasIt = this.teachingAssistantsIterator();
        while (tasIt.hasNext()) {
            TeachingAssistantPrototype ta = tasIt.next();
            if (type.equals(TAType.All)) {
                teachingAssistants.add(ta);
            }
            else if (ta.getType().equals(type.toString())) {
                teachingAssistants.add(ta);
            }
        }
        
        // SORT THEM BY NAME
        sortTAs();

        // CLEAR ALL THE OFFICE HOURS
        Iterator<TimeSlot> officeHoursIt = officeHours.iterator();
        while (officeHoursIt.hasNext()) {
            TimeSlot timeSlot = officeHoursIt.next();
            timeSlot.filter(type);
        }
        
        app.getFoolproofModule().updateAll();
    }
    
    public void addSubject(){
        
    }
    
    public void removeSubject(){
        
    }
    
    public void addNumber(){
        
    }
    
    public void removeNumber(){
        
    }
    
    public void addLecture(Lecture lec){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> lecTable = (TableView<Lecture>) gui.getGUINode(M_LECTURE_TABLE);
        
        lecs.add(lec);
        lecTable.refresh();
        lecList.add(lec);
    }
    
    public void removeLecture(Lecture lec){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> lecTable = (TableView<Lecture>) gui.getGUINode(M_LECTURE_TABLE);
        
        lecs.remove(lec);
        lecTable.refresh();
        lecList.remove(lec);
    }
    
    public void addRecitation(Recitation rec){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> recTable = (TableView<Lecture>) gui.getGUINode(M_RECITATION_TABLE);
        
        recs.add(rec);
        recTable.refresh();
        recList.add(rec);
    }
    
    public void removeRecitation(Recitation rec){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> recTable = (TableView<Lecture>) gui.getGUINode(M_RECITATION_TABLE);
        
        recs.remove(rec);
        recTable.refresh();
        recList.remove(rec);
    }
    
    public void addLab(Lab lab){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> labTable = (TableView<Lecture>) gui.getGUINode(M_LAB_TABLE);
        
        labs.add(lab);
        labTable.refresh();
        labList.add(lab);
    }
    
    public void removeLab(Lab lab){
        AppGUIModule gui = app.getGUIModule();
        TableView<Lecture> labTable = (TableView<Lecture>) gui.getGUINode(M_LAB_TABLE);
        
        labs.remove(lab);
        labTable.refresh();
        labList.remove(lab);
    }
    
    public void addSchedule(Schedule sch){
        AppGUIModule gui = app.getGUIModule();
        TableView<Schedule> schTable = (TableView<Schedule>) gui.getGUINode(SCH_SI_TABLE);
        
        schs.add(sch);
        schTable.refresh();
        schList.add(sch);
    }
    
    public void removeSchedule(Schedule sch){
        AppGUIModule gui = app.getGUIModule();
        TableView<Schedule> schTable = (TableView<Schedule>) gui.getGUINode(SCH_SI_TABLE);
        
        schs.remove(sch);
        schTable.refresh();
        schList.remove(sch);
    }
    
    public ArrayList getLecArraylist(){
        return lecList;
    }
    
    public ArrayList getRecArraylist(){
        return recList;
    }
    
    public ArrayList getLabArraylist(){
        return labList;
    }
    
    public ArrayList getSchArraylist(){
        return schList;
    }
            
    
    public Iterator<TimeSlot> officeHoursIterator() {
        return officeHours.iterator();
    }

    public Iterator<TeachingAssistantPrototype> teachingAssistantsIterator() {
        return new AllTAsIterator();
    }
    
    private class AllTAsIterator implements Iterator {
        Iterator gradIt = allTAs.get(TAType.Graduate).iterator();
        Iterator undergradIt = allTAs.get(TAType.Undergraduate).iterator();

        public AllTAsIterator() {}
        
        @Override
        public boolean hasNext() {
            if (gradIt.hasNext() || undergradIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (gradIt.hasNext())
                return gradIt.next();
            else if (undergradIt.hasNext())
                return undergradIt.next();
            else
                return null;
        }
    }
    
    public ArrayList getOfficeHoursHold(){
        return officeHoursHold;
    }
    
    /*public String getSubject(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox subjectBox =(ComboBox) gui.getGUINode(S_SUBJECT_COMBOBOX);
        return (String)subjectBox.getSelectionModel().getSelectedItem();
                //getValue().toString();
    }*/
        
}