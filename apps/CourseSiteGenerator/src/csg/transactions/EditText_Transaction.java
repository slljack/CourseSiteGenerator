/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.CourseSiteGeneratorData;
import javafx.scene.control.TextArea;
import jtps.jTPS_Transaction;

/**
 *
 * @author Youyi
 */
public class EditText_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorData data;
    String oldText;
    String newText;
    TextArea textArea;
    
    public EditText_Transaction(TextArea textArea, String oldText, String newText){
    this.oldText = oldText;
    this.newText = newText;
    this.textArea = textArea;
    
}
    
    @Override
    public void doTransaction() {
        textArea.setText(newText);
    }

    @Override
    public void undoTransaction() {
        textArea.setText(oldText);
    }
    
}
