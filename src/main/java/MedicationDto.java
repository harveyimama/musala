package com.musala.harvey.drone;

import javax.validation.constraints.Pattern;

public class MedicationDto {
   
    @Pattern(regexp = "([A-Za-z0-9_-]+)")
    private String name;
    private double weight;
    @Pattern(regexp = "([A-Z0-9_]+)")
    private String code;
    private String image;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    
    
}
