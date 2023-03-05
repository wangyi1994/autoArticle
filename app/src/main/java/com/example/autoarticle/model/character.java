package com.example.autoarticle.model;

import java.io.Serializable;

public  class character implements Serializable {
    int id;
    String name;
    String gender;
    String age;
    String language;
    String nationality;
    String location;
    String level;
    String speech_language;
    String speech_speed;
    String customized_features;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpeech_language() {
        return speech_language;
    }

    public void setSpeech_language(String speech_language) {
        this.speech_language = speech_language;
    }

    public String getSpeech_speed() {
        return speech_speed;
    }

    public void setSpeech_speed(String speech_speed) {
        this.speech_speed = speech_speed;
    }

    public String getCustomized_features() {
        return customized_features;
    }

    public void setCustomized_features(String customized_features) {
        this.customized_features = customized_features;
    }
}
