package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lecture;
import csg.data.Schedule;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddSchedule_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Schedule sch;
    
    public AddSchedule_Transaction(CourseSiteGeneratorData initData, Schedule initSch) {
        data = initData;
        sch = initSch;
    }

    @Override
    public void doTransaction() {
        data.addSchedule(sch);        
    }

    @Override
    public void undoTransaction() {
        data.removeSchedule(sch);
    }
}
