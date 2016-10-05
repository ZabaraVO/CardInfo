/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Main entry point.
 *
 * @author Vlad
 */
public class MainEntryPoint implements EntryPoint {
    /*@UiField
    InputElement cardNumber;*/
    
    int i=0;
    /**
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    public void onModuleLoad() { 
        CardPayButton pay=new CardPayButton();
        RootPanel.get("payContainer").add(pay);        
    }
}
