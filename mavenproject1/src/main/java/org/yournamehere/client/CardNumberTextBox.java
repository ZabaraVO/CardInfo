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
public class CardNumberTextBox extends CardInfoTextBox{
    private String numbers="0123456789";
    
    //private Element noteElement;
    private boolean inputRised=true;
    
    public CardNumberTextBox(String elementId, String noteElementName) {
        super(elementId,noteElementName);
    }
    
    private boolean isNumber(char character){
        if(numbers.indexOf(character)!=-1){
            return true;
        }
        return false;
    }
    
    private String removeSpaces(String str){
        return str.replaceAll("\\s+","");
    }
    
    private String setSpaces(String str){
        String result="";
        for(int i=0;i<str.length();i++){
            if((i==4)||(i==8)||(i==12))
                result+=" ";
            result+=str.toCharArray()[i];
        }
        return result;
    }
    
    private int getCursorPoseIfNoSpaces(int pos){
        int result=pos;
        if(pos>4){
            result--;
        }
        if(pos>9){
            result--;
        }
        if(pos>14){
            result--;
        }
        return result;
    }
    
    @Override
    protected boolean checkFillness(){
        if(getValue().length()!=19){
            showNote("Номер карты должен состоять из 16 цифр");
            notFilledEnoughRised=true;
            return false;
        }
        return true;
    }
    
    @Override
    protected boolean checkConsistance() {
        String currValue=getValue();
        String noSpacesValue=removeSpaces(currValue);
        for(int i=0;i<noSpacesValue.length();i++){
            if (!isNumber(noSpacesValue.toCharArray()[i])){
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
    protected boolean checkLength(int valueLength){
        if(valueLength>19){
            Window.alert("Длина поля достигла максимального занчения");
            return false;
        }
        return true;
    }
    
    @Override
    protected void deletingWithCursorPosSetting(){
        int currCursorPos=getCursorPos();
        setValue(removeSubStr(getValue(), currCursorPos, getSelectionLength()));
        String noSpacesValue=removeSpaces(getValue());
        setValue(setSpaces(noSpacesValue));
        setCursorPos(currCursorPos);
        inputRised=true;
    }
    
    @Override
    protected void handleKeypress(Event event){
        char character=this.getCharFromEvent(event).toCharArray()[0];
        
        if(character==' '){
            event.preventDefault();
            return;
        }
        
        int keyCode=getKeyCode(event);
        
        int currCursorPos=getCursorPos();
        
        if( (getSelectionLength()>0) &&
                (((int)character!=0)||(keyCode==8)||(keyCode==46))
                ){
            deletingWithCursorPosSetting();
            event.preventDefault();
        }
        else if((keyCode==8)||(keyCode==46)){
            int charsNumberToRemove;
            switch(keyCode){
                case 8:
                    if(currCursorPos==0){
                        break;
                    }
                    charsNumberToRemove=1;
                    char charBeforeCursor=getValue().toCharArray()[currCursorPos-1];
                    if(charBeforeCursor==' '){
                        charsNumberToRemove=2;
                    }
                    setValue(removeSubStr(getValue(),currCursorPos-charsNumberToRemove,charsNumberToRemove));
                    currCursorPos-=charsNumberToRemove;
                    int i;
                    i=0;
                    break;
                case 46:
                    if(currCursorPos==getValue().length()){
                        break;
                    }
                    charsNumberToRemove=1;
                    char charAfterCursor=getValue().toCharArray()[currCursorPos];
                    if(charAfterCursor==' '){
                        charsNumberToRemove=2;
                    }
                    setValue(removeSubStr(getValue(),currCursorPos,charsNumberToRemove));
                    break;
            }
            String noSpacesValue=removeSpaces(getValue());
            setValue(setSpaces(noSpacesValue));
            setCursorPos(currCursorPos);
            event.preventDefault();
        }
        
        if((int)character==0){
                complexCheck();
                return;
        }
        
        if(event.getAltKey()){
            return;
        }
        
        if(!checkLength(getValue().length()+1)){
            event.preventDefault(); 
            return;
        }
        
        String noSpacesValue=removeSpaces(getValue());
        noSpacesValue=addSubStr(noSpacesValue,getCursorPoseIfNoSpaces(currCursorPos),Character.toString(character));
        setValue(setSpaces(noSpacesValue));
        
        int newCursorPos=currCursorPos;
        
        if((getValue().toCharArray()[currCursorPos]==' ')&&(currCursorPos>3)){
            newCursorPos++;
        }
        
        setCursorPos(newCursorPos+1);
        
        event.preventDefault();        
        complexCheck();
    }   
    
    public String getVaueWithoutSpaces(){
        String result="";
        for(int i=0;i<getValue().length();i++){
            char ch=getValue().toCharArray()[i];
            if(ch!=' ')
                result+=Character.toString(ch);
        }
        return result;
    }
    
    @Override
    protected void handlePaste(Event event){
        String pastedValue=removeSpaces(getClipboardData(event));
        
        int currCursorPos=getCursorPos();
        String value=getValue();
        int selLength=getSelectionLength();
        if(selLength!=0){
		value=removeSubStr(value,currCursorPos,selLength);
	}
        value=addSubStr(value,currCursorPos,pastedValue);
        String noSpacesValue=removeSpaces(value);
        String newValue=setSpaces(noSpacesValue);
        
        if(!checkLength(newValue.length())){
            event.preventDefault(); 
            return;
        }
        
        setValue(newValue);
        
        int incrCursorPosVlue=0;
        if((4>currCursorPos)&&(4<currCursorPos+pastedValue.length()))
            incrCursorPosVlue++;
        if((8>currCursorPos)&&(8<currCursorPos+pastedValue.length()))
            incrCursorPosVlue++;
        if((12>currCursorPos)&&(12<currCursorPos+pastedValue.length()))
            incrCursorPosVlue++;
        setCursorPos(currCursorPos+pastedValue.length()+incrCursorPosVlue);
        
        event.preventDefault();
        complexCheck();
    }
    
    @Override
    protected native void addDeleteFromContextMenuHandler(Element element)/*-{
            var temp = this;
            if(temp.@org.yournamehere.client.CardNumberTextBox::inputRised){
                temp.@org.yournamehere.client.CardNumberTextBox::inputRised=false;
                return;
            }
            element.oninput=function(event){
                temp.@org.yournamehere.client.CardNumberTextBox::deletingWithCursorPosSetting()();
                temp.@org.yournamehere.client.CardNumberTextBox::complexCheck()();
            }
    }-*/;
    
    @Override
    protected native void addCutHandler(Element element)/*-{
            var temp = this;
            element.oncut = function(e) {
                temp.@org.yournamehere.client.CardNumberTextBox::handleCut()();
            }
    }-*/;
}
