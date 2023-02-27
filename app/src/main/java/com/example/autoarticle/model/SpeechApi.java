package com.example.autoarticle.model;

public  class SpeechApi{
    private String  service;
    private String service_region;
    private String speech_key;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService_region() {
        return service_region;
    }

    public void setService_region(String service_region) {
        this.service_region = service_region;
    }

    public String getSpeech_key() {
        return speech_key;
    }

    public void setSpeech_key(String speech_key) {
        this.speech_key = speech_key;
    }
}
