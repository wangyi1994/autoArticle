package com.example.autoarticle.model;

import java.util.List;

/**
 * @author wy
 * @date 2023/2/16
 * description:
 */

public class CreateResult {
    private int conversation_id;
    private List<talkBean> greetings;

    private String character;

    private String scenario;

    public List<talkBean> getGreetings() {
        return greetings;
    }

    public void setGreetings(List<talkBean> greetings) {
        this.greetings = greetings;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }
}
