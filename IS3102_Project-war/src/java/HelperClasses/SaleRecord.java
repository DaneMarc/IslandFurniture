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
    public SaleRecord(long m, double ap, long cid){
        memberId = m;
        amountPaid = ap;
        countryId = cid;
    }
    private long memberId;
    private double amountPaid;
    private long countryId;
    
    public long getMemberId(){
    return memberId;
    }
    public double getAmountPaid(){
        return amountPaid;
    }
    public long getCountryId(){
        return countryId;
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

 
}
