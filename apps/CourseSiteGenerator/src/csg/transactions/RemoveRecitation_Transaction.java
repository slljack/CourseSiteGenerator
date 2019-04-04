package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.Recitation;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class RemoveRecitation_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Recitation rec;
    
    public RemoveRecitation_Transaction(CourseSiteGeneratorData initData, Recitation initRec) {
        data = initData;
        rec = initRec;
    }

    @Override
    public void doTransaction() {
        data.removeRecitation(rec);    
    }

    @Override
    public void undoTransaction() {
        data.addRecitation(rec);   
    }
}
