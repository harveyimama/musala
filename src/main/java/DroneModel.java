package com.musala.harvey.drone;

public enum DroneModel {
    Lightweight("Lightweight"), Middleweight("Middleweight"), 
    Cruiserweight("Cruiserweight"), Heavyweight("Heavyweight") ;

    private String val;
    private DroneModel(String val)
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
