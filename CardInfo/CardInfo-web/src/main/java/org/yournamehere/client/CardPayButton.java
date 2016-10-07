/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Vlad
 */
public class CardPayButton extends Button implements IListener{
    private boolean numberIsReady=false;
    private boolean dateIsReady=false;
    private boolean cvcIsReady=false;
    private boolean cardholderIsReady=false;
    
    private CardNumberTextBox number;
    private CardMonthListBox month;
    private CardYearTextBox year;
    private CardCvcTextBox cvc;
    private CardholderTextBox name;

    public CardPayButton() {
        name=new CardholderTextBox("cardholder", "cardholderNote");
        name.setListener(this);
        RootPanel.get("cardholderContainer").add(name);
        
        number=new CardNumberTextBox("cardNumber", "cardNumberNote");
        number.setListener(this);
        RootPanel.get("cardNumberContainer").add(number);
        
        cvc=new CardCvcTextBox("cvc","cvcNote");
        cvc.setListener(this);
        RootPanel.get("cvcContainer").add(cvc);
        
        year=new CardYearTextBox("validThruYear","monthYearNote");
        year.setListener(this);
        RootPanel.get("validThruYearContainer").add(year);
        
        month=year.monthElement;
        
        getElement().setId("pay");
        this.setHTML("Оплатить");
        setEnabled(false);
        
        addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent event) {
                Window.alert("Данные карты отправлены:\n"+
                        "Номер: "+number.getVaueWithoutSpaces()+"\n"+
                        "Срок действия: "+month.getValue(month.getSelectedIndex())+"/"+year.getValue()+"\n"+
                        "CVC: "+cvc.getValue()+"\n"+
                        "Владелец: "+name.geValueWithoutExcessSpaces()
                        );
                number.setValue("");
                month.setSelectedIndex(0);
                year.setValue("");
                cvc.setValue("");
                name.setValue("");
                setEnabled(false);
            }
        });
    }
    
    @Override
    public void setCorrectnessMark(IControllToListen controllToListen, boolean value){
        if(controllToListen instanceof CardNumberTextBox){
            numberIsReady=value;
        }
        if(controllToListen instanceof CardYearTextBox){
            dateIsReady=value;
        }
        if(controllToListen instanceof CardCvcTextBox){
            cvcIsReady=value;
        }
        if(controllToListen instanceof CardholderTextBox){
            cardholderIsReady=value;
        }
        if (numberIsReady&&dateIsReady&&cvcIsReady&&cardholderIsReady){
            setEnabled(true);
            return;
        }
        setEnabled(false);
    }
}
