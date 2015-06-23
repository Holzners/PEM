package com.team3.pem.pem;

public class SwitchSymptom {

    private String name;
//    public Switch mSwitch;
//    public Color color;

    public SwitchSymptom(String name){
        this.name = name;
//        this.mSwitch = mSwitch;
//        this.color = color;
    }

    public void setSymptomName(String symptomName){
        this.name = symptomName;
    }

    public String getSymptomName(){
        return name;
    }
}
