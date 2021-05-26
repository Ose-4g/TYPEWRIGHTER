package com.ose4g.typerighter.Models;

import androidx.annotation.Nullable;

public class User {
    private String userId;
    private String userName;
    private Long bestScore;

    public User(){}

    public User(String userId, String userName, Long bestScore) {
        this.userId = userId;
        this.userName = userName;
        this.bestScore = bestScore;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getBestScore() {
        return bestScore;
    }

    public void setBestScore(Long bestScore) {
        this.bestScore = bestScore;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        User user = (User) obj;
        if(user!=null)
        {
            if(user.userId .equals(this.userId))
                return true;
        }
        return false;
    }
}
