package com.uoit.noteme;

public class Note {
    String id;
    String title;
    String subtitle;
    String text;
    String color;
    String image;
    String path;

    public Note() {
    }

    public Note(String id, String title, String subtitle, String text, String color, String image, String path) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
        this.color = color;
        this.image = image;
        this.path = path;
    }

    public Note(String title, String subtitle, String text, String color, String image, String path) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
        this.color = color;
        this.image = image;
        this.path = path;
    }
//    public Note(String id, String title, String subtitle, String text, String color) {
//        this.id = id;
//        this.title = title;
//        this.subtitle = subtitle;
//        this.text = text;
//        this.color = color;
//    }

    public Note(String title, String subtitle, String text, String color) {
        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPath() {return path;}

    public void setPath(String path) {this.path = path;}
}
