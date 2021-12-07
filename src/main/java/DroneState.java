package com.musala.harvey.drone;

public enum DroneState {

    IDLE("IDLE"), LOADING("LOADING"), LOADED("LOADED"), DELIVERING("DELIVERING"),
     DELIVERED("DELIVERED"), RETURNING("RETURNING");

     private String val;

     DroneState(String val)
     {
            this.val = val;
     }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    
     
    
}
