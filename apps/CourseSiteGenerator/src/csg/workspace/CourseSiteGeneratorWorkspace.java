package csg.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorPropertyType;
import static csg.CourseSiteGeneratorPropertyType.*;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.Schedule;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.transactions.AddSubjectComboBoxData_Transaction;
import csg.transactions.EditText_Transaction;
import csg.workspace.controllers.OfficeHoursController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CourseSiteGeneratorFoolproofDesign;
import static csg.workspace.style.OHStyle.*;
import static djf.AppPropertyType.APP_PATH_EXPORT;
import static djf.AppPropertyType.SCH_PROGRAM_DATA_TYPE;
import static djf.AppPropertyType.S_BANNER_TITLE_TEXT_FIELD;
import static djf.AppPropertyType.S_EXPORT_TEXT_LABEL;
import static djf.AppPropertyType.S_PROGRAM_DATA_NUMBER;
import static djf.AppPropertyType.S_PROGRAM_DATA_SUBJECT;
import static djf.AppPropertyType.S_PUBLIC_HTML_LABEL;
import static djf.AppPropertyType.S_STYLE_FAV_IMAGE;
import static djf.AppPropertyType.S_STYLE_LEFT_IMAGE;
import static djf.AppPropertyType.S_STYLE_NAV_IMAGE;
import static djf.AppPropertyType.S_STYLE_RIGHT_IMAGE;
import static djf.AppTemplate.PATH_IMAGES;
import static djf.AppTemplate.PATH_PROGRAMDATA;
import djf.modules.AppLanguageModule;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;



public class CourseSiteGeneratorWorkspace extends AppWorkspaceComponent {

