/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 *
 * @author Vlad
 */
public abstract class CardInfoTextBox extends TextBox implements IControllToListen{
    protected IListener listener;
    protected boolean inputRised=true;
    protected boolean notFilledEnoughRised=false;
    protected boolean isEmptyRised=false;
    protected boolean blureRised=false;
    protected Element noteElement;
    
    //protected abstract boolean checkConsistance();
    protected abstract boolean checkFillness();
    protected abstract boolean checkLength(int valueLength);
    protected abstract void addCutHandler(Element element);
    protected abstract void addDeleteFromContextMenuHandler(Element element);
    protected abstract void handleKeypress(Event event);
    protected abstract void handlePaste(Event event);
    //protected abstract void handleCut();
    protected abstract void deletingWithCursorPosSetting();
    
    public CardInfoTextBox(String elementId, String noteElementName){
        super();
        getElement().setId(elementId);
        getElement().setClassName("inputTextCenter");
        sinkEvents(Event.ONKEYUP);
        sinkEvents(Event.ONKEYPRESS);
        sinkEvents(Event.ONPASTE);
        sinkEvents(Event.ONBLUR);
        addCutHandler(getElement());
        addDeleteFromContextMenuHandler(getElement());
        noteElement=RootPanel.get(noteElementName).getElement();
    }
    
    public void setListener(IListener listener){
        this.listener=listener;
    }
    
    protected boolean complexCheck(){
        if(!checkConsistance()){
            listener.setCorrectnessMark(this,false);
            return false;
        }
        if(blureRised){
            blureRised=false;
            if(!chekIsEmpty()){
                listener.setCorrectnessMark(this,false);
                return false;
            }
            if(!checkFillness()){
                listener.setCorrectnessMark(this,false);
                return false;
            }
        }
        listener.setCorrectnessMark(this,true);
        return true;
    }
    
    protected boolean checkConsistance(){
        if(isEmptyRised&&(!chekIsEmpty()))
            return false;
        if(notFilledEnoughRised&&(!checkFillness()))
            return false;
        return true;
    }
    
    protected boolean chekIsEmpty(){
        if(getValue().length()==0){
            showNote("Данные не заполнены");
            isEmptyRised=true;
            return false;
        }
        return true;
    }
    
    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (event.getTypeInt()) {
        case Event.ONKEYUP:
            complexCheck();
            break;
        case Event.ONKEYPRESS:
            handleKeypress(event);
            break;
        case Event.ONPASTE:
            handlePaste(event);
            break;
        case Event.ONBLUR:
            blureRised=true;
            complexCheck();
            break;
        }
    }
    
    protected String addSubStr(String s1, int pos, String s2){
        String beforeSubStr = s1.substring(0,pos);
        String afterSubStr = s1.substring(pos,s1.length());
        return beforeSubStr+s2+afterSubStr;
    }
    
    protected String removeSubStr(String s, int pos1, int n){
        String beforePos1 = s.substring(0,pos1);
        String afterPos2 = s.substring(pos1+n,s.length());
        return beforePos1+afterPos2;
    }
    
    protected void showNote(String text){
        noteElement.getParentElement().getStyle().setProperty("backgroundColor", "rgba(255, 0, 0, 0.5)");
        noteElement.setInnerHTML(text);
    }
    
    protected void hideNote(){
        noteElement.getParentElement().getStyle().setProperty("backgroundColor", "");
        noteElement.setInnerHTML("");
    }
    
    protected void handleCut() {
        String selectedText=this.getSelectedText();
        deletingWithCursorPosSetting();
        int currCursorPos=getCursorPos();
        complexCheck();
        setValue(addSubStr(getValue(),currCursorPos,selectedText));
        setSelectionRange(currCursorPos, selectedText.length());
        inputRised=true;
    }
    
    @Override
    public void setCursorPos(int pos){
        setCursPos(getElement(),pos);
    }
        
    protected native void setCursPos(Element el, int pos)/*-{
        el.setSelectionRange(pos, pos);
    }-*/;
    
    protected native String getCharFromEvent(Event event)/*-{
        return String.fromCharCode(event.charCode);
    }-*/;
    
    protected native String getClipboardData(Event event)/*-{
        return event.clipboardData.getData("text/plain");
    }-*/;
    
    protected native int getKeyCode(Event event)/*-{
        return event.keyCode;
    }-*/;
}
