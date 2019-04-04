/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.CourseSiteGeneratorData;
import jtps.jTPS_Transaction;

/**
 *
 * @author Youyi
 */
public class AddSubjectComboBoxData_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String subjectComboBoxData;
    
    public AddSubjectComboBoxData_Transaction(CourseSiteGeneratorData initData, String initComboBoxData){
    data = initData;
    subjectComboBoxData = initComboBoxData;
    
}
    
    @Override
    public void doTransaction() {
        data.addSubject();
        /*data.addTA(comboBoxData); 
        BufferedWriter writer = new BufferedWriter(new FileWriter("", true));*/
    }

    @Override
    public void undoTransaction() {
        data.removeSubject();
        /*data.removeTA(comboBoxData);*/
    }
    
}
