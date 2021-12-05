package com.musala.harvey.drone;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Drone {
    @Id
    private String id;
    @Indexed(unique=true)
    private String serialNumber;
    private DroneModel model;
    private double weightLimit;
    private double currentLimit;
    private int batteryCapacity;
    private DroneState state;
    private List<MedicationDto> medications;

    public Drone() {
    }

    public Drone(DroneDto dto) {
        this.model = dto.getModel();
        this.serialNumber = dto.getSerialNumber();
        this.weightLimit = 500;
        this.batteryCapacity = dto.getBatteryCapacity();
        this.state = DroneState.IDLE;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public void setModel(final DroneModel model) {
        this.model = model;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(final double weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(final int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(final DroneState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public double getCurrentLimit() {
        return currentLimit;
    }

    public void setCurrentLimit(double currentLimit) {
        this.currentLimit = currentLimit;
    }

    public List<MedicationDto> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationDto> medications) {
        this.medications = medications;
    }

}
