package com.example.kunal.socialapp;

public class Comment {

    private String userName;
    private String userComment;

    public Comment(String userName, String userComment) {
        this.userName = userName;
        this.userComment = userComment;
    }

    public Comment() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
