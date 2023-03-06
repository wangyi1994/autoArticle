package com.example.autoarticle.model;

import java.io.Serializable;

/**
 * @CreateDate: 2018/3/9
 * @Author: lzsheng
 * @Description:
 * @Version:
 */
public class UserInfo implements Serializable {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
