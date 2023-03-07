package com.example.autoarticle.model;

import java.util.List;

/**
 * @author wy
 * @date 2023/2/16
 * description:
 */

public class CreateResult {
    private int conversation_id;
    private List<ChatMessage> greetings;

    private character character;

    private scenario scenario;

    public List<ChatMessage> getGreetings() {
        return greetings;
    }

    public void setGreetings(List<ChatMessage> greetings) {
        this.greetings = greetings;
    }

    public com.example.autoarticle.model.character getCharacter() {
        return character;
    }

    public void setCharacter(com.example.autoarticle.model.character character) {
        this.character = character;
    }

    public com.example.autoarticle.model.scenario getScenario() {
        return scenario;
    }

    public void setScenario(com.example.autoarticle.model.scenario scenario) {
        this.scenario = scenario;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }
}
