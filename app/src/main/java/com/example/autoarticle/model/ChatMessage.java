package com.example.autoarticle.model;

import java.io.Serializable;

/**
 * @CreateDate: 2023/02
 * @Author: ey
 * @Description:
 * @Version:
 */
public class ChatMessage implements Serializable {

    private String correctMsg;

    private String filePath;

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

    /**
     * 该段对话的角色
     */
    private String role;
    /**
     * 该段对话的内容
     */
    private String text;

    private String datetime;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
