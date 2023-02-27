package com.example.autoarticle.model;

public class resultBean {
    private String corrected_user_text;

    /**
     * 普通模式返回的ai文本
     */
    private String ai_text;
    /**
     * 提示模式返回的AI生成提示文本
     */

    private String user_text;
    private boolean is_grammar_correct;


    public String getCorrected_user_text() {
        return corrected_user_text;
    }

    public void setCorrected_user_text(String corrected_user_text) {
        this.corrected_user_text = corrected_user_text;
    }

    public String getAi_text() {
        return ai_text;
    }

    public void setAi_text(String ai_text) {
        this.ai_text = ai_text;
    }

    public boolean isIs_grammar_correct() {
        return is_grammar_correct;
    }

    public void setIs_grammar_correct(boolean is_grammar_correct) {
        this.is_grammar_correct = is_grammar_correct;
    }

    public String getUser_text() {
        return user_text;
    }

    public void setUser_text(String user_text) {
        this.user_text = user_text;
    }
}
