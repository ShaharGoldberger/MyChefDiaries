package com.example.mychefdiaries.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Recipe implements Serializable {

    public String id;
    private String name;
    private String ingredients;
    private String image;
    private String createdUserId;
    private String minutes;
    private String category;
    private ArrayList<String> likedByUsers;
    private HashMap<String, String> favoritedBy = new HashMap<>();
    private boolean isFavorite = false;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public HashMap<String, String> getFavoritedBy() {
        return favoritedBy;
    }

    public Recipe() {

    }

    public Recipe(String id, String name, String ingredients, String image, String createdUserId, String minutes, String category) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.image = image;
        this.createdUserId = createdUserId;
        this.minutes = minutes;
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return ingredients;
    }


    public void setTitle(String title) {
        this.name = title;
    }

    public void setText(String text) {
        this.ingredients = text;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(ArrayList<String> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

    public void setFavoritedBy(HashMap<String, String> favoritedBy) {
        this.favoritedBy = favoritedBy;
    }
}
