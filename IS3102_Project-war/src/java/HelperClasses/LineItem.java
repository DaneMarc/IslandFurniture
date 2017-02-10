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
public class LineItem {
    public LineItem(){};
    public LineItem(long srid, long ieid, int q, long ci, String s){
        salesRecordId = srid;
        itemEntityId = ieid;
        quantity = q;
        countryId = ci;
        sku = s;
    }
    
    private long salesRecordId;
    private long itemEntityId;
    private int quantity;
    private long countryId;
    private String sku;
    
    public String getSku(){
        return sku;
    }
    public long getSalesRecordId(){
        return salesRecordId;
    }
    public long getItemEntityId(){
        return itemEntityId;
    }
    public int getQuantity(){
        return quantity;
    }
    public long getCountryId(){
        return countryId;
    }
    
    public void setSku(String s){
        sku = s;
    }
    public void setSalesRecordId(long srid){
        salesRecordId = srid;
    }
    public void setItemEntityId(long ieid){
        itemEntityId = ieid;
    }
    public void setQuantity(int q){
        quantity = q;
    }
    public void setCountryId(long ci){
        countryId = ci;
    }
}
