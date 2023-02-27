package com.example.autoarticle.model;

import java.util.List;

/**
 * @author wy
 * @date 2023/2/15
 * description:
 */

public class OralChatBean {
    private String scenario;

    private character character;

    private List<talkBean> conversations;

    /**
     * 是否开启提示模式
     */
    private String inspire_me;

    /**
     * 是否开启纠错模式
     * 默认为true
     */
    private String use_corrector="true";


    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public com.example.autoarticle.model.character getCharacter() {
        return character;
    }

    public void setCharacter(com.example.autoarticle.model.character character) {
        this.character = character;
    }

    public List<talkBean> getConversations() {
        return conversations;
    }

    public void setConversations(List<talkBean> conversations) {
        this.conversations = conversations;
    }

    public String isInspire_me() {
        return inspire_me;
    }

    public void setInspire_me(String inspire_me) {
        this.inspire_me = inspire_me;
    }

    public String isUse_corrector() {
        return use_corrector;
    }

    public void setUse_corrector(String use_corrector) {
        this.use_corrector = use_corrector;
    }
}
