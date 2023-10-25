package com.example.l999;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class Competition implements Serializable {
    private String id;
    private String image;
    private String description;
    private String date;
    private String category;
    private Boolean isFinished;
    private Boolean isInscris;
    private Boolean hasToken;
    private Boolean joToken;

    public Competition() {
        // Constructor gol necesar pentru Firebase
    }

    public Competition(String id, String image, String description, String date, String category, Boolean isFinished, Boolean isInscris, Boolean hasToken, Boolean joToken) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.date = date;
        this.category = category;
        this.isFinished= isFinished;
        this.isInscris=isInscris;
        this.hasToken=hasToken;
        this.joToken=joToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Boolean getIsInscris() {
        return isInscris;
    }
    public void setIsInscris(Boolean isInscris) {
        this.isInscris = isInscris;
    }

    public void setHasToken(Boolean hasToken) {
        this.hasToken = hasToken;
    }
    public Boolean hasToken() {
        return hasToken;
    }

    public void setJoToken(Boolean joToken) {
        this.joToken = joToken;
    }
    public Boolean joToken() {
        return joToken;
    }

}