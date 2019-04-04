package csg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CourseSiteGeneratorApp;
import static csg.CourseSiteGeneratorPropertyType.SCH_END_TIME;
import static csg.CourseSiteGeneratorPropertyType.SCH_START_TIME;
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
import static csg.CourseSiteGeneratorPropertyType.S_SCHEDULE_CHECK_BOX;
import static csg.CourseSiteGeneratorPropertyType.S_SUBJECT_COMBOBOX;
import static csg.CourseSiteGeneratorPropertyType.S_SYLLABUS_CHECK_BOX;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.Schedule;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import static djf.AppPropertyType.APP_PATH_EXPORT;
import static djf.AppPropertyType.S_BANNER_TITLE_TEXT_FIELD;
import djf.modules.AppGUIModule;
import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;
import properties_manager.PropertiesManager;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class CourseSiteGeneratorFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CourseSiteGeneratorApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    //page
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_LOGOS = "logos";
    static final String JSON_FAVICON = "favicon";
    static final String JSON_NAVBAR = "navbar";
    static final String JSON_BOTTOM_LEFT = "bottom_left";
    static final String JSON_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_HREF = "href";
    static final String JSON_SRC = "src";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_LINK = "link";
    static final String JSON_ROOM = "room";
    static final String JSON_HOURS = "hours";
    static final String JSON_PHOTO = "photo";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_PAGES = "pages";
    static final String JSON_DES = "description";
    static final String JSON_TOP = "topics";
    static final String JSON_PRE = "prerequisites";
    static final String JSON_OUT = "outcomes";
    static final String JSON_TEXT = "textbooks";
    static final String JSON_GRADED = "gradedComponent";
    static final String JSON_GRADING = "gradingNote";
    static final String JSON_ACA = "academicDishonesty";
    static final String JSON_SPE = "specialAssistance";
    static final String JSON_SECTION = "section";
    static final String JSON_LEC_DAYS = "days";
    static final String JSON_TA1 ="ta_1";
    static final String JSON_TA2 ="ta_2";
    static final String JSON_REC_DAYS ="day_time";
    static final String JSON_LEC = "lectures";
    static final String JSON_REC = "recitations";
    static final String JSON_LAB = "labs";
    static final String JSON_LOCATION = "location";
    static final String JSON_START_MONTH = "startingMondayMonth";
    static final String JSON_START_DAY = "startingMondayDay";
    static final String JSON_END_MONTH = "endingFridayMonth";
    static final String JSON_END_DAY = "endingFridayDay";
    static final String JSON_MONTH = "month";
    static final String JSON_SCH_TOPIC = "topic";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_LECTURES = "lecture";
    static final String JSON_REFERENCE = "references";
    static final String JSON_RECITATION = "recitation";
    static final String JSON_HWS = "hws";
    static final String JSON_CRI = "criteria";
    static final String JSON_EX_LECTURES = "lectures";
    static final String JSON_EX_RECITATION = "recitations";
     
    
    

    public CourseSiteGeneratorFiles(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        AppGUIModule gui = app.getGUIModule();
        dataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        //
        
        String subject = json.getString(JSON_SUBJECT);
        String number = json.getString(JSON_NUMBER);
        Integer year = Integer.parseInt(json.getString(JSON_YEAR));
        String semester = json.getString(JSON_SEMESTER);
        String title = json.getString(JSON_TITLE);
        
        JsonObject instructor = json.getJsonObject(JSON_INSTRUCTOR);
        String iName = instructor.getString(JSON_NAME);
        String iRoom = instructor.getString(JSON_ROOM);
        String iLink = instructor.getString(JSON_LINK);
        String iEmail = instructor.getString(JSON_EMAIL);
        String iHours = instructor.getString(JSON_HOURS);
        
        String des = json.getString(JSON_DES);
        String top = json.getString(JSON_TOP);
        String pre = json.getString(JSON_PRE);
        String out = json.getString(JSON_OUT);
        String text = json.getString(JSON_TEXT);
        String graded = json.getString(JSON_GRADED);
        String grading = json.getString(JSON_GRADING);
        String aca = json.getString(JSON_ACA);
        String spe = json.getString(JSON_SPE);
       
        
        dataManager.initSubject(subject);
        dataManager.initNumber(number);
        dataManager.initYear(year);
        dataManager.initSemester(semester);
        dataManager.initTitle(title);
        
        dataManager.initInstructorName(iName);
        dataManager.initInstructorLink(iLink);
        dataManager.initInstructorRoom(iRoom);
        dataManager.initInstructorEmail(iEmail);
        dataManager.initInstructorHours(iHours);
        
        dataManager.initDescription(des);
        dataManager.initTopics(top);
        dataManager.initPrerequisites(pre);
        dataManager.initOutcomes(out);
        dataManager.initTextbooks(text);
        dataManager.initGraded(graded);
        dataManager.initGrading(grading);
        dataManager.initAca(aca);
        dataManager.initSpe(spe);
        
        
        //load checkbox
        CheckBox home = (CheckBox) gui.getGUINode(S_HOME_CHECK_BOX);
        CheckBox syllabus = (CheckBox) gui.getGUINode(S_SYLLABUS_CHECK_BOX);
        CheckBox schedule = (CheckBox) gui.getGUINode(S_SCHEDULE_CHECK_BOX);
        CheckBox hws = (CheckBox) gui.getGUINode(S_HWS_CHECK_BOX);
        
        JsonArray jsonPagesArray = json.getJsonArray(JSON_PAGES);
        for(int i = 0; i < jsonPagesArray.size(); i++){
            JsonObject jsonCB = jsonPagesArray.getJsonObject(i);
            if(jsonCB.getString(JSON_NAME).equals("Home"))
                home.setSelected(true);
            if(jsonCB.getString(JSON_NAME).equals("Syllabus"))
                syllabus.setSelected(true);
            if(jsonCB.getString(JSON_NAME).equals("Schedule"))
                schedule.setSelected(true);
            if(jsonCB.getString(JSON_NAME).equals("HWs"))
                hws.setSelected(true);
        }
        
        JsonArray jsonLecArray = json.getJsonArray(JSON_LEC);
        for(int i = 0; i < jsonLecArray.size(); i++){
            JsonObject jsonLec = jsonLecArray.getJsonObject(i);
            Lecture lec = new Lecture();
            lec.setSection(jsonLec.getString(JSON_SECTION));
            lec.setDays(jsonLec.getString(JSON_LEC_DAYS));
            lec.setTime(jsonLec.getString(JSON_TIME));
            lec.setRoom(jsonLec.getString(JSON_ROOM));
            dataManager.addLecture(lec);
        }
        
         JsonArray jsonRecArray = json.getJsonArray(JSON_REC);
        for(int i = 0; i < jsonRecArray.size(); i++){
            JsonObject jsonRec = jsonRecArray.getJsonObject(i);
            Recitation rec = new Recitation();
            String a = jsonRec.getString(JSON_SECTION);
            String b = jsonRec.getString(JSON_REC_DAYS);
            rec.setSection(jsonRec.getString(JSON_SECTION));
            
            //System.out.println(jsonRec.getString(JSON_REC_DAYS));
            rec.setDays(jsonRec.getString(JSON_REC_DAYS));
            rec.setRoom(jsonRec.getString(JSON_LOCATION));
            rec.setTA1(jsonRec.getString(JSON_TA1));
            rec.setTA2(jsonRec.getString(JSON_TA2));
            dataManager.addRecitation(rec);
        }
        
         JsonArray jsonLabArray = json.getJsonArray(JSON_LAB);
        for(int i = 0; i < jsonLabArray.size(); i++){
            JsonObject jsonLab = jsonLabArray.getJsonObject(i);
            Lab lab = new Lab();
            lab.setSection(jsonLab.getString(JSON_SECTION));
            lab.setDays(jsonLab.getString(JSON_REC_DAYS));
            lab.setRoom(jsonLab.getString(JSON_LOCATION));
            lab.setTA1(jsonLab.getString(JSON_TA1));
            lab.setTA2(jsonLab.getString(JSON_TA2));
            dataManager.addLab(lab);
        }
       
        
         JsonArray jsonHolidayArray = json.getJsonArray(JSON_HOLIDAYS);
         for(int i = 0; i < jsonHolidayArray.size(); i++){
            JsonObject jsonHoliday = jsonHolidayArray.getJsonObject(i);
            String month = jsonHoliday.getString(JSON_MONTH);
            String day = jsonHoliday.getString(JSON_DAY);
            String title1 = jsonHoliday.getString(JSON_TITLE);
            String link = jsonHoliday.getString(JSON_LINK);
            String time = json.getString(JSON_YEAR) + "-" + month + "-" + day;
            Schedule sch = new Schedule("holidays", time, title1, "no topic", link);
            dataManager.addSchedule(sch);
         }
         
         JsonArray jsonLectureArray = json.getJsonArray(JSON_LECTURES);
         for(int i = 0; i < jsonLectureArray.size(); i++){
            JsonObject jsonLecture = jsonLectureArray.getJsonObject(i);
            String month = jsonLecture.getString(JSON_MONTH);
            String day = jsonLecture.getString(JSON_DAY);
            String title1 = jsonLecture.getString(JSON_TITLE);
            String topic = jsonLecture.getString(JSON_SCH_TOPIC);
            String link = jsonLecture.getString(JSON_LINK);
            String time = json.getString(JSON_YEAR) + "-" + month + "-" + day;
            Schedule sch = new Schedule("lectures", time, title1, topic, link);
            dataManager.addSchedule(sch);
         }
         
         JsonArray jsonReferenceArray = json.getJsonArray(JSON_REFERENCE);
         for(int i = 0; i < jsonReferenceArray.size(); i++){
            JsonObject jsonReference = jsonReferenceArray.getJsonObject(i);
            String month = jsonReference.getString(JSON_MONTH);
            String day = jsonReference.getString(JSON_DAY);
            String title1 = jsonReference.getString(JSON_TITLE);
            String topic = jsonReference.getString(JSON_SCH_TOPIC);
            String link = jsonReference.getString(JSON_LINK);
            String time = json.getString(JSON_YEAR) + "-" + month + "-" + day;
            Schedule sch = new Schedule("references", time, title1, topic, link);
            dataManager.addSchedule(sch);
         }
         
         JsonArray jsonRecitationArray = json.getJsonArray(JSON_RECITATION);
         for(int i = 0; i < jsonHolidayArray.size(); i++){
            JsonObject jsonRecitation = jsonRecitationArray.getJsonObject(i);
            String month = jsonRecitation.getString(JSON_MONTH);
            String day = jsonRecitation.getString(JSON_DAY);
            String title1 = jsonRecitation.getString(JSON_TITLE);
            String topic = jsonRecitation.getString(JSON_SCH_TOPIC);
            String link = jsonRecitation.getString(JSON_LINK);
            String time = json.getString(JSON_YEAR) + "-" + month + "-" + day;
            Schedule sch = new Schedule("recitations", time, title1, topic, link);
            dataManager.addSchedule(sch);
         }
        
         JsonArray jsonHWsArray = json.getJsonArray(JSON_HWS);
         for(int i = 0; i < jsonHWsArray.size(); i++){
            JsonObject jsonHWs = jsonHWsArray.getJsonObject(i);
            String month = jsonHWs.getString(JSON_MONTH);
            String day = jsonHWs.getString(JSON_DAY);
            String title1 = jsonHWs.getString(JSON_TITLE);
            String topic = jsonHWs.getString(JSON_SCH_TOPIC);
            String link = jsonHWs.getString(JSON_LINK);
            String time = json.getString(JSON_YEAR) + "-" + month + "-" + day;
            Schedule sch = new Schedule("hws", time, title1, topic, link);
            dataManager.addSchedule(sch);
         }

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
        }
    }
    
    private void loadTAs(CourseSiteGeneratorData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /*private JsonArray loadJSONTextArea(String text)throws IOException{
        //Json Reader jsonReader = Json.
        
    }*/
    
    public void saveComboBox(AppDataComponent data, String filePath)throws IOException{
        
        CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        AppGUIModule gui = app.getGUIModule();
        ComboBox subject =(ComboBox) gui.getGUINode(S_SUBJECT_COMBOBOX);
        ObservableList<String> subjectList = subject.getItems();
        JsonArrayBuilder subjectArrayBuilder = Json.createArrayBuilder();
        Iterator<String> subjectIterator = subjectList.iterator();
        
        while(subjectIterator.hasNext()){
            String subjectTemp = subjectIterator.next();
            subjectArrayBuilder.add(subjectTemp);
        }
        JsonArray subjectArray = subjectArrayBuilder.build();
        
        JsonObject comboBoxSave = Json.createObjectBuilder()
                .add(JSON_SUBJECT, subjectArray)
                .build();
        
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(comboBoxSave);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(comboBoxSave);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        AppGUIModule gui = app.getGUIModule();
        
        //BUILD THE SITE PAGE
        
        //BUILD LOGO OBJECT
        JsonObject faviconPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/images/Favicon.png" ).build();
        
       
        JsonObject navbarPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/images/Navbar.png" ).build();
        
        
        JsonObject lfPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/images/Left_Footer.png" ).build();
       
        
        JsonObject rfPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/images/Right_Footer.png" ).build();
       
        
        //BUILD LOGO
        JsonObject logo = Json.createObjectBuilder()
                .add(JSON_FAVICON, faviconPath)
                .add(JSON_NAVBAR, navbarPath)
                .add(JSON_BOTTOM_LEFT, lfPath)
                .add(JSON_BOTTOM_RIGHT, rfPath).build();
        
        //BUILD 
        //BUILD INSTRUCTOR
        
        //JsonReader jsonReader = Json.createReader(((TextArea)gui.getGUINode(S_INSTRUCTOR_OH)).getText());
        JsonObject instructor = Json.createObjectBuilder()
                .add(JSON_NAME, ((TextField)gui.getGUINode(S_INSTRUCTOR_NAME)).getText())
                .add(JSON_LINK, ((TextField)gui.getGUINode(S_INSTRUCTOR_HP)).getText())
                .add(JSON_EMAIL, ((TextField)gui.getGUINode(S_INSTRUCTOR_EMAIL)).getText())
                .add(JSON_ROOM, ((TextField)gui.getGUINode(S_INSTRUCTOR_ROOM)).getText())
                .add(JSON_PHOTO, "./images/RichardMcKenna.jpg")
                .add(JSON_HOURS, ((TextArea)gui.getGUINode(S_INSTRUCTOR_OH)).getText())
                .build();
        
        //BUILD PAGES
        CheckBox home = (CheckBox) gui.getGUINode(S_HOME_CHECK_BOX);
        CheckBox syllabus = (CheckBox) gui.getGUINode(S_SYLLABUS_CHECK_BOX);
        CheckBox schedule = (CheckBox) gui.getGUINode(S_SCHEDULE_CHECK_BOX);
        CheckBox hws = (CheckBox) gui.getGUINode(S_HWS_CHECK_BOX);
        
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        if(home.isSelected()){
            JsonObject homeCB = Json.createObjectBuilder()
                    .add(JSON_NAME, "Home")
                    .add(JSON_LINK, "index.html").build();
                    pagesArrayBuilder.add(homeCB);
        }
        if(syllabus.isSelected()){
            JsonObject syllabusCb = Json.createObjectBuilder()
                    .add(JSON_NAME, "Syllabus")
                    .add(JSON_LINK, "syllabus.html").build();
                    pagesArrayBuilder.add(syllabusCb);
        }
        if(schedule.isSelected()){
            JsonObject scheduleCb = Json.createObjectBuilder()
                    .add(JSON_NAME, "Schedule")
                    .add(JSON_LINK, "schedule.html").build();
                    pagesArrayBuilder.add(scheduleCb);
        }
        if(hws.isSelected()){
            JsonObject hwsCb = Json.createObjectBuilder()
                    .add(JSON_NAME, "HWs")
                    .add(JSON_LINK, "hws.html").build();
                    pagesArrayBuilder.add(hwsCb);
        }
        
        //BUILD SECTION PAGE
        
        JsonArrayBuilder lecArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder labArrayBuilder = Json.createArrayBuilder();
        ArrayList<Lecture> lecArrayList = dataManager.getLecArraylist();
        ArrayList<Recitation> recArrayList = dataManager.getRecArraylist();
        ArrayList<Lab> labArrayList = dataManager.getLabArraylist();
        for(int i = 0; i < lecArrayList.size(); i++){
            Lecture lec = lecArrayList.get(i);
            JsonObject lecObject = Json.createObjectBuilder()
                    .add(JSON_SECTION, lec.getSection())
                    .add(JSON_LEC_DAYS, lec.getDays())
                    .add(JSON_TIME, lec.getTime())
                    .add(JSON_ROOM, lec.getRoom()).build();
            lecArrayBuilder.add(lecObject);
        }
        for(int i = 0; i < recArrayList.size(); i++){
            Recitation rec = recArrayList.get(i);
            JsonObject recObject = Json.createObjectBuilder()
                    .add(JSON_SECTION, rec.getSection())
                    .add(JSON_REC_DAYS, rec.getDays())
                    .add(JSON_LOCATION, rec.getRoom())
                    .add(JSON_TA1, rec.getTA1())
                    .add(JSON_TA2, rec.getTA2()).build();
            recArrayBuilder.add(recObject);
        }
        for(int i = 0; i < labArrayList.size(); i++){
            Lab lab = labArrayList.get(i);
            JsonObject labObject = Json.createObjectBuilder()
                    .add(JSON_SECTION, lab.getSection())
                    .add(JSON_REC_DAYS, lab.getDays())
                    .add(JSON_LOCATION, lab.getRoom())
                    .add(JSON_TA1, lab.getTA1())
                    .add(JSON_TA2, lab.getTA2()).build();
            labArrayBuilder.add(labObject);
        }
        JsonArray lecArray = lecArrayBuilder.build();
        JsonArray recArray = recArrayBuilder.build();
        JsonArray labArray = labArrayBuilder.build();
        
        //SCHEDULE PAGE
        String start = ((DatePicker)gui.getGUINode(SCH_START_TIME)).getValue() + "";
        String end = ((DatePicker)gui.getGUINode(SCH_END_TIME)).getValue() + "";
        gui.getGUINode(SCH_END_TIME);
        String startMonth = "";
        String startDay = "";
        String endMonth = "";
        String endDay = "";
        System.out.println(start + end);
        System.out.println(start != null);
        if(start != null && end != null){
        String[] startT = start.split("-");
        String[] endT = end.split("-");
        startMonth = startT[1];
        startDay = startT[2];
        endMonth = endT[1];
        endDay = endT[2];
        }
        
        JsonArrayBuilder holidayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lectureBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referenceBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsBuilder = Json.createArrayBuilder();
        ArrayList<Schedule> schArrayList = dataManager.getSchArraylist();
        for(int i = 0; i < schArrayList.size(); i++){
            Schedule sch = schArrayList.get(i);
            if(sch.getType().equals( "holidays")){
                String[] s = sch.getDays().split("-");
            JsonObject holidayObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, s[1])
                    .add(JSON_DAY, s[2])
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_LINK, sch.getLink()).build();
            holidayBuilder.add(holidayObject);
        }
            else if(sch.getType().equals("lectures")){
                String[] s = sch.getDays().split("-");
            JsonObject lectureObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, s[1])
                    .add(JSON_DAY, s[2])
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            lectureBuilder.add(lectureObject);
            }
            else if(sch.getType().equals("references")){
                 String[] s = sch.getDays().split("-");
            JsonObject referenceObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, s[1])
                    .add(JSON_DAY, s[2])
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            referenceBuilder.add(referenceObject);
            }
            else if(sch.getType().equals("recitations")){
                 String[] s = sch.getDays().split("-");
            JsonObject recitationObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, s[1])
                    .add(JSON_DAY, s[2])
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            recitationBuilder.add(recitationObject);
            }
            else if(sch.getType().equals("hws")){
                 String[] s = sch.getDays().split("-");
            JsonObject hwsObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, s[1])
                    .add(JSON_DAY, s[2])
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            hwsBuilder.add(hwsObject);
            }
        }
            JsonArray holidayArray = holidayBuilder.build();
            JsonArray lectureArray = lectureBuilder.build();
            JsonArray referenceArray = referenceBuilder.build();
            JsonArray recitationArray = recitationBuilder.build();
            JsonArray hwsArray = hwsBuilder.build();
            
        
        
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_SUBJECT, dataManager.getSubject())
                .add(JSON_NUMBER, dataManager.getNumber())
                .add(JSON_SEMESTER, dataManager.getSemester())
                .add(JSON_YEAR, "" + dataManager.getYear())
                .add(JSON_TITLE, ((TextField)gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD)).getText())
                .add(JSON_LOGOS, logo)
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_PAGES, pagesArrayBuilder)
                .add(JSON_DES, ((TextArea)gui.getGUINode(SY_DES_TEXT_AREA)).getText())
                .add(JSON_TOP, ((TextArea)gui.getGUINode(SY_TOP_TEXT_AREA)).getText())
                .add(JSON_PRE, ((TextArea)gui.getGUINode(SY_PRE_TEXT_AREA)).getText())
                .add(JSON_OUT, ((TextArea)gui.getGUINode(SY_OUT_TEXT_AREA)).getText())
                .add(JSON_TEXT, ((TextArea)gui.getGUINode(SY_TEXT_TEXT_AREA)).getText())
                .add(JSON_GRADED, ((TextArea)gui.getGUINode(SY_GRADED_TEXT_AREA)).getText())
                .add(JSON_GRADING, ((TextArea)gui.getGUINode(SY_GRADING_TEXT_AREA)).getText())
                .add(JSON_ACA, ((TextArea)gui.getGUINode(SY_ACA_TEXT_AREA)).getText())
                .add(JSON_SPE, ((TextArea)gui.getGUINode(SY_SPE_TEXT_AREA)).getText())
                .add(JSON_LEC, lecArray)
                .add(JSON_REC, recArray)
                .add(JSON_LAB, labArray)
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .add(JSON_START_MONTH, startMonth)
                .add(JSON_START_DAY, startDay)
                .add(JSON_END_MONTH, endMonth)
                .add(JSON_END_DAY, endDay)
                .add(JSON_HOLIDAYS, holidayArray)
                .add(JSON_LECTURES, lectureArray)
                .add(JSON_REFERENCE, referenceArray)
                .add(JSON_RECITATION, recitationArray)
                .add(JSON_HWS, hwsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
        CourseSiteGeneratorData dataManager = (CourseSiteGeneratorData)data;
        AppGUIModule gui = app.getGUIModule();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        
                String exportD = dataManager.getExportD();
                String[] parts= exportD.split("/");
                
                props.removeProperty(APP_PATH_EXPORT);
                props.addProperty(APP_PATH_EXPORT, exportD);
                //System.out.println(APP_PATH_EXPORT + exportD);
                
                File a = new File("./export/" + parts[2] + "/" + parts[3] + "hws.html");
                File b = new File("./export/" + parts[2] + "/" + parts[3] + "index.html");
                File c = new File("./export/" + parts[2] + "/" + parts[3] + "schedule.html");
                File d = new File("./export/" + parts[2] + "/" + parts[3] + "syllabus.html");
                
                
                
                FileUtils.deleteQuietly(a);
                FileUtils.deleteQuietly(b);
                FileUtils.deleteQuietly(c);
                FileUtils.deleteQuietly(d);
                
                boolean success = new File ( "./export/" + parts[2]).mkdir();
                boolean success2 = new File("./export/" + parts[2] + "/" + parts[3]).mkdir();
                File srcD = new File("./template");
                File expD = new File(exportD);
                FileUtils.copyDirectory(srcD, expD);
                
                String newOHFile = exportD + "/js/OfficeHoursData.json";
                new File(newOHFile).createNewFile();
                String newPageFile = exportD + "/js/PageData.json";
                new File(newPageFile).createNewFile();
                String newSDFile = exportD + "/js/ScheduleData.json";
                new File(newSDFile).createNewFile();
                String newSeFile = exportD + "/js/SectionsData.json";
                new File(newSeFile).createNewFile();
                String newSyFile = exportD + "/js/SyllabusData.json";
                new File(newSyFile).createNewFile();
                
                
                
                
        
        //SCHEDULE PAGE
        String start = ((DatePicker)gui.getGUINode(SCH_START_TIME)).getValue() + "";
        String end = ((DatePicker)gui.getGUINode(SCH_END_TIME)).getValue() + "";
        gui.getGUINode(SCH_END_TIME);
        String startMonth = "";
        String startDay = "";
        String endMonth = "";
        String endDay = "";
        System.out.println(start + end);
        System.out.println(start != null);
        if(start != null && end != null){
        String[] startT = start.split("-");
        String[] endT = end.split("-");
        startMonth = startT[1];
        startDay = startT[2];
        endMonth = endT[1];
        endDay = endT[2];
        }
        
        JsonArrayBuilder holidayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lectureBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referenceBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsBuilder = Json.createArrayBuilder();
        ArrayList<Schedule> schArrayList = dataManager.getSchArraylist();
        for(int i = 0; i < schArrayList.size(); i++){
            Schedule sch = schArrayList.get(i);
            if(sch.getType().equals( "holidays")){
                String[] s = sch.getDays().split("-");
                Integer l = Integer.parseInt(s[2]);
                Integer m = Integer.parseInt(s[1]);
            JsonObject holidayObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, m+"")
                    .add(JSON_DAY, l+"")
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_LINK, sch.getLink()).build();
            holidayBuilder.add(holidayObject);
        }
            else if(sch.getType().equals("lectures")){
                String[] s = sch.getDays().split("-");
                Integer l = Integer.parseInt(s[2]);
                Integer m = Integer.parseInt(s[1]);
            JsonObject lectureObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, m+"")
                    .add(JSON_DAY, l+"")
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            lectureBuilder.add(lectureObject);
            }
            else if(sch.getType().equals("references")){
                 String[] s = sch.getDays().split("-");
                  Integer l = Integer.parseInt(s[2]);
                Integer m = Integer.parseInt(s[1]);
            JsonObject referenceObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, m+"")
                    .add(JSON_DAY, l+"")
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            referenceBuilder.add(referenceObject);
            }
            else if(sch.getType().equals("recitations")){
                 String[] s = sch.getDays().split("-");
                  Integer l = Integer.parseInt(s[2]);
                Integer m = Integer.parseInt(s[1]);
            JsonObject recitationObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, m+"")
                    .add(JSON_DAY, l+"")
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink()).build();
            recitationBuilder.add(recitationObject);
            }
            else if(sch.getType().equals("hws")){
                 String[] s = sch.getDays().split("-");
                  Integer l = Integer.parseInt(s[2]);
                Integer m = Integer.parseInt(s[1]);
            JsonObject hwsObject = Json.createObjectBuilder()
                    .add(JSON_MONTH, m+"")
                    .add(JSON_DAY, l+"")
                    .add(JSON_TITLE, sch.getTitle())
                    .add(JSON_SCH_TOPIC, sch.getTopic())
                    .add(JSON_LINK, sch.getLink())
                    .add(JSON_TIME, "")
                    .add(JSON_CRI, "none").build();
            hwsBuilder.add(hwsObject);
            }
        }
            JsonArray holidayArray = holidayBuilder.build();
            JsonArray lectureArray = lectureBuilder.build();
            JsonArray referenceArray = referenceBuilder.build();
            JsonArray recitationArray = recitationBuilder.build();
            JsonArray hwsArray = hwsBuilder.build();
        
        JsonObject dataManagerSCH = Json.createObjectBuilder()
                .add(JSON_START_MONTH, startMonth)
                .add(JSON_START_DAY, startDay)
                .add(JSON_END_MONTH, endMonth)
                .add(JSON_END_DAY, endDay)
                .add(JSON_HOLIDAYS, holidayArray)
                .add(JSON_EX_LECTURES, lectureArray)
                .add(JSON_REFERENCE, referenceArray)
                .add(JSON_EX_RECITATION, recitationArray)
                .add(JSON_HWS, hwsArray)
		.build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties1 = new HashMap<>(1);
	properties1.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory1 = Json.createWriterFactory(properties1);
	StringWriter sw1 = new StringWriter();
	JsonWriter jsonWriter1 = writerFactory1.createWriter(sw1);
	jsonWriter1.writeObject(dataManagerSCH);
	jsonWriter1.close();

	// INIT THE WRITER
	OutputStream os1 = new FileOutputStream(newSDFile);
	JsonWriter jsonFileWriter1 = Json.createWriter(os1);
	jsonFileWriter1.writeObject(dataManagerSCH);
	String prettyPrinted1 = sw1.toString();
	PrintWriter pw1 = new PrintWriter(newSDFile);
	pw1.write(prettyPrinted1);
	pw1.close();
        
        //BUILD SECTION PAGE
        
        JsonArrayBuilder lecArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder labArrayBuilder = Json.createArrayBuilder();
        ArrayList<Lecture> lecArrayList = dataManager.getLecArraylist();
        ArrayList<Recitation> recArrayList = dataManager.getRecArraylist();
        ArrayList<Lab> labArrayList = dataManager.getLabArraylist();
        for(int i = 0; i < lecArrayList.size(); i++){
            Lecture lec = lecArrayList.get(i);
            JsonObject lecObject = Json.createObjectBuilder()
                    .add(JSON_SECTION, lec.getSection())
                    .add(JSON_LEC_DAYS, lec.getDays())
                    .add(JSON_TIME, lec.getTime())
                    .add(JSON_ROOM, lec.getRoom()).build();
            lecArrayBuilder.add(lecObject);
        }
        for(int i = 0; i < recArrayList.size(); i++){
            Recitation rec = recArrayList.get(i);
            JsonObject recObject = Json.createObjectBuilder()
                    .add(JSON_SECTION, rec.getSection())
                    .add(JSON_REC_DAYS, rec.getDays())
                    .add(JSON_LOCATION, rec.getRoom())
                    .add(JSON_TA1, rec.getTA1())
                    .add(JSON_TA2, rec.getTA2()).build();
            recArrayBuilder.add(recObject);
        }
        for(int i = 0; i < labArrayList.size(); i++){
            Lab lab = labArrayList.get(i);
            JsonObject labObject = Json.createObjectBuilder()
                    .add(JSON_SECTION, lab.getSection())
                    .add(JSON_REC_DAYS, lab.getDays())
                    .add(JSON_LOCATION, lab.getRoom())
                    .add(JSON_TA1, lab.getTA1())
                    .add(JSON_TA2, lab.getTA2()).build();
            labArrayBuilder.add(labObject);
        }
        JsonArray lecArray = lecArrayBuilder.build();
        JsonArray recArray = recArrayBuilder.build();
        JsonArray labArray = labArrayBuilder.build();
        
        JsonObject dataManagerMT = Json.createObjectBuilder()
                .add(JSON_LEC, lecArray)
                .add(JSON_REC, recArray)
                .add(JSON_LAB, labArray).build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties2 = new HashMap<>(1);
	properties2.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory2 = Json.createWriterFactory(properties2);
	StringWriter sw2 = new StringWriter();
	JsonWriter jsonWriter2 = writerFactory2.createWriter(sw2);
	jsonWriter2.writeObject(dataManagerMT);
	jsonWriter2.close();

	// INIT THE WRITER
	OutputStream os2 = new FileOutputStream(newSeFile);
	JsonWriter jsonFileWriter2 = Json.createWriter(os2);
	jsonFileWriter2.writeObject(dataManagerMT);
	String prettyPrinted2 = sw2.toString();
	PrintWriter pw2 = new PrintWriter(newSeFile);
	pw2.write(prettyPrinted2);
	pw2.close();
        
        //BUILD THE SITE PAGE
        
        //BUILD LOGO OBJECT
        JsonObject faviconPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "./images/SBUShieldFavicon.ico" ).build();
        
       
        JsonObject navbarPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.stonybrook.edu")
                .add(JSON_SRC, "./images/SBUDarkRedShieldLogo.png" ).build();
        
        
        JsonObject lfPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.cs.stonybrook.edu")
                .add(JSON_SRC, "./images/SBUWhiteShieldLogo.jpg" ).build();
       
        
        JsonObject rfPath =  Json.createObjectBuilder()
                .add(JSON_HREF, "http://www.cs.stonybrook.edu")
                .add(JSON_SRC, "./images/SBUCSLogo.png" ).build();
       
        
        //BUILD LOGO
        JsonObject logo = Json.createObjectBuilder()
                .add(JSON_FAVICON, faviconPath)
                .add(JSON_NAVBAR, navbarPath)
                .add(JSON_BOTTOM_LEFT, lfPath)
                .add(JSON_BOTTOM_RIGHT, rfPath).build();
        
        //BUILD 
        //BUILD INSTRUCTOR
        
        
        JsonReader jsonReader = Json.createReader(new StringReader(((TextArea)gui.getGUINode(S_INSTRUCTOR_OH)).getText()));
        JsonArray hours = jsonReader.readArray();
        
        JsonObject instructor = Json.createObjectBuilder()
                .add(JSON_NAME, ((TextField)gui.getGUINode(S_INSTRUCTOR_NAME)).getText())
                .add(JSON_LINK, ((TextField)gui.getGUINode(S_INSTRUCTOR_HP)).getText())
                .add(JSON_EMAIL, ((TextField)gui.getGUINode(S_INSTRUCTOR_EMAIL)).getText())
                .add(JSON_ROOM, ((TextField)gui.getGUINode(S_INSTRUCTOR_ROOM)).getText())
                .add(JSON_PHOTO, "./images/RichardMcKenna.jpg")
                .add(JSON_HOURS, hours)
                .build();
        
        //BUILD PAGES
        CheckBox home = (CheckBox) gui.getGUINode(S_HOME_CHECK_BOX);
        CheckBox syllabus = (CheckBox) gui.getGUINode(S_SYLLABUS_CHECK_BOX);
        CheckBox schedule = (CheckBox) gui.getGUINode(S_SCHEDULE_CHECK_BOX);
        CheckBox hws = (CheckBox) gui.getGUINode(S_HWS_CHECK_BOX);
        
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        if(home.isSelected()){
            JsonObject homeCB = Json.createObjectBuilder()
                    .add(JSON_NAME, "Home")
                    .add(JSON_LINK, "index.html").build();
                    pagesArrayBuilder.add(homeCB);
        }
        if(syllabus.isSelected()){
            JsonObject syllabusCb = Json.createObjectBuilder()
                    .add(JSON_NAME, "Syllabus")
                    .add(JSON_LINK, "syllabus.html").build();
                    pagesArrayBuilder.add(syllabusCb);
        }
        if(schedule.isSelected()){
            JsonObject scheduleCb = Json.createObjectBuilder()
                    .add(JSON_NAME, "Schedule")
                    .add(JSON_LINK, "schedule.html").build();
                    pagesArrayBuilder.add(scheduleCb);
        }
        if(hws.isSelected()){
            JsonObject hwsCb = Json.createObjectBuilder()
                    .add(JSON_NAME, "HWs")
                    .add(JSON_LINK, "hws.html").build();
                    pagesArrayBuilder.add(hwsCb);
        }
        JsonObject dataManagerPG = Json.createObjectBuilder()
        .add(JSON_SUBJECT, dataManager.getSubject())
                .add(JSON_NUMBER, dataManager.getNumber())
                .add(JSON_SEMESTER, dataManager.getSemester())
                .add(JSON_YEAR, "" + dataManager.getYear())
                .add(JSON_TITLE, ((TextField)gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD)).getText())
                .add(JSON_LOGOS, logo)
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_PAGES, pagesArrayBuilder).build();
        
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties3 = new HashMap<>(1);
	properties3.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory3 = Json.createWriterFactory(properties3);
	StringWriter sw3 = new StringWriter();
	JsonWriter jsonWriter3 = writerFactory3.createWriter(sw3);
	jsonWriter3.writeObject(dataManagerPG);
	jsonWriter3.close();

	// INIT THE WRITER
	OutputStream os3 = new FileOutputStream(newPageFile);
	JsonWriter jsonFileWriter3 = Json.createWriter(os3);
	jsonFileWriter3.writeObject(dataManagerPG);
	String prettyPrinted3 = sw3.toString();
	PrintWriter pw3 = new PrintWriter(newPageFile);
	pw3.write(prettyPrinted3);
	pw3.close();
        
        // NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
        
        
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        JsonObject dataManagerOH = Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray).build();
        
        //File officeHour = new File("officeHour.json");
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerOH);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(newOHFile);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerOH);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(newOHFile);
	pw.write(prettyPrinted);
	pw.close();
        
        JsonReader jsonReader1 = Json.createReader(new StringReader(((TextArea)gui.getGUINode(SY_TOP_TEXT_AREA)).getText()));
        JsonArray top = jsonReader1.readArray();
        JsonReader jsonReader2 = Json.createReader(new StringReader(((TextArea)gui.getGUINode(SY_OUT_TEXT_AREA)).getText()));
        JsonArray out = jsonReader2.readArray();
        JsonReader jsonReader3 = Json.createReader(new StringReader(((TextArea)gui.getGUINode(SY_TEXT_TEXT_AREA)).getText()));
        JsonArray text = jsonReader3.readArray();
        JsonReader jsonReader4 = Json.createReader(new StringReader(((TextArea)gui.getGUINode(SY_GRADED_TEXT_AREA)).getText()));
        JsonArray graded = jsonReader4.readArray();
        
         JsonObject dataManagerSY = Json.createObjectBuilder()
                .add(JSON_DES, ((TextArea)gui.getGUINode(SY_DES_TEXT_AREA)).getText())
                .add(JSON_TOP, top)
                .add(JSON_PRE, ((TextArea)gui.getGUINode(SY_PRE_TEXT_AREA)).getText())
                .add(JSON_OUT, out)
                .add(JSON_TEXT, text)
                .add(JSON_GRADED, graded)
                .add(JSON_GRADING, ((TextArea)gui.getGUINode(SY_GRADING_TEXT_AREA)).getText())
                .add(JSON_ACA, ((TextArea)gui.getGUINode(SY_ACA_TEXT_AREA)).getText())
                .add(JSON_SPE, ((TextArea)gui.getGUINode(SY_SPE_TEXT_AREA)).getText()).build();
        
         // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties4 = new HashMap<>(1);
	properties4.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory4 = Json.createWriterFactory(properties4);
	StringWriter sw4 = new StringWriter();
	JsonWriter jsonWriter4 = writerFactory4.createWriter(sw4);
	jsonWriter4.writeObject(dataManagerSY);
	jsonWriter4.close();

	// INIT THE WRITER
	OutputStream os4 = new FileOutputStream(newSyFile);
	JsonWriter jsonFileWriter4 = Json.createWriter(os4);
	jsonFileWriter4.writeObject(dataManagerSY);
	String prettyPrinted4 = sw4.toString();
	PrintWriter pw4 = new PrintWriter(newSyFile);
	pw4.write(prettyPrinted4);
	pw4.close();
        
    }
}