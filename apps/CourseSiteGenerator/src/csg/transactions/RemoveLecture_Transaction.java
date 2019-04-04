package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lecture;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class RemoveLecture_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Lecture lec;
    
    public RemoveLecture_Transaction(CourseSiteGeneratorData initData, Lecture initLec) {
        data = initData;
        lec = initLec;
    }

    @Override
    public void doTransaction() {
        data.removeLecture(lec);
    }

    @Override
    public void undoTransaction() {
        data.addLecture(lec);      
    }
}
