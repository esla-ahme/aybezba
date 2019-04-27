package com.example.mainapp;


public class model {
    private String post;
    private String content;
    private String image;

    public model(String post, String content, String image) {
        this.post = post;
        this.content = content;
        this.image = image;
    }
    public model(String post, String image) {
        this.post = post;
        this.image = image;
    }

    public String getPost() {
        return post;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }
}