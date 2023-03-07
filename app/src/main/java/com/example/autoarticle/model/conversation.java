package com.example.autoarticle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class conversation implements Serializable {
    private int conversation_id;
   private character character;

   private scenario scenario;

   private List<ChatMessage> messages;

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public com.example.autoarticle.model.character getCharacter() {
        return character;
    }

    public void setCharacter(character character) {
        this.character = character;
    }

    public com.example.autoarticle.model.scenario getScenario() {
        return scenario;
    }

    public void setScenario(com.example.autoarticle.model.scenario scenario) {
        this.scenario = scenario;
    }

    public List<ChatMessage> getMessages() {
        if(messages==null){
            return new ArrayList<>();
        }
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
