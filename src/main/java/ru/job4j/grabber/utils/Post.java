package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String name;
    private String url;
    private String text;
    private LocalDateTime date;

    public Post() {

    }

    public Post(int id, String name, String url, String text, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}