    public CourseSiteGeneratorWorkspace(CourseSiteGeneratorApp app) throws IOException {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CourseSiteGeneratorApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() throws IOException {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        AppGUIModule gui = app.getGUIModule();
        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        AppLanguageModule languageSettings = app.getLanguageModule();
        
        //The Tabs and TabPane
        
        TabPane layout = new TabPane();
        //props.addProperty(S_SITE_TAB, "Site");
        Tab site = new Tab();
        languageSettings.addLabeledControlProperty(CSG_SITE_TAB, site.textProperty());
        props.getProperty(props.getProperty(CSG_SITE_TAB));
        //asite.setText(CSG_SITE_TAB);
        Tab syllabus = new Tab();
        languageSettings.addLabeledControlProperty(CSG_SYLLABUS_TAB, syllabus.textProperty());
        Tab meetingTimes = new Tab(props.getProperty(CSG_MT_TAB));
        Tab officeHours = new Tab(props.getProperty(CSG_OH_TAB));
        Tab schedule = new Tab(props.getProperty(CSG_SCHEDULE_TAB));
        languageSettings.addLabeledControlProperty(CSG_MT_TAB, meetingTimes.textProperty());
        languageSettings.addLabeledControlProperty(CSG_OH_TAB, officeHours.textProperty());
        languageSettings.addLabeledControlProperty(CSG_SCHEDULE_TAB, schedule.textProperty());
        layout.getTabs().add(site);
        layout.getTabs().add(syllabus);
        layout.getTabs().add(meetingTimes);
        layout.getTabs().add(officeHours);
        layout.getTabs().add(schedule);
        
        
        
        //Init the Site Tab
        ObservableList<String> subjectList = readData(PATH_PROGRAMDATA + props.getProperty(S_PROGRAM_DATA_SUBJECT));// "/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/programdata/Subject");
        ObservableList<String> numberList = readData(PATH_PROGRAMDATA +  props.getProperty(S_PROGRAM_DATA_NUMBER));//"/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/programdata/Number");
        ObservableList<String> semesterList = FXCollections.observableArrayList("Fall", "Spring", "Winter", "Summer");
        ObservableList<Integer> yearList = FXCollections.observableArrayList();
        //System.out.println(FILE_PROTOCOL + PATH_PROGRAMDATA +  props.getProperty(S_PROGRAM_DATA_SUBJECT));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearList.add(year);
        yearList.add(year + 1);
        
        
        //observableList.add("a");
        //VBox sitePane = ohBuilder.buildVBox(S_SITE_PANE, null, CLASS_S_PANE, ENABLED);
        GridPane bannerPane = ohBuilder.buildGridPane(S_BANNER_GRIDPANE, null, CLASS_S_PANE, ENABLED);
        ComboBox subjectBox = ohBuilder.buildComboBox(S_SUBJECT_COMBOBOX, bannerPane, 5,3,1,1, CLASS_S_COMBOBOX, ENABLED, "","");
        ComboBox numberBox = ohBuilder.buildComboBox(S_NUMBER_COMBOBOX, bannerPane, 15,3,1,1, CLASS_S_COMBOBOX, ENABLED, "","");
        ComboBox semesterBox = ohBuilder.buildComboBox(S_SEMESTER_COMBOBOX, bannerPane, 5,6,1,1, CLASS_S_COMBOBOX, ENABLED, "","");
        ComboBox yearBox = ohBuilder.buildComboBox(S_YEAR_COMBOBOX, bannerPane, 15,6,1,1, CLASS_S_COMBOBOX, ENABLED, "","");
        subjectBox.setItems(subjectList);
        numberBox.setItems(numberList);
        semesterBox.setItems(semesterList);
        yearBox.setItems(yearList);
        
        subjectBox.setEditable(ENABLED);
        numberBox.setEditable(ENABLED);
        //semesterBox.setEditable(ENABLED);
        //yearBox.setEditable(ENABLED);
        TextField titleTF = new TextField();
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_BANNER_LABEL, bannerPane, CLASS_S_PANE_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_SUBJECT_LABEL, bannerPane, 2, 2, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_NUMBER_LABEL, bannerPane, 12, 2, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_SEMESTER_LABEL, bannerPane, 2, 5, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_YEAR_LABEL, bannerPane, 12, 5, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_TITLE_LABEL, bannerPane, 2, 8, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_EXPORT_LABEL, bannerPane, 2, 11, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_EXPORT_TEXT_LABEL, bannerPane, 5, 11, 2, 2, CLASS_S_BANNER_LABEL, ENABLED);
        
        String subjectName = (String)subjectBox.getSelectionModel().getSelectedItem() + "_";
        String numberName = (String)numberBox.getSelectionModel().getSelectedItem()+ "_";
        String semesterName = (String)semesterBox.getSelectionModel().getSelectedItem()+ "_";
        String yearName = (Integer)yearBox.getSelectionModel().getSelectedItem() + "";
        String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName +props.getProperty(S_PUBLIC_HTML_LABEL) ;
        Label exportText = new Label(exportName);
        gui.addGUINode(S_EXPORT_TEXT_LABEL, exportText);
        gui.addGUINode(S_BANNER_TITLE_TEXT_FIELD, titleTF);
        
        
        bannerPane.add(exportText, 5, 11);

        bannerPane.setPadding(new Insets(10, 10, 10, 10));
        bannerPane.setVgap(8);
        bannerPane.setHgap(10);
        
        //bannerPane.add(subjectBox, 5, 3);
        //bannerPane.add(numberBox, 15, 3);
        //bannerPane.add(semesterBox, 5, 6);
        //bannerPane.add(yearBox, 15, 6);
        bannerPane.add(titleTF, 5, 9);
        
        GridPane pagesPane = ohBuilder.buildGridPane(S_PAGES_GRIDPANE, null, CLASS_S_PAGES_PANE, ENABLED);
        CheckBox homeCB = new CheckBox("Home"); //ohBuilder.buildCheckBox(S_HOME_CHECK_BOX, pagesPane, CLASS_S_CHECKBOX, ENABLED);
        CheckBox syllabusCB = new CheckBox("Syllabus");//ohBuilder.buildCheckBox(S_SYLLABUS_CHECK_BOX, pagesPane, CLASS_S_CHECKBOX, ENABLED);
        CheckBox scheduleCB = new CheckBox("Schedule");//ohBuilder.buildCheckBox(S_SCHEDULE_CHECK_BOX, pagesPane, CLASS_S_CHECKBOX, ENABLED);
        CheckBox hwsCB = new CheckBox("HWs");//ohBuilder.buildCheckBox(S_HWS_CHECK_BOX, pagesPane, CLASS_S_CHECKBOX, ENABLED);
        //pagesPane.setPadding(new Insets(10, 10, 10, 10));
        
        
        gui.addGUINode(S_HOME_CHECK_BOX, homeCB);
        gui.addGUINode(S_SYLLABUS_CHECK_BOX, syllabusCB);
        gui.addGUINode(S_SCHEDULE_CHECK_BOX, scheduleCB);
        gui.addGUINode(S_HWS_CHECK_BOX, hwsCB);
        
        languageSettings.addLabeledControlProperty(S_HOME_CHECK_BOX, homeCB.textProperty());
        languageSettings.addLabeledControlProperty(S_SYLLABUS_CHECK_BOX, syllabusCB.textProperty());
        languageSettings.addLabeledControlProperty(S_SCHEDULE_CHECK_BOX, scheduleCB.textProperty());
        languageSettings.addLabeledControlProperty(S_HWS_CHECK_BOX, hwsCB.textProperty());
        
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_PAGES_LABEL, pagesPane, CLASS_S_PANE_LABEL, ENABLED);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_HOME_LABEL, pagesPane, 11, 2, 1, 1, CLASS_S_PAGES_LABEL, ENABLED);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_SYLLABUS_LABEL, pagesPane, 21, 2, 2, 2, CLASS_S_PAGES_LABEL, ENABLED);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_SCHEDULE_LABEL, pagesPane, 31, 2, 2, 2, CLASS_S_PAGES_LABEL, ENABLED);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_HWS_LABEL, pagesPane, 41, 2, 2, 2, CLASS_S_PAGES_LABEL, ENABLED);
        pagesPane.setVgap(2);
        pagesPane.setHgap(10);
        
        pagesPane.add(homeCB, 10,2);
        pagesPane.add(syllabusCB, 20,2);
        pagesPane.add(scheduleCB, 30,2);
        pagesPane.add(hwsCB, 40,2);
        
        //pagesPane.add(homeCheckBox,7,1);
        GridPane stylePane = ohBuilder.buildGridPane(S_STYLE_GRIDPANE, null, CLASS_S_PANE, ENABLED);
        ObservableList<String> fontList = FXCollections.observableArrayList("sea_wolf.css", "course_homepage_layout.css");
        //fontList.
        ComboBox fontBox = ohBuilder.buildComboBox(S_FONT_COMBOBOX, stylePane, 2,15,2,2, CLASS_S_COMBOBOX, ENABLED, "","");
        fontBox.setItems(fontList);
        Button styleB1 = ohBuilder.buildTextButton(S_STYLE_FAV_BUTTON, stylePane, 1, 3, 1, 1, CLASS_S_BUTTON, ENABLED);
        Button styleB2 = ohBuilder.buildTextButton(S_STYLE_NAV_BUTTON, stylePane, 1, 6, 1, 1, CLASS_S_BUTTON, ENABLED);
        Button styleB3 = ohBuilder.buildTextButton(S_STYLE_LEFT_BUTTON, stylePane, 1, 9, 1, 1, CLASS_S_BUTTON, ENABLED);
        Button styleB4 = ohBuilder.buildTextButton(S_STYLE_RIGHT_BUTTON, stylePane, 1, 12, 1, 1, CLASS_S_BUTTON, ENABLED);
        //Label styleLabel = new Label(CourseSiteGeneratorPropertyType.S_STYLE_LABEL);
        
        Image favicon = new Image(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(S_STYLE_FAV_IMAGE), 50, 50, false, true);
        Image navbar = new Image(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(S_STYLE_NAV_IMAGE), 230, 50, false, true);
        Image leftFooter = new Image(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(S_STYLE_LEFT_IMAGE), 230, 50, false, true);
        Image rightFooter = new Image(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(S_STYLE_RIGHT_IMAGE), 230, 50, false, true);
        //System.out.println(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(S_STYLE_FAV_IMAGE));
        ImageView faviconView = new ImageView(favicon);
        ImageView navbarView = new ImageView(navbar);
        ImageView leftFooterView = new ImageView(leftFooter);
        ImageView rightFooterView = new ImageView(rightFooter);
        
        
        gui.addGUINode(S_STYLE_ONE_IMAGE_VIEW, faviconView);
        gui.addGUINode(S_STYLE_TWO_IMAGE_VIEW, navbarView);
        gui.addGUINode(S_STYLE_THREE_IMAGE_VIEW, leftFooterView);
        gui.addGUINode(S_STYLE_FOUR_IMAGE_VIEW, rightFooterView);
        faviconView.prefHeight(100);
        faviconView.prefHeight(200);
        stylePane.add(faviconView, 2, 3);
        stylePane.add(navbarView, 2, 6);
        stylePane.add(leftFooterView, 2, 9);
        stylePane.add(rightFooterView, 2, 12);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_STYLE_LABEL, stylePane, CLASS_S_PANE_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_FONT_LABEL, stylePane, 1, 15, 1, 1, CLASS_S_STYLE_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_NOTE_LABEL, stylePane, 1, 17, 1, 1, CLASS_S_STYLE_LABEL, ENABLED);
        
        //ohBuilder.build
        

        //stylePane.setPadding(new Insets(10, 10, 10, 10));
        //stylePane.setVgap(2);
        //stylePane.setHgap(10);
        
        //stylePane.add(styleB1, 1, 3);
        //stylePane.add(styleB2, 1, 6);
        //stylePane.add(styleB3, 1, 9);
        //stylePane.add(styleB4, 1, 12);
        //ohBuilder.buil2dTextButton(S_STYLE_BUTTON, stylePane, CLASS_S_BUTTON, ENABLED);
        //ohBuilder.buildTextButton(S_STYLE_BUTTON, stylePane, CLASS_S_BUTTON, ENABLED);
        //ohBuilder.buildTextButton(S_STYLE_BUTTON, stylePane, CLASS_S_BUTTON, ENABLED);
        //ohBuilder.buildTextButton(S_STYLE_BUTTON, stylePane, CLASS_S_BUTTON, ENABLED);
        GridPane instructorPane = ohBuilder.buildGridPane(S_INSTRUCTOR_GRIDPANE, null, CLASS_S_PANE, ENABLED);
        TextField instName = new TextField();
        TextField instRoom = new TextField();
        TextField instEmail = new TextField();
        TextField InstHP = new TextField();
        gui.addGUINode(S_INSTRUCTOR_NAME, instName);
        gui.addGUINode(S_INSTRUCTOR_ROOM, instRoom);
        gui.addGUINode(S_INSTRUCTOR_EMAIL, instEmail);
        gui.addGUINode(S_INSTRUCTOR_HP, InstHP);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_INSTRUCTOR_LABEL, instructorPane, CLASS_S_PANE_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_NAME_LABEL, instructorPane, 3, 2, 2, 2, CLASS_S_INSTRUCTOR_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_EMAIL_LABEL, instructorPane, 3, 11, 2, 2, CLASS_S_INSTRUCTOR_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_ROOM_LABEL, instructorPane, 18, 2, 2, 2, CLASS_S_INSTRUCTOR_LABEL, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_HOME_PAGE_LABEL, instructorPane, 18, 11, 2, 2, CLASS_S_INSTRUCTOR_LABEL, ENABLED);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_OFFICE_HOURS_LABEL, instructorPane, 2, 18, 2, 2, CLASS_S_INSTRUCTOR_LABEL, ENABLED);
        TextArea OHText = new TextArea();
         gui.addGUINode(S_INSTRUCTOR_OH, OHText);
        TitledPane OHTitle = new TitledPane();
        OHTitle.setContent(OHText);
        
         languageSettings.addLabeledControlProperty(S_OH_TITLE_PANE, OHTitle.textProperty());
        //Button OHExpand =  new Button("+");
        
        instructorPane.setPadding(new Insets(10, 10, 10, 10));
        instructorPane.setVgap(2);
        instructorPane.setHgap(10);
        
        //instructorPane.add(OHText, 3, 15);
        //instructorPane.add(OHExpand, 1, 18);
        instructorPane.add(OHTitle, 1, 19, 5, 1);
        instructorPane.add(instName, 5, 3);
        instructorPane.add(instRoom, 20, 3);
        instructorPane.add(instEmail, 5, 12);
        instructorPane.add(InstHP, 20, 12);
        
        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
        ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        HBox typeHeaderBox = ohBuilder.buildHBox(OH_GRAD_UNDERGRAD_TAS_PANE, tasHeaderBox, CLASS_OH_RADIO_BOX, ENABLED);
        ToggleGroup tg = new ToggleGroup();
        Button removeTa = ohBuilder.buildTextButton(OH_REMOVE_TA_BUTTON, tasHeaderBox, OH_REMOVE_BUTTON, ENABLED);
        
        typeHeaderBox.getChildren().add(removeTa);
        //typeHeaderBox.getChildren().add(tasLabel);
        ohBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, true);
        ohBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        ohBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_CENTERED_COLUMN);
        TableColumn typeColumn = ohBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
      
        Label startL = ohBuilder.buildLabel(OH_START_LABEL, officeHoursHeaderBox, SY_HBOX, ENABLED);
        ComboBox startTimeOH = ohBuilder.buildComboBox(OH_START_COMBOBOX,"","", officeHoursHeaderBox, CLASS_S_COMBOBOX, ENABLED);
        Label endL = ohBuilder.buildLabel(OH_END_LABEL, officeHoursHeaderBox, SY_HBOX, ENABLED);
        ComboBox endTimeOH = ohBuilder.buildComboBox(OH_END_COMBOBOX,"","", officeHoursHeaderBox, CLASS_S_COMBOBOX, ENABLED);

        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "friday");

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPaneOH = new SplitPane(leftPane, rightPane);
        sPaneOH.setDividerPositions(.4);
        sPaneOH.setOrientation(Orientation.VERTICAL);
        workspace = new BorderPane();
        ScrollPane siteScroll = new ScrollPane();
        ScrollPane OHScroll = new ScrollPane();
        
        
        //Syllabus tab
        
        VBox syllabusBox = ohBuilder.buildVBox(S_SYLLABUS_VBOX, null, SY_VBOX, ENABLED);
        HBox descriptionBox = ohBuilder.buildHBox(S_DES_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox topicsBox = ohBuilder.buildHBox(S_TOP_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox prerequisitesBox = ohBuilder.buildHBox(S_PRE_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox outcomesBox = ohBuilder.buildHBox(S_OUT_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox textbooksBox = ohBuilder.buildHBox(S_TEX_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox gradedBox = ohBuilder.buildHBox(S_GRA_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox gradingBox = ohBuilder.buildHBox(S_GRAD_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox academicBox = ohBuilder.buildHBox(S_ACA_HBOX, syllabusBox, SY_HBOX, ENABLED);
        HBox specialBox = ohBuilder.buildHBox(S_SPE_HBOX, syllabusBox, SY_HBOX, ENABLED);
        
        ScrollPane syllabusScroll = new ScrollPane(descriptionBox);
        
        TextArea des = new TextArea();
        TextArea top = new TextArea();
        TextArea pre = new TextArea();
        TextArea out = new TextArea();
        TextArea text = new TextArea();
        TextArea graded = new TextArea();
        TextArea grading = new TextArea();
        TextArea aca = new TextArea();
        TextArea spe = new TextArea();
        
        gui.addGUINode(SY_DES_TEXT_AREA, des);
        gui.addGUINode(SY_TOP_TEXT_AREA, top);
        gui.addGUINode(SY_PRE_TEXT_AREA, pre);
        gui.addGUINode(SY_OUT_TEXT_AREA, out);
        gui.addGUINode(SY_TEXT_TEXT_AREA, text);
        gui.addGUINode(SY_GRADED_TEXT_AREA, graded);
        gui.addGUINode(SY_GRADING_TEXT_AREA, grading);
        gui.addGUINode(SY_ACA_TEXT_AREA, aca);
        gui.addGUINode(SY_SPE_TEXT_AREA, spe);
        
        TitledPane desTitle = new TitledPane(props.getProperty(S_DES_TITLE_PANE) , des);
        TitledPane topTitle = new TitledPane(props.getProperty(S_TOP_TITLE_PANE) , top);
        TitledPane preTitle = new TitledPane(props.getProperty(S_PRE_TITLE_PANE) , pre);
        TitledPane outTitle = new TitledPane(props.getProperty(S_OUT_TITLE_PANE) , out);
        TitledPane textTitle = new TitledPane(props.getProperty(S_TEX_TITLE_PANE) , text);
        TitledPane gradedTitle = new TitledPane(props.getProperty(S_GRADED_TITLE_PANE) , graded);
        TitledPane gradingTitle = new TitledPane(props.getProperty(S_GRADING_TITLE_PANE) , grading);
        TitledPane acaTitle = new TitledPane(props.getProperty(S_ACA_TITLE_PANE) , aca);
        TitledPane speTitle = new TitledPane(props.getProperty(S_SPE_TITLE_PANE) , spe);
        
        languageSettings.addLabeledControlProperty(S_DES_TITLE_PANE, desTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_TOP_TITLE_PANE, topTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_PRE_TITLE_PANE, preTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_OUT_TITLE_PANE, outTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_TEX_TITLE_PANE, textTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_GRADED_TITLE_PANE, gradedTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_GRADING_TITLE_PANE, gradingTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_ACA_TITLE_PANE, acaTitle.textProperty());
        languageSettings.addLabeledControlProperty(S_SPE_TITLE_PANE, speTitle.textProperty());
        
        
       
        //GridPane syllabusGrid = new GridPane();
        //Button desExpand = ohBuilder.buildTextButton(SY_DES, descriptionBox, SY_HBOX, ENABLED);
        //Button topExpand = ohBuilder.buildTextButton(SY_TOP_BUTTON, topicsBox, SY_HBOX, ENABLED);
        //Button preExpand = ohBuilder.buildTextButton(SY_PRE_BUTTON, prerequisitesBox, SY_HBOX, ENABLED);
        //Button outExpand = ohBuilder.buildTextButton(SY_OUT_BUTTON, outcomesBox, SY_HBOX, ENABLED);
        //Button texExpand = ohBuilder.buildTextButton(SY_TEX_BUTTON, textbooksBox, SY_HBOX, ENABLED);
        //Button gradedExpand = ohBuilder.buildTextButton(SY_GRADED_BUTTON, gradedBox, SY_HBOX, ENABLED);
        //Button gradingExpand = ohBuilder.buildTextButton(SY_GRADING_BUTTON, gradingBox, SY_HBOX, ENABLED);
        //Button acaExpand = ohBuilder.buildTextButton(SY_ACA_BUTTON, academicBox, SY_HBOX, ENABLED);
        //Button speExpand = ohBuilder.buildTextButton(SY_SPE_BUTTON, specialBox, SY_HBOX, ENABLED);
        
        
        
        Accordion syllabusAcc = new Accordion(desTitle, topTitle, preTitle, outTitle, textTitle, gradedTitle, gradingTitle, acaTitle, speTitle);
        
        //syllabusScroll.setContent(syllabusGrid);
        syllabus.setContent(syllabusScroll);
        syllabusScroll.setContent(syllabusAcc);
        
        //meetingTimes Tab
        VBox mtBox = ohBuilder.buildVBox(MT_TAB_VBOX, null, MT_VBOX, ENABLED);
        VBox lectureBox = ohBuilder.buildVBox(MT_LECTURE_VBOX, mtBox, MT_VBOX, ENABLED);
        VBox recitationBox = ohBuilder.buildVBox(MT_RECITATION_VBOX, mtBox, MT_VBOX, ENABLED);
        VBox labBox = ohBuilder.buildVBox(MT_LAB_VBOX, mtBox, MT_VBOX, ENABLED);
        HBox lecHBox = ohBuilder.buildHBox(MT_LEC_HBOX, lectureBox, MT_VBOX, ENABLED);
        HBox recHBox = ohBuilder.buildHBox(MT_REC_HBOX, recitationBox, MT_VBOX, ENABLED);
        HBox labHBox = ohBuilder.buildHBox(MT_LAB_HBOX, labBox, MT_VBOX, ENABLED);
        
        Button lecPlus = new Button("+");
        Button lecMinus = new Button("-");
        Button recPlus = new Button("+");
        Button recMinus = new Button("-");
        Button labPlus = new Button("+");
        Button labMinus = new Button("-");
        
        gui.addGUINode(M_LECTURE_ADD, lecPlus);
        gui.addGUINode(M_LECTURE_REMOVE, lecMinus);
        gui.addGUINode(M_RECITATION_ADD, recPlus);
        gui.addGUINode(M_RECITATION_REMOVE, recMinus);
        gui.addGUINode(M_LAB_ADD, labPlus);
        gui.addGUINode(M_LAB_REMOVE, labMinus);
        
        TableView<Lecture> lectureTable = new TableView();
        TableColumn lectureSection = new TableColumn("Section");
        TableColumn lectureDays = new TableColumn("Days");
        TableColumn lectureTime = new TableColumn("Time");
        TableColumn lectureRoom = new TableColumn("Room");
        lectureTable.setEditable(ENABLED);
        lectureSection.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureDays.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureTime.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureRoom.setCellFactory(TextFieldTableCell.forTableColumn());
        
        lectureSection.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        lectureDays.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        lectureTime.setCellValueFactory(new PropertyValueFactory<String, String>("time"));
        lectureRoom.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        
        lectureSection.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        lectureDays.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0/9.0));
        lectureTime.prefWidthProperty().bind(lectureTable.widthProperty().multiply(3.0/9.0));
        lectureRoom.prefWidthProperty().bind(lectureTable.widthProperty().multiply(3.0/9.0));
        
        lectureTable.getColumns().add(lectureSection);
        lectureTable.getColumns().add(lectureDays);
        lectureTable.getColumns().add(lectureTime);
        lectureTable.getColumns().add(lectureRoom);
        
        TableView<Recitation> recitationTable = new TableView();
        TableColumn recitationSection = new TableColumn("Section");
        TableColumn recitationDays = new TableColumn("Days");
        TableColumn recitationRoom = new TableColumn("Room");
        TableColumn recitationTA1 = new TableColumn("TA1");
        TableColumn recitationTA2 = new TableColumn("TA2");
        recitationTable.setEditable(ENABLED);
        recitationSection.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationDays.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationRoom.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationTA1.setCellFactory(TextFieldTableCell.forTableColumn());
        recitationTA2.setCellFactory(TextFieldTableCell.forTableColumn());
        
        recitationSection.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        recitationDays.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        recitationRoom.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        recitationTA1.setCellValueFactory(new PropertyValueFactory<String, String>("TA1"));
        recitationTA2.setCellValueFactory(new PropertyValueFactory<String, String>("TA2"));
        
        
        recitationSection.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0/9.0));
        recitationDays.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        recitationRoom.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0/9.0));
        recitationTA1.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        recitationTA2.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        
        recitationTable.getColumns().add(recitationSection);
        recitationTable.getColumns().add(recitationDays);
        recitationTable.getColumns().add(recitationRoom);
        recitationTable.getColumns().add(recitationTA1);
        recitationTable.getColumns().add(recitationTA2);
        
        TableView<Lab> labsTable = new TableView();
        TableColumn labsSection = new TableColumn("Section");
        TableColumn labsDays = new TableColumn("Days & Time");
        TableColumn labsRoom = new TableColumn("Room");
        TableColumn labsTA1 = new TableColumn("TA1");
        TableColumn labsTA2 = new TableColumn("TA2");
        
        labsSection.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        labsDays.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        labsRoom.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
        labsTA1.setCellValueFactory(new PropertyValueFactory<String, String>("TA1"));
        labsTA2.setCellValueFactory(new PropertyValueFactory<String, String>("TA2"));
        labsTable.setEditable(ENABLED);
        labsSection.setCellFactory(TextFieldTableCell.forTableColumn());
        labsDays.setCellFactory(TextFieldTableCell.forTableColumn());
        labsRoom.setCellFactory(TextFieldTableCell.forTableColumn());
        labsTA1.setCellFactory(TextFieldTableCell.forTableColumn());
        labsTA2.setCellFactory(TextFieldTableCell.forTableColumn());
        
        gui.addGUINode(M_LECTURE_TABLE, lectureTable);
        gui.addGUINode(M_RECITATION_TABLE, recitationTable);
        gui.addGUINode(M_LAB_TABLE, labsTable);
        
        labsSection.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0/9.0));
        labsDays.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        labsRoom.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0/9.0));
        labsTA1.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        labsTA2.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        
        labsTable.getColumns().add(labsSection);
        labsTable.getColumns().add(labsDays);
        labsTable.getColumns().add(labsRoom);
        labsTable.getColumns().add(labsTA1);
        labsTable.getColumns().add(labsTA2);
        
        
        
        Label mtLecLabel = ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.MT_LECTURE_LABEL, null, CLASS_MT_PANE_LABEL, ENABLED);
        Label mtRecLabel = ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.MT_RECITATION_LABEL, null, CLASS_MT_PANE_LABEL, ENABLED);
        Label mtLabLabel = ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.MT_LABS_LABEL, null, CLASS_MT_PANE_LABEL, ENABLED);
        
        lecHBox.getChildren().add(lecPlus);
        lecHBox.getChildren().add(lecMinus);
        lecHBox.getChildren().add(mtLecLabel);
        recHBox.getChildren().add(recPlus);
        recHBox.getChildren().add(recMinus);
        recHBox.getChildren().add(mtRecLabel);
        labHBox.getChildren().add(labPlus);
        labHBox.getChildren().add(labMinus);
        labHBox.getChildren().add(mtLabLabel);
        lectureBox.getChildren().add(lectureTable);
        recitationBox.getChildren().add(recitationTable);
        labBox.getChildren().add(labsTable);
        //ohBuilder.buildLabel(CourseSiteGeneratorPropertyType.S_NAME_LABEL, mtBox, 3, 2, 2, 2, CLASS_S_INSTRUCTOR_LABEL, ENABLED);
        ScrollPane mtScroll = new ScrollPane();
        meetingTimes.setContent(mtScroll);
        mtScroll.setContent(mtBox);
        
        //Schedule Tab
        ObservableList<String> typeList = readData(PATH_PROGRAMDATA + props.getProperty(SCH_PROGRAM_DATA_TYPE));
        VBox schBox = ohBuilder.buildVBox(SCH_TAB_VBOX, null, MT_VBOX, ENABLED);
        GridPane calGrid = ohBuilder.buildGridPane(CALENDAR_GRID_BOX, schBox, SY_HBOX, ENABLED);
        VBox siBox = ohBuilder.buildVBox(SCH_SI_VBOX, schBox, MT_VBOX, ENABLED);
        HBox siHBox = ohBuilder.buildHBox(SCH_SI_HBOX, siBox, MT_VBOX, ENABLED);
        GridPane addGrid = ohBuilder.buildGridPane(ADD_GRID_BOX, schBox, SY_HBOX, ENABLED);
        ohBuilder.buildLabel(CALENDAR_LABEL, calGrid , 2, 1, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(START_LABEL, calGrid , 2, 2, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(END_LABEL, calGrid , 10, 2, 1, 1, SCH_LABEL, ENABLED);
        Button removeSItems = ohBuilder.buildTextButton(OH_REMOVE_SCH_BUTTON, siHBox, OH_REMOVE_BUTTON, ENABLED);
        ohBuilder.buildLabel(SI_LABEL, siHBox, SCH_LABEL, ENABLED);
        
        ohBuilder.buildLabel(ADD_LABEL, addGrid , 2, 1, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(TYPE_LABEL, addGrid , 2, 3, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(DATE_LABEL, addGrid , 2, 6, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(TITLE_LABEL, addGrid , 2, 9, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(TOPIC_LABEL, addGrid , 2, 12, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildLabel(LINK_LABEL, addGrid , 2, 15, 1, 1, SCH_LABEL, ENABLED);
        ohBuilder.buildTextButton(SCH_ADD_BUTTON, addGrid,4,18,1,1, SY_HBOX, ENABLED);
        ohBuilder.buildTextButton(SCH_CLEAR_BUTTON, addGrid,7,18,1,1, SY_HBOX, ENABLED);
        ohBuilder.buildTextField(SCH_TITLE_TF, addGrid, 5,9,1,1, SY_HBOX, ENABLED);
        ohBuilder.buildTextField(SCH_TOPIC_TF, addGrid,5,12,1,1, SY_HBOX, ENABLED);
        ohBuilder.buildTextField(SCH_LINK_TF, addGrid,5,15,1,1, SY_HBOX, ENABLED);
        ComboBox typeBox = ohBuilder.buildComboBox(SCH_TYPE_COMBOBOX, addGrid, 5,3,1,1, CLASS_S_COMBOBOX, ENABLED, "","");
        typeBox.setItems(typeList);
        typeBox.setEditable(ENABLED);
        DatePicker addDate = new DatePicker();
        addGrid.add(addDate, 5, 6);
        gui.addGUINode(SCH_Add_DATEPICKER, addDate);
        
        TableView<Schedule> siTable = new TableView<Schedule>();
        gui.addGUINode(SCH_SI_TABLE, siTable);
        TableColumn siType = new TableColumn("Type");
        TableColumn siDate = new TableColumn("Days");
        TableColumn siTitle = new TableColumn("Title");
        TableColumn siTopic = new TableColumn("Topic");
        
        siType.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        siDate.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
        siTitle.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        siTopic.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        
        siType.setCellFactory(TextFieldTableCell.forTableColumn());
        siDate.setCellFactory(TextFieldTableCell.forTableColumn());
        siTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        siTopic.setCellFactory(TextFieldTableCell.forTableColumn());
        
        siType.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        siDate.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        siTitle.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0/9.0));
        siTopic.prefWidthProperty().bind(lectureTable.widthProperty().multiply(4.0/9.0));
        
        siTable.getColumns().add(siType);
        siTable.getColumns().add(siDate);
        siTable.getColumns().add(siTitle);
        siTable.getColumns().add(siTopic);
        
        siBox.getChildren().add(siTable);
       
        
        
        DatePicker starting = new DatePicker();
        DatePicker ending = new DatePicker();
        starting.setValue(LocalDate.of(2018, 8, 20));
        ending.setValue(LocalDate.of(2018, 12, 10));
        gui.addGUINode(SCH_START_TIME, starting);
        gui.addGUINode(SCH_END_TIME, ending);
        calGrid.add(starting, 3, 2);
        calGrid.add(ending, 15, 2);
        
        
        
        ScrollPane scheduleScroll = new ScrollPane();
        scheduleScroll.setContent(schBox);
         schedule.setContent(scheduleScroll);
        
        
        //Site Split Pane
        SplitPane sPaneS = new SplitPane(bannerPane, pagesPane, stylePane, instructorPane);
        sPaneS.setOrientation(Orientation.VERTICAL);
        sPaneS.setDividerPositions(.4);
        // AND PUT EVERYTHING IN THE WORKSPACE
        //((BorderPane) workspace).setCenter(sPane);
        siteScroll.setContent(sPaneS);
        OHScroll.setContent(sPaneOH);
        ((BorderPane) workspace).setCenter(layout);
        officeHours.setContent(OHScroll);
        site.setContent(siteScroll);
        
        
        
        
        
        //Pane help = new Pane(layout);
        
        
        sPaneS.prefHeightProperty().bind(workspace.heightProperty());
        sPaneS.prefWidthProperty().bind(workspace.widthProperty());
        sPaneOH.prefHeightProperty().bind(workspace.heightProperty());
        sPaneOH.prefWidthProperty().bind(workspace.widthProperty());
        syllabusAcc.prefHeightProperty().bind(workspace.heightProperty());
        syllabusAcc.prefWidthProperty().bind(workspace.widthProperty());
        mtBox.prefHeightProperty().bind(workspace.heightProperty());
        mtBox.prefWidthProperty().bind(workspace.widthProperty());
        schBox.prefWidthProperty().bind(workspace.widthProperty());
        schBox.prefHeightProperty().bind(workspace.heightProperty());
        
        
        
    }

    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }

    public void initControllers()  {
         PropertiesManager props = PropertiesManager.getPropertiesManager();
        OfficeHoursController controller = new OfficeHoursController((CourseSiteGeneratorApp) app);
        AppGUIModule gui = app.getGUIModule();
        //CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
        ComboBox start = (ComboBox) gui.getGUINode(OH_START_COMBOBOX);
        ComboBox end = (ComboBox) gui.getGUINode(OH_END_COMBOBOX);

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));
        TextField titleTextField = ((TextField) gui.getGUINode(S_BANNER_TITLE_TEXT_FIELD));
        TextField nameTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_NAME));
        TextField emailTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_EMAIL));
        TextField roomTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_ROOM));
        TextField linkTF = ((TextField) gui.getGUINode(S_INSTRUCTOR_HP));
        
        TextArea iOH = (TextArea) gui.getGUINode(S_INSTRUCTOR_OH);
        TextArea des = (TextArea) gui.getGUINode(SY_DES_TEXT_AREA);
        TextArea top = (TextArea) gui.getGUINode(SY_TOP_TEXT_AREA);
        TextArea pre = (TextArea) gui.getGUINode(SY_PRE_TEXT_AREA);
        TextArea out = (TextArea) gui.getGUINode(SY_OUT_TEXT_AREA);
        TextArea text = (TextArea) gui.getGUINode(SY_TEXT_TEXT_AREA);
        TextArea graded = (TextArea) gui.getGUINode(SY_GRADED_TEXT_AREA);
        TextArea grading = (TextArea) gui.getGUINode(SY_GRADING_TEXT_AREA);
        TextArea aca = (TextArea) gui.getGUINode(SY_ACA_TEXT_AREA);
        TextArea spe = (TextArea) gui.getGUINode(SY_SPE_TEXT_AREA);
        
        Button style1 = (Button) gui.getGUINode(S_STYLE_FAV_BUTTON);
        Button style2 = (Button) gui.getGUINode(S_STYLE_NAV_BUTTON);
        Button style3 = (Button) gui.getGUINode(S_STYLE_LEFT_BUTTON);
        Button style4 = (Button) gui.getGUINode(S_STYLE_RIGHT_BUTTON);
        
        
        ComboBox subjectCombo = (ComboBox)gui.getGUINode(S_SUBJECT_COMBOBOX);
        ComboBox numberCombo = (ComboBox)gui.getGUINode(S_NUMBER_COMBOBOX);
        ComboBox yearCombo = (ComboBox)gui.getGUINode(S_YEAR_COMBOBOX);
        ComboBox semesterCombo = (ComboBox)gui.getGUINode(S_SEMESTER_COMBOBOX);
        
        
        
        CheckBox homeCb = (CheckBox)gui.getGUINode(S_HOME_CHECK_BOX);
        CheckBox syllabusCb = (CheckBox)gui.getGUINode(S_SYLLABUS_CHECK_BOX);
        CheckBox scheduleCb = (CheckBox)gui.getGUINode(S_SCHEDULE_CHECK_BOX);
        CheckBox hwsCb = (CheckBox)gui.getGUINode(S_HWS_CHECK_BOX);
        
        TableView<Lecture> lecTable = (TableView) gui.getGUINode(M_LECTURE_TABLE);
        TableView<Recitation> recTable = (TableView) gui.getGUINode(M_RECITATION_TABLE);
        TableView<Lab> labTable = (TableView) gui.getGUINode(M_LAB_TABLE);
        
        
        
        titleTextField.textProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text1 = titleTextField.getText();
            //EditText_Transaction addTitle = new EditText_Transaction(data, text1);
                //app.processTransaction(addTitle);
        });
        
        homeCb.setOnAction(e ->{
            controller.processHomeCheckBox();
        });
        
        syllabusCb.setOnAction(e ->{
            controller.processSyllabusCheckBox();
        });
         
        scheduleCb.setOnAction(e ->{
            controller.processScheduleCheckBox();
        });
        
        hwsCb.setOnAction(e ->{
            controller.processHwsCheckBox();
        });
        
        nameTF.textProperty().addListener(e ->{
            //controller.processText();
        });
        emailTF.textProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text1 = emailTF.getText();
            //EditText_Transaction addTitle = new EditText_Transaction(data, text1);
                //app.processTransaction(addTitle);
        });
        
        roomTF.textProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text2 = roomTF.getText();
            //EditText_Transaction addTitle = new EditText_Transaction(data, text2);
                //app.processTransaction(addTitle);
        });
        linkTF.textProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text3 = linkTF.getText();
            //EditText_Transaction addTitle = new EditText_Transaction(data, text3);
                //app.processTransaction(addTitle);
        });
        
        iOH.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            
            String text4 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, iOH.getText(), null, iOH);
            }
            else{
                controller.processEditTextArea(true, null, iOH.getText(), iOH);
            }
        });
        
        des.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text5 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, des.getText(), null, des);
            }
            else{
                controller.processEditTextArea(true, null, des.getText(), des);
            }
        });
        top.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text6 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, top.getText(), null, top);
            }
            else{
                controller.processEditTextArea(true, null, top.getText(), top);
            }
        });
        pre.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text7 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, pre.getText(), null, pre);
            }
            else{
                controller.processEditTextArea(true, null, pre.getText(), pre);
            }
        });
        out.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text8 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, out.getText(), null, out);
            }
            else{
                controller.processEditTextArea(true, null, out.getText(), out);
            }
        });
        text.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text9 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, text.getText(), null, text);
            }
            else{
                controller.processEditTextArea(true, null, text.getText(), text);
            }
        });
        graded.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text0 = iOH.getText();
            if(graded.isFocused()){
                controller.processEditTextArea(false, graded.getText(), null, graded);
            }
            else{
                controller.processEditTextArea(true, null, graded.getText(), graded);
            }
        });
        grading.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text11 = iOH.getText();
           //CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            //String text13 = iOH.getText();
            if(grading.isFocused()){
                controller.processEditTextArea(false, grading.getText(), null, grading);
            }
            else{
                controller.processEditTextArea(true, null, grading.getText(), grading);
            }
               
        });
        aca.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text12 = iOH.getText();
            //CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            //String text13 = iOH.getText();
            if(spe.isFocused()){
                controller.processEditTextArea(false, aca.getText(), null, aca);
            }
            else{
                controller.processEditTextArea(true, null, aca.getText(), aca);
            }
        });
        spe.focusedProperty().addListener(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            String text13 = iOH.getText();
            if(spe.isFocused()){
                controller.processEditTextArea(false, spe.getText(), null, spe);
            }
            else{
                controller.processEditTextArea(true, null, spe.getText(), spe);
            }
            //EditText_Transaction addTitle = new EditText_Transaction(data, text13);
                //app.processTransaction(addTitle);
        });
        
        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        }); 
        
        style1.setOnMouseClicked(e ->{
            controller.processImageOneFileChooser();
        });
        style2.setOnMouseClicked(e ->{
             controller.processImageTwoFileChooser();
        });
        style3.setOnMouseClicked(e ->{
             controller.processImageThreeFileChooser();
        });
        style4.setOnMouseClicked(e ->{
             controller.processImageFourFileChooser();
        });
         
        yearCombo.setOnAction(e ->{
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
               String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
                
                AddSubjectComboBoxData_Transaction addSubjectTransaction = new AddSubjectComboBoxData_Transaction(data, yearName);
                app.processTransaction(addSubjectTransaction);
                
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                data.setExportD(exportName);
        });
          
        semesterCombo.setOnAction(e ->{
                CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
                String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
                
                AddSubjectComboBoxData_Transaction addSubjectTransaction = new AddSubjectComboBoxData_Transaction(data, semesterName);
                app.processTransaction(addSubjectTransaction);
                
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                data.setExportD(exportName);
        });
        
           subjectCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
               CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
               //String a =  (String)subjectCombo.getSelectionModel().getSelectedItem();
                
            try {
                controller.processAddSubject();
                
                
                
                ObservableList<String> subjectList = readData(PATH_PROGRAMDATA + props.getProperty(S_PROGRAM_DATA_SUBJECT));
                subjectCombo.setItems(subjectList);
                //subjectCombo
                
  
            } catch (IOException ex) {
                Logger.getLogger(CourseSiteGeneratorWorkspace.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println(data.getSubject());
                String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
         
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                data.setExportD(exportName);
                //subjectCombo.setValue(subjectCombo.getItems().get(subjectCombo.getItems().size()-1));
           });
        /*subjectCombo.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
            try {
                controller.processAddSubject();
                ObservableList<String> subjectList = readData("/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/programdata/Subject");
                subjectCombo.setItems(subjectList);
            } catch (IOException ex) {
                Logger.getLogger(CourseSiteGeneratorWorkspace.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                //String a = data.getSubject();
            String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
         
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                
                /*String text = subjectCombo.getEditor().getText();
                subjectCombo.getEditor().setText("");
                  //  }
                   
                // }

       //}); 
        
        numberCombo.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
          String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
                
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                   
    }); 
        
        .getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
        subject.textProperty().bind(Binding.format))
        )*/
        
        numberCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            CourseSiteGeneratorData data = (CourseSiteGeneratorData) app.getDataComponent();
            try {
                controller.processAddNumber();
                ObservableList<String> numberList = readData(PATH_PROGRAMDATA + props.getProperty(S_PROGRAM_DATA_NUMBER));
                numberCombo.setItems(numberList);
            } catch (IOException ex) {
                Logger.getLogger(CourseSiteGeneratorWorkspace.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
                
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                data.setExportD(exportName);
           });
        
        /*numberCombo.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
            try {
                controller.processAddNumber();
                ObservableList<String> numberList = readData("/Users/Youyi/Documents/219/HW/HW4/apps/CourseSiteGenerator/programdata/Number");
                numberCombo.setItems(numberList);
            } catch (IOException ex) {
                Logger.getLogger(CourseSiteGeneratorWorkspace.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            System.out.println(data.getSubject());
            
            String subjectName = (String) subjectCombo.getSelectionModel().getSelectedItem() + "_";
                String numberName = (String) numberCombo.getSelectionModel().getSelectedItem() + "_";
                String semesterName = (String) semesterCombo.getSelectionModel().getSelectedItem()+ "_";
                String yearName = (Integer)yearCombo.getSelectionModel().getSelectedItem() + "";
                
                String exportName = props.getProperty(APP_PATH_EXPORT)+ subjectName + numberName + semesterName + yearName + props.getProperty(S_PUBLIC_HTML_LABEL) ;
                Label exportText = (Label)gui.getGUINode(S_EXPORT_TEXT_LABEL);
                exportText.setText(exportName);
                    }
                 }

        });*/
        
        start.setOnAction(e ->{
            if(start.getSelectionModel().getSelectedItem()!=null){
           controller.processTimeRange();
           timeRangeEndFoolproofDesign();
           
           //app.getFoolproofModule().updateAll();
            }
        });
        
        end.setOnAction(e ->{
            if(start.getSelectionModel().getSelectedItem()!=null){
                controller.processTimeRange();
                //timeRangeStartFoolproofDesign();
                
                //app.getFoolproofModule().updateAll();
            }
           
        });
       
        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
        });
        gui.getGUINode(OH_REMOVE_TA_BUTTON).setOnMouseClicked(e->{
            controller.processRemoveTA();
        });
        
        gui.getGUINode(M_LECTURE_ADD).setOnMouseClicked(e ->{
            controller.processAddLecture();
        });
        gui.getGUINode(M_LECTURE_REMOVE).setOnMouseClicked(e ->{
            controller.processRemoveLecture();
        });
        gui.getGUINode(M_RECITATION_ADD).setOnMouseClicked(e ->{
            controller.processAddRecitation();
        });
        gui.getGUINode(M_RECITATION_REMOVE).setOnMouseClicked(e ->{
            controller.processRemoveRecitation();
        }); 
        gui.getGUINode(M_LAB_ADD).setOnMouseClicked(e ->{
            controller.processAddLab();
        });
        gui.getGUINode(M_LAB_REMOVE).setOnMouseClicked(e ->{
            controller.processRemoveLab();
        });
        
        for(TableColumn tc : lecTable.getColumns()){
            tc.setOnEditCommit(
                    new EventHandler<CellEditEvent<Lecture, String>>(){
                    public void handle (CellEditEvent<Lecture, String> t){
                        Lecture edit = lecTable.getSelectionModel().getSelectedItem();
                        TablePosition cell = lecTable.getSelectionModel().getSelectedCells().get(0);
                        int col = cell.getColumn();
                        
                        controller.processEditLecture(col, t.getNewValue());
                    }
        }
            );
        }
        //setonEditcommit
        for(TableColumn tc : recTable.getColumns()){
            tc.setOnEditCommit(
                    new EventHandler<CellEditEvent<Recitation, String>>(){
                    public void handle (CellEditEvent<Recitation, String> t){
                        Recitation edit = recTable.getSelectionModel().getSelectedItem();
                        TablePosition cell = recTable.getSelectionModel().getSelectedCells().get(0);
                        int col = cell.getColumn();
                        
                        controller.processEditRecitation(col, t.getNewValue());
                    }
        }
            );
        }
        
        for(TableColumn tc : labTable.getColumns()){
            tc.setOnEditCommit(
                    new EventHandler<CellEditEvent<Lab, String>>(){
                    public void handle (CellEditEvent<Lab, String> t){
                        Lab edit = labTable.getSelectionModel().getSelectedItem();
                        TablePosition cell = labTable.getSelectionModel().getSelectedCells().get(0);
                        int col = cell.getColumn();
                        
                        controller.processEditLab(col, t.getNewValue());
                    }
        }
            );
        }
        
        ((Button)gui.getGUINode(SCH_ADD_BUTTON)).setOnMouseClicked(e->{
            controller.processAddSchedule();
        });
        ((Button)gui.getGUINode(OH_REMOVE_SCH_BUTTON)).setOnMouseClicked(e->{
            controller.processRemoveSchedule();
        });
        
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new CourseSiteGeneratorFoolproofDesign((CourseSiteGeneratorApp) app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
    
    public void timeRangeStartFoolproofDesign(){
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
            
            
            startList.clear();
            
            for(TimeSlot timeSlot : officeHoursTemp){
                
            if(convertForCompare(timeSlot.getStartTime()) < convertForCompare(en))
            startList.add(timeSlot.getStartTime());
   
            
            
            
        }
            start.setItems(startList);
        }
    }
    
    public void timeRangeEndFoolproofDesign(){
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
            
            
            
            endList.clear();
            
            for(TimeSlot timeSlot : officeHoursTemp){
                
            
   
            if(convertForCompare(timeSlot.getEndTime()) > convertForCompare(st))
            endList.add(timeSlot.getEndTime());
            
            
        }
            
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
    }
        
        public ObservableList<String> readData(String filePath) throws IOException{
            ObservableList<String> temp = FXCollections.observableArrayList();
            File file = new File(filePath);
            Scanner sc = new Scanner(file); 
            
            while(sc.hasNextLine())
                temp.add(sc.nextLine());
            
            return temp;
        }
    }

