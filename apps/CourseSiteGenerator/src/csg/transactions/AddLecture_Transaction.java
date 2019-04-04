package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lecture;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddLecture_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Lecture lec;
    
    public AddLecture_Transaction(CourseSiteGeneratorData initData, Lecture initLec) {
        data = initData;
        lec = initLec;
    }

    @Override
    public void doTransaction() {
        data.addLecture(lec);        
    }

    @Override
    public void undoTransaction() {
        data.removeLecture(lec);
    }
}
