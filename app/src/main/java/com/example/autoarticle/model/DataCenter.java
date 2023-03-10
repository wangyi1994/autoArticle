package com.example.autoarticle.model;

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCenter {
    private volatile static DataCenter dataCenter;

    private DataCenter() {
    }

    /**
     * 单例模式
     */
    public static DataCenter getInstance() {
        if (dataCenter == null) {
            synchronized (DataCenter.class) {
                if (dataCenter == null) {
                    dataCenter = new DataCenter();
                }
            }
        }
        return dataCenter;
    }

    private initBean initBean;

    private User user;
    private List<conversation> conversations;

    public com.example.autoarticle.model.initBean getInitBean() {
        return initBean;
    }

    public void setInitBean(com.example.autoarticle.model.initBean initBean) {
        this.initBean = initBean;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<conversation> conversations) {
        this.conversations = conversations;
    }
}
