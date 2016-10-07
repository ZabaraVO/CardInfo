/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

/**
 *
 * @author Vlad
 */
public class CardCvcTextBox extends CardInfoTextBox {
    private boolean inputRised=true;
    private String numbers="0123456789";

    public CardCvcTextBox(String elementId, String noteElementName) {
        super(elementId, noteElementName);
    }
    
    private boolean isNumber(char character){
        if(numbers.indexOf(character)!=-1){
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkFillness() {
        if(getValue().length()!=3){
            showNote("CVC должен состоять из 3 цифр");
            notFilledEnoughRised=true;
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkLength(int valueLength) {
        if(valueLength>3){
            Window.alert("Длина поля достигла максимального занчения");
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
        hideNote();
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
    protected void deletingWithCursorPosSetting() {
        int currCursorPos=getCursorPos();
        setValue(removeSubStr(getValue(), getCursorPos(), getSelectionLength()));
        setCursorPos(currCursorPos);
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
    protected native void addDeleteFromContextMenuHandler(Element element)/*-{
            var temp = this;
            if(temp.@org.yournamehere.client.CardCvcTextBox::inputRised){
                temp.@org.yournamehere.client.CardCvcTextBox::inputRised=false;
                return;
            }
            element.oninput=function(event){
                temp.@org.yournamehere.client.CardCvcTextBox::deletingWithCursorPosSetting()();
                temp.@org.yournamehere.client.CardCvcTextBox::complexCheck()();
            }
    }-*/;
    
    @Override
    protected native void addCutHandler(Element element)/*-{
            var temp = this;
            element.oncut = function(e) {
                temp.@org.yournamehere.client.CardCvcTextBox::handleCut()();
            }
    }-*/;
}
