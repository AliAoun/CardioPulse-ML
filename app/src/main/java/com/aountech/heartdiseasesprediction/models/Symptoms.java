package com.aountech.heartdiseasesprediction.models;

import java.io.Serializable;

public class Symptoms implements Serializable {
    private String age;
    private String gender;
    private String chestPain;
    private String restingBloodPressure;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChestPain() {
        return chestPain;
    }

    public void setChestPain(String chestPain) {
        this.chestPain = chestPain;
    }

    public String getRestingBloodPressure() {
        return restingBloodPressure;
    }

    public void setRestingBloodPressure(String restingBloodPressure) {
        this.restingBloodPressure = restingBloodPressure;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getFastingBloodSugar() {
        return fastingBloodSugar;
    }

    public void setFastingBloodSugar(String fastingBloodSugar) {
        this.fastingBloodSugar = fastingBloodSugar;
    }

    public String getRestEcg() {
        return restEcg;
    }

    public void setRestEcg(String restEcg) {
        this.restEcg = restEcg;
    }

    public String getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(String maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public String getExerciseInducedAngina() {
        return exerciseInducedAngina;
    }

    public void setExerciseInducedAngina(String exerciseInducedAngina) {
        this.exerciseInducedAngina = exerciseInducedAngina;
    }

    public String getStDepression() {
        return stDepression;
    }

    public void setStDepression(String stDepression) {
        this.stDepression = stDepression;
    }

    public String getStSlope() {
        return stSlope;
    }

    public void setStSlope(String stSlope) {
        this.stSlope = stSlope;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getThal() {
        return thal;
    }

    public void setThal(String thal) {
        this.thal = thal;
    }

    private String cholesterol;
    private String fastingBloodSugar;
    private String restEcg;
    private String maxHeartRate;
    private String exerciseInducedAngina;
    private String stDepression;
    private String stSlope;
    private String ca;
    private String thal;

    public Symptoms(String age, String gender, String chestPain, String restingBloodPressure, String cholesterol, String fastingBloodSugar, String restEcg, String maxHeartRate, String exerciseInducedAngina, String stDepression, String stSlope, String ca, String thal) {
        this.age = age;
        this.gender = gender;
        this.chestPain = chestPain;
        this.restingBloodPressure = restingBloodPressure;
        this.cholesterol = cholesterol;
        this.fastingBloodSugar = fastingBloodSugar;
        this.restEcg = restEcg;
        this.maxHeartRate = maxHeartRate;
        this.exerciseInducedAngina = exerciseInducedAngina;
        this.stDepression = stDepression;
        this.stSlope = stSlope;
        this.ca = ca;
        this.thal = thal;
    }
}
