package com.example.catalogoanimales;
import java.io.Serializable;
public class Animal implements Serializable {
    private final String name;
    private final int imageResId;
    private final String description;

    public Animal(String name, int imageResId, String description) {
        this.name = name;
        this.imageResId = imageResId;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }
}
