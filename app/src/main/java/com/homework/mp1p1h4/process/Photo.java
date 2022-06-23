package com.homework.mp1p1h4.process;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;

public class Photo {
    private Integer id;
    private String name;
    private String description;
    private String photo;

    public Photo() {}
    public Photo(Integer id, String name, String description, String photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public String getPhoto() { return this.photo; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPhoto(String photo) { this.photo = photo; }
}
