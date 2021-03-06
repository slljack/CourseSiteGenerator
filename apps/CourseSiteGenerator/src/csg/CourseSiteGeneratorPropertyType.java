package csg;

/**
 * This class provides the properties that are needed to be loaded for
 * setting up To Do List Maker workspace controls including language-dependent
 * text.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum CourseSiteGeneratorPropertyType {

    /* THESE ARE THE NODES IN OUR APP */
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OH_OK_PROMPT,
    OH_CANCEL_PROMPT,
    
    //site tab
    S_SITE_TAB,
    S_NUMBER_COMBOBOX,
    S_SUBJECT_COMBOBOX,
    S_YEAR_COMBOBOX,
    S_SEMESTER_COMBOBOX,
    
    //site labels
    S_BANNER_LABEL,
    S_PAGES_LABEL,
    S_STYLE_LABEL,
    S_INSTRUCTOR_LABEL,
    S_SUBJECT_LABEL,
    S_NUMBER_LABEL,
    S_SEMESTER_LABEL,
    S_YEAR_LABEL,
    S_TITLE_LABEL,
    S_EXPORT_LABEL,
    
    
    //pages labels
    S_HOME_LABEL,
    S_SYLLABUS_LABEL,
    S_SCHEDULE_LABEL,
    S_HWS_LABEL,
    
    //style labels
    S_FONT_LABEL,
    S_NOTE_LABEL,
    
    //instructor labels
    S_NAME_LABEL,
    S_EMAIL_LABEL,
    S_ROOM_LABEL,
    S_OFFICE_HOURS_LABEL,
    S_HOME_PAGE_LABEL,
    S_OH_TITLE_PANE,
    
    MT_LECTURE_LABEL,
    MT_RECITATION_LABEL,
    MT_LABS_LABEL,
    
    S_DES_TITLE_PANE,
    S_TOP_TITLE_PANE,
    S_PRE_TITLE_PANE,
    S_OUT_TITLE_PANE,
    S_TEX_TITLE_PANE,
    S_GRADED_TITLE_PANE,
    S_GRADING_TITLE_PANE,
    S_ACA_TITLE_PANE,
    S_SPE_TITLE_PANE,
    
    CSG_SITE_TAB,
    CSG_SYLLABUS_TAB,
    CSG_MT_TAB,
    CSG_OH_TAB,
    CSG_SCHEDULE_TAB,
    
    S_SYLLABUS_VBOX,
    S_DES_HBOX, 
    S_TOP_HBOX, 
    S_PRE_HBOX,
    S_OUT_HBOX,
    S_TEX_HBOX,
    S_GRA_HBOX,
    S_GRAD_HBOX,
    S_ACA_HBOX,
    S_SPE_HBOX,
    
    SY_TOP_BUTTON,
    SY_PRE_BUTTON,
    SY_OUT_BUTTON,
    SY_TEX_BUTTON,
    
    SY_DES_TEXT_AREA,
    SY_TOP_TEXT_AREA,
    SY_PRE_TEXT_AREA,
    SY_OUT_TEXT_AREA,
    SY_TEXT_TEXT_AREA,
    SY_GRADED_TEXT_AREA,
    SY_GRADING_TEXT_AREA,
    SY_ACA_TEXT_AREA,
    SY_SPE_TEXT_AREA,
    
    MT_TAB_VBOX,
    MT_LECTURE_VBOX,
    MT_RECITATION_VBOX,
    MT_LAB_VBOX,
    
    SCH_TAB_VBOX,
    SCH_SI_VBOX,
    CALENDAR_GRID_BOX,
    ADD_GRID_BOX,
    CALENDAR_LABEL,
    START_LABEL,
    END_LABEL,
    OH_REMOVE_SCH_BUTTON,
    SCH_SI_HBOX,
    SI_LABEL,
    
    TYPE_LABEL,
    DATE_LABEL,
    TITLE_LABEL,
    TOPIC_LABEL,
    LINK_LABEL,
    ADD_LABEL,
    
    SCH_ADD_BUTTON,
    SCH_CLEAR_BUTTON,
    SCH_TITLE_TF,
    SCH_TOPIC_TF,
    SCH_LINK_TF,
    SCH_TYPE_COMBOBOX,
    SCH_SI_TABLE,
    SCH_Add_DATEPICKER,
    SCH_START_TIME,
    SCH_END_TIME,
    
    OH_START_COMBOBOX,
    OH_END_COMBOBOX,
    OH_START_LABEL,
    OH_END_LABEL,
    
    
    
    MT_LEC_HBOX,
    MT_REC_HBOX,
    MT_LAB_HBOX,

    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    OH_LEFT_PANE,
    OH_TAS_HEADER_PANE,
    OH_TAS_HEADER_LABEL,
    OH_GRAD_UNDERGRAD_TAS_PANE,
    OH_ALL_RADIO_BUTTON,
    OH_GRAD_RADIO_BUTTON,
    OH_UNDERGRAD_RADIO_BUTTON,
    OH_TAS_HEADER_TEXT_FIELD,
    OH_TAS_TABLE_VIEW,
    OH_NAME_TABLE_COLUMN,
    OH_EMAIL_TABLE_COLUMN,
    OH_SLOTS_TABLE_COLUMN,
    OH_TYPE_TABLE_COLUMN,
    OH_NO_SELECTED_TITLE,
    OH_NO_SELECTED_CONTENT,
    OH_REMOVE_TA_BUTTON,

    OH_ADD_TA_PANE,
    OH_NAME_TEXT_FIELD,
    OH_EMAIL_TEXT_FIELD,
    OH_ADD_TA_BUTTON,

    OH_RIGHT_PANE,
    OH_OFFICE_HOURS_HEADER_PANE,
    OH_OFFICE_HOURS_HEADER_LABEL,
    OH_OFFICE_HOURS_TABLE_VIEW,
    OH_START_TIME_TABLE_COLUMN,
    OH_END_TIME_TABLE_COLUMN,
    OH_MONDAY_TABLE_COLUMN,
    OH_TUESDAY_TABLE_COLUMN,
    OH_WEDNESDAY_TABLE_COLUMN,
    OH_THURSDAY_TABLE_COLUMN,
    OH_FRIDAY_TABLE_COLUMN,
    OH_DAYS_OF_WEEK,
    OH_FOOLPROOF_SETTINGS,
    
    S_STYLE_FAV_BUTTON,
    S_STYLE_NAV_BUTTON,
    S_STYLE_LEFT_BUTTON,
    S_STYLE_RIGHT_BUTTON,
    
    //image
    //S_STYLE_FAV_IMAGE,
    //S_STYLE_NAV_IMAGE,
    //S_STYLE_LEFT_IMAGE,
    //S_STYLE_RIGHT_IMAGE,
    
    S_BANNER_GRIDPANE,
    S_FONT_COMBOBOX,
    //S_SUBJECT_COMBOBOX,
    S_PAGES_GRIDPANE,
    S_PAGES_CHECK_BOX,
    S_STYLE_GRIDPANE,
    S_INSTRUCTOR_GRIDPANE,
    //S_NUMBER_COMBOBOX,
    //S_SEMESTER_COMBOBOX,
    //S_YEAR_COMBOBOX,
    //S_TITLE_COMBOBOX,
    S_SITE_PANE,
    S_HOME_CHECK_BOX,
    S_SYLLABUS_CHECK_BOX,
    S_SCHEDULE_CHECK_BOX,
    S_HWS_CHECK_BOX,
    S_STYLE_BUTTON,
    
    S_INSTRUCTOR_NAME,
    S_INSTRUCTOR_ROOM,
    S_INSTRUCTOR_EMAIL,
    S_INSTRUCTOR_HP,
    S_INSTRUCTOR_OH,
    
    // FOR THE EDIT DIALOG
    OH_TA_EDIT_DIALOG,
    OH_TA_DIALOG_GRID_PANE,
    OH_TA_DIALOG_HEADER_LABEL, 
    OH_TA_DIALOG_NAME_LABEL,
    OH_TA_DIALOG_NAME_TEXT_FIELD,
    OH_TA_DIALOG_EMAIL_LABEL,
    OH_TA_DIALOG_EMAIL_TEXT_FIELD,
    OH_TA_DIALOG_TYPE_LABEL,
    OH_TA_DIALOG_TYPE_BOX,
    OH_TA_DIALOG_GRAD_RADIO_BUTTON,
    OH_TA_DIALOG_UNDERGRAD_RADIO_BUTTON,
    OH_TA_DIALOG_OK_BOX,
    OH_TA_DIALOG_OK_BUTTON, 
    OH_TA_DIALOG_CANCEL_BUTTON, 
    
    S_STYLE_ONE_IMAGE_VIEW,
    S_STYLE_TWO_IMAGE_VIEW,
    S_STYLE_THREE_IMAGE_VIEW,
    S_STYLE_FOUR_IMAGE_VIEW,
    
    M_LECTURE_ADD,
    M_LECTURE_REMOVE,
    M_RECITATION_ADD,
    M_RECITATION_REMOVE,
    M_LAB_ADD,
    M_LAB_REMOVE,
    M_LECTURE_TABLE,
    M_RECITATION_TABLE,
    M_LAB_TABLE,
    
    // THESE ARE FOR ERROR MESSAGES PARTICULAR TO THE APP
    OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT
}