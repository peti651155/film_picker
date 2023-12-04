package com.example.film_picker;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;

    public Movie(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

