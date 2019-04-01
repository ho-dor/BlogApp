package com.example.kunal.socialapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable{

    private String image;
    private String title;
    private String desc;
    private boolean isLiked;
    private ArrayList<String> comment;

    public Post(){

    }

    public Post(String image, String title, String desc,boolean isLiked) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.isLiked = isLiked;
        //this.comment = new ArrayList<String>();
    }

    public void addComment(String cmmnt){
        comment.add(cmmnt);
    }

    public void removeComment(){

    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
