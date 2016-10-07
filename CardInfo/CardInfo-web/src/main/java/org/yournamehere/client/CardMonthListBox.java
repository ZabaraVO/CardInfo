/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

/**
 *
 * @author Vlad
 */
public class CardMonthListBox extends ListBox {
    private IListener listener;
    private CardYearTextBox cardYear;

    public CardMonthListBox(CardYearTextBox cardYr) {
        super();
        this.cardYear = cardYr;
        getElement().setId("validThruMonth");
        getElement().setClassName("inputTextCenter");
        for(int i=1;i<=12;i++){
            addItem(Integer.toString(i));
        }
        addChangeHandler(new ChangeHandler(){
            @Override
            public void onChange(ChangeEvent event) {
                cardYear.complexCheck();
            }
        });
    }
    
}
