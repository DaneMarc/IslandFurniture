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
public class SaleRecord {
    public SaleRecord(){}
    public SaleRecord(long m, double ap, long cid, String c, long si){
        memberId = m;
        amountPaid = ap;
        countryId = cid;
        currency = c;
        storeId = si;
    }
    private long memberId;
    private double amountPaid;
    private long countryId;
    private String currency;
    private long storeId;
    
    public long getMemberId(){
    return memberId;
    }
    public double getAmountPaid(){
        return amountPaid;
    }
    public long getCountryId(){
        return countryId;
    }
    public String getCurrency(){
        return currency;
    }
    public long getStoreId(){
        return storeId;
    }
    public void setMemberId(long m){
        memberId = m;
    }
    public void setAmountPaid(double ap){
        amountPaid = ap;
    }
    public void setCountryId(long cid){
        countryId = cid;
    }
    public void setCurrency(String c){
        currency = c;
    }
    public void setStoreId(long si){
        storeId = si;
    }

 
}
