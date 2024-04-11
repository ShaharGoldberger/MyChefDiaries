package com.example.mychefdiaries.Model;

import android.net.Uri;

import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URI;
import java.util.ArrayList;

public class User {

    private String userId;
    private String email;
    private String imageUrl;

    private String fullName;
    private ArrayList<String> recipesIds = new ArrayList<>();


    public User() {
    }

    public User(String userId, String email,String fullName, String imageUrl) {
        this.userId = userId;
        this.email = email;
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.recipesIds = new ArrayList<>();
    }



    public ArrayList<String> getRecipesIds() {
        return recipesIds;
    }

    public void setRecipesIds(ArrayList<String> recipesIds) {
        this.recipesIds = recipesIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
