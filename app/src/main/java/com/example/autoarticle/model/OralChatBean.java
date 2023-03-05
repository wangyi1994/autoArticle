package com.example.autoarticle.model;

import java.util.List;

/**
 * @author wy
 * @date 2023/2/15
 * description:
 */

public class OralChatBean {
    private int conversation_id;
    private User user;

    private String ai_level;

    private String ai_speed;
    private String scenario;

    private String character;

    private List<talkBean> messages;

    /**
     * 是否开启提示模式
     */
    private String inspire_me;

    /**
     * 是否开启纠错模式
     * 默认为true
     */
    private String use_corrector="true";

    private String generate_ai_audio="false";


    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAi_level() {
        return ai_level;
    }

    public void setAi_level(String ai_level) {
        this.ai_level = ai_level;
    }

    public String getAi_speed() {
        return ai_speed;
    }

    public void setAi_speed(String ai_speed) {
        this.ai_speed = ai_speed;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }


    public List<talkBean> getMessages() {
        return messages;
    }

    public void setMessages(List<talkBean> messages) {
        this.messages = messages;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getInspire_me() {
        return inspire_me;
    }

    public void setInspire_me(String inspire_me) {
        this.inspire_me = inspire_me;
    }

    public String getUse_corrector() {
        return use_corrector;
    }

    public void setUse_corrector(String use_corrector) {
        this.use_corrector = use_corrector;
    }

    public String getGenerate_ai_audio() {
        return generate_ai_audio;
    }

    public void setGenerate_ai_audio(String generate_ai_audio) {
        this.generate_ai_audio = generate_ai_audio;
    }
}
