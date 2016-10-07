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
public class CardholderTextBox extends CardInfoTextBox{
    private String rusLower="абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private String rusUpper="АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private String engLower="abcdefghijklmnopqrstuvwxyz";
    private String engUpper="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String symbols=". ";
    String[] vocabs = { rusLower,rusUpper,engLower,engUpper,symbols };
    String[] translit = { "A","B","V","G","D","E","YO","ZH","Z","I","J","K","L","M","N","O","P","R","S","T","U","F","H","TS","CH","SH","SH","","Y","","E","JU","YA" };
    
    //private Element noteElement;
    private boolean inputRised;
    
    public CardholderTextBox(String elementId, String noteElementName){
        super(elementId,noteElementName);
    }
    
    private boolean isLetter(char character){
        for(int i=0;i<vocabs.length;i++){
            if(vocabs[i].indexOf(character)!=-1)
                return true;
        }
        return false;
    }
    
    private String rusToEng(String str){
        String result="";
        for(int i=0;i<str.length();i++){
            int posInRusUpper=rusUpper.indexOf(str.toCharArray()[i]);
            if(posInRusUpper!=-1)
                    result+=translit[posInRusUpper];
            else
                    result+=str.toCharArray()[i];
        }
        return result;
    }
    
    private void cardholderToUpper(String characters, Event event){
        event.preventDefault();
        String newCharacters=characters.toUpperCase();
        newCharacters=rusToEng(newCharacters);
        int cursorPos=getCursorPos();
        String value=getValue();
        int selLength=getSelectionLength();
        if(selLength!=0){
            value=removeSubStr(value,cursorPos,selLength);
	}
        value=addSubStr(value,cursorPos,newCharacters);
        if(!checkLength(value.length())){
            return;
        }
	setValue(value);
	int newCursorPos=cursorPos+newCharacters.length();
	setCursorPos(newCursorPos); 
    }
    
    @Override
    protected boolean checkFillness(){
        String valueToCheck=this.removeExcessSpaces(getValue());
        if(valueToCheck.length()==0){
            showNote("Данные не заполнены");
            notFilledEnoughRised=true;
            return false;
        }
        return true;
    }
    
    @Override
    protected void deletingWithCursorPosSetting(){
        int currCursorPos=getCursorPos();
        setValue(removeSubStr(getValue(), getCursorPos(), getSelectionLength()));
        setCursorPos(currCursorPos);
    }
    
    @Override
    protected boolean checkLength(int valueLength){
        if(valueLength>25){
            Window.alert("Длина поля достигла максимального занчения");
            return false;
        }
        return true;  
    }
            
    @Override
    protected void handleKeypress(Event event){
        inputRised=true;
        char character=this.getCharFromEvent(event).toCharArray()[0];
        
        if((int)character==0){
                complexCheck();
                return;
        }
        
        if(isLetter(character)){
            cardholderToUpper(Character.toString(character),event);
        }
        else{
            showNote("Доступные символы: точка, A-Z");
        }
        
        complexCheck();
    }
    
    @Override
    protected void handlePaste(Event event){
        String clipboard=this.getClipboardData(event);
        cardholderToUpper(clipboard,event);
        complexCheck();
    }
    
    @Override
    protected boolean checkConsistance(){
        String value=getValue();
        for(int i=0;i<value.length();i++){
            if(!isLetter(value.toCharArray()[i])){
                    showNote("Доступные символы: точка, A-Z");
                    return false;
            }
            if(value.toCharArray()[i]=='.'){
                if((i==0)||(/*i!=0&&*/value.toCharArray()[i-1]==' ')){
                        showNote("Символ '.' может стоять только после буквы");
                        return false;
                }
            }
        }
        if(!super.checkConsistance())
            return false;
        hideNote();
        return true;
    }
    
    public String geValueWithoutExcessSpaces(){
        return removeExcessSpaces(getValue());
    }
    
    private native String removeExcessSpaces(String str)/*-{
            var re = new RegExp("\s+", "g");
            var str = str.replace(/\s{2,}/g, ' ');
            if(str[0]==' ')
                str=str.substring(1,str.length);
            if(str[str.length-1]==' ')
                str=str.substring(0,str.length-1);
            return str;
    }-*/;
    
    @Override
    protected native void addDeleteFromContextMenuHandler(Element element)/*-{
            var temp = this;
            if(temp.@org.yournamehere.client.CardholderTextBox::inputRised){
                temp.@org.yournamehere.client.CardholderTextBox::inputRised=false;
                return;
            }
            element.oninput=function(event){
                temp.@org.yournamehere.client.CardholderTextBox::deletingWithCursorPosSetting()();
                temp.@org.yournamehere.client.CardholderTextBox::complexCheck()();
            }
    }-*/;
    
    @Override
    protected native void addCutHandler(Element element)/*-{
            var temp = this;
            element.oncut = function(e) {
                temp.@org.yournamehere.client.CardholderTextBox::handleCut()();
            }
    }-*/;
}
