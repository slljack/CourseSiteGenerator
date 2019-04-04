package csg.transactions;

import csg.data.CourseSiteGeneratorData;
import java.time.LocalDate;
import jtps.jTPS_Transaction;
import csg.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class CheckBox_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String name;
    
     public CheckBox_Transaction(CourseSiteGeneratorData initData, String initName){
    data = initData;
    name = initName;
    
}

    @Override
    public void doTransaction() {
      data.CheckBox();
    }

    @Override
    public void undoTransaction() {
        data.UncheckBox();
    }
}