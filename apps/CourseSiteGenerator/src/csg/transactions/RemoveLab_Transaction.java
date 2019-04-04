package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CourseSiteGeneratorData;
import csg.data.Lab;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class RemoveLab_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    Lab lab;
    
    public RemoveLab_Transaction(CourseSiteGeneratorData initData, Lab initLab) {
        data = initData;
        lab = initLab;
    }

    @Override
    public void doTransaction() {
        data.removeLab(lab);
    }

    @Override
    public void undoTransaction() {
        data.addLab(lab);   
    }
}
