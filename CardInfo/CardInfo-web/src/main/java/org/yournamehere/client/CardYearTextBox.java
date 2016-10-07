/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Vlad
 */
public class CardYearTextBox extends CardInfoTextBox{
    int currentMonth=10;
    int currentYear=2016;
    protected boolean inputRised=true;
    private boolean dateIncorrectRised=false;
    private String numbers="0123456789";
    public CardMonthListBox monthElement;
    
    public void setToSensetiveCheckingMode(){
        notFilledEnoughRised=true;
        isEmptyRised=true;
        dateIncorrectRised=true;
    }

    public CardYearTextBox(String elementId, String noteElementName) {
        super(elementId, noteElementName);
        monthElement=new CardMonthListBox(this);
        RootPanel.get("validThruMonthContainer").add(monthElement);
        //sinkEvents(Event.ONBLUR);
    }
    
    private boolean isNumber(char character){
        if(numbers.indexOf(character)!=-1){
            return true;
        }
        return false;
    }
    
    @Override
    public boolean complexCheck(){
        boolean localBlurRised=blureRised;
        if(!super.complexCheck()){
            listener.setCorrectnessMark(this,false);
            return false;
        }
        if((localBlurRised)&&(!checkDateCorrectness())){
            listener.setCorrectnessMark(this,false);
            return false;
        }
        listener.setCorrectnessMark(this,true);
        return true;
    }
    
    @Override
    protected boolean checkFillness(){
        if(getValue().length()!=4){
            showNote("Год должен состоять из 4 цифр");
            notFilledEnoughRised=true;
            dateIncorrectRised=true;
            return false;
        }
        return true;
    }
    
    private boolean checkDateCorrectness(){
        setToSensetiveCheckingMode();
        int year=Integer.parseInt(getValue());
        int month=Integer.parseInt(monthElement.getValue(monthElement.getSelectedIndex()));
        if((year<currentYear)||
                ((year==currentYear)&&(currentMonth>=month))){
            showNote("Срок действия данной карты истёк");
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkConsistance() {
        String value=getValue();
        for(int i=0;i<value.length();i++){
            if(!isNumber(value.toCharArray()[i])){
                    showNote("В этом поле должны быть только цифры");
                    return false;
            }
        }
        if(!super.checkConsistance())
            return false;
        if(dateIncorrectRised&&(!checkDateCorrectness()))
            return false;
        hideNote();
        return true;
    }
    
    private void insertValue(String characters, Event event){
        int cursorPos=getCursorPos();
        String value=getValue();
        int selLength=getSelectionLength();
        if(selLength!=0){
            value=removeSubStr(value,cursorPos,selLength);
	}
        value=addSubStr(value,cursorPos,characters);
	setValue(value);
	int newCursorPos=cursorPos+characters.length();
	setCursorPos(newCursorPos);
        event.preventDefault();
    }

    @Override
    protected boolean checkLength(int valueLength) {
        if(valueLength>4){
            Window.alert("Длина поля достигла максимального занчения");
            return false;
        }
        return true;  
    }

    @Override
    protected void handleKeypress(Event event) {
        inputRised=true;
        char character=this.getCharFromEvent(event).toCharArray()[0];
        
        if(getKeyCode(event)==13){
            return;
        }
        
        if((int)character==0){
            complexCheck();
            return;
        }
        
        if((getSelectionLength()==0)&&(!checkLength(getValue().length()+1))){
            event.preventDefault(); 
            return;
        }
        if(!event.getAltKey())
                insertValue(Character.toString(character), event);
        
        complexCheck();
    }

    @Override
    protected void handlePaste(Event event){
        String clipboard=this.getClipboardData(event);
        if(!checkLength(getValue().length()+clipboard.length())){
            event.preventDefault(); 
            return;
        }
        insertValue(clipboard, event);
        complexCheck();
    }

    @Override
    protected void deletingWithCursorPosSetting(){
        int currCursorPos=getCursorPos();
        setValue(removeSubStr(getValue(), getCursorPos(), getSelectionLength()));
        setCursorPos(currCursorPos);
    }
    
    @Override
    protected native void addDeleteFromContextMenuHandler(Element element)/*-{
            var temp = this;
            if(temp.@org.yournamehere.client.CardYearTextBox::inputRised){
                temp.@org.yournamehere.client.CardYearTextBox::inputRised=false;
                return;
            }
            element.oninput=function(event){
                temp.@org.yournamehere.client.CardYearTextBox::deletingWithCursorPosSetting()();
                temp.@org.yournamehere.client.CardYearTextBox::complexCheck()();
            }
    }-*/;
    
    @Override
    protected native void addCutHandler(Element element)/*-{
            var temp = this;
            element.oncut = function(e) {
                temp.@org.yournamehere.client.CardYearTextBox::handleCut()();
            }
    }-*/;
}
