/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperClasses;

/**
 *
 * @author user
 */
public class Currency_Storeid {
    public Currency_Storeid(){}
    public Currency_Storeid(long sid, String c){
        storeId = sid;
        currency = c;
    }
    private long storeId;
    private String currency;
    
    public long getSaleRecordId(){
        return storeId;
    }
    public String getCurrency(){
        return currency;
    }
    public void setSaleRecordId(long sid){
        storeId = sid;
    }
    public void setCurrency(String c){
        currency = c;
    }
}
