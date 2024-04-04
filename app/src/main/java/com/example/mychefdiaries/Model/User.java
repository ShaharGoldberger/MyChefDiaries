package com.example.mychefdiaries.Model;

import android.net.Uri;

import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URI;
import java.util.ArrayList;

public class User {

    private String userId;
    private String email;
    private String imageUrl;
    private ArrayList<String> recipesIds = new ArrayList<>();


    public User() {
    }

    public User(String userId, String email, String imageUrl) {
        this.userId = userId;
        this.email = email;
        this.imageUrl = imageUrl;
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


    // Method to add a recipe ID to the user's list of recipes
    public void addRecipeId(String recipeId) {
        if (this.recipesIds == null) {
            this.recipesIds = new ArrayList<>();
        }
        if (!this.recipesIds.contains(recipeId)) {
            this.recipesIds.add(recipeId);

            // Update Firestore with the new list
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(this.userId)
                    .update("recipesIds", this.recipesIds);
        }
    }
}
