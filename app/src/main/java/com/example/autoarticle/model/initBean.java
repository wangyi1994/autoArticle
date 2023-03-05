package com.example.autoarticle.model;

import java.util.List;

public class initBean {
    private List<String> default_characters;

    private List<String> default_scenarios;

    private SpeechApi speech_api;

    public List<String> getDefault_characters() {
        return default_characters;
    }

    public void setDefault_characters(List<String> default_characters) {
        this.default_characters = default_characters;
    }

    public List<String> getDefault_scenarios() {
        return default_scenarios;
    }

    public void setDefault_scenarios(List<String> default_scenarios) {
        this.default_scenarios = default_scenarios;
    }

    public SpeechApi getSpeech_api() {
        return speech_api;
    }

    public void setSpeech_api(SpeechApi speech_api) {
        this.speech_api = speech_api;
    }
}
