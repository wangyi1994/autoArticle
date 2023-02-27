package com.example.autoarticle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class talkListBean implements Serializable {
   private character character;

   private String scene;

   private List<ChatMessage > messages;
   private String msgContent;

    public com.example.autoarticle.model.character getCharacter() {
        return character;
    }

    public void setCharacter(com.example.autoarticle.model.character character) {
        this.character = character;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
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
