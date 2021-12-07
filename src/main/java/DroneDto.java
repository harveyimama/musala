package com.musala.harvey.drone;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class DroneDto {

    @Size(max = 100)
    private  String serialNumber;
    private  DroneModel model;
    @Min(0)
    @Max(100)
    private int batteryCapacity;
    
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public DroneModel getModel() {
        return model;
    }
    public void setModel(DroneModel model) {
        this.model = model;
    }
    public int getBatteryCapacity() {
        return batteryCapacity;
    }
    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
   

    
    
}

