package com.example.autoarticle.model;

import java.io.Serializable;

/**
 * @CreateDate: 2018/3/9
 * @Author: lzsheng
 * @Description:
 * @Version:
 */
public class ChatMessage implements Serializable {

    private character character;
    private int from;
    private String msgContent;

    private String correctMsg;

    private String filePath;

    public com.example.autoarticle.model.character getCharacter() {
        return character;
    }

    public void setCharacter(com.example.autoarticle.model.character character) {
        this.character = character;
    }


    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getCorrectMsg() {
        return correctMsg;
    }

    public void setCorrectMsg(String correctMsg) {
        this.correctMsg = correctMsg;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
