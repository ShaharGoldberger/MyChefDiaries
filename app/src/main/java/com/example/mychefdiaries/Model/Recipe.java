package com.example.mychefdiaries.Model;

public class Recipe {

    public String id;
    private String name;
    private String ingredients;
    private String image;
    private User createdUser;
    private String minutes;
    private String category;
    public Recipe() {
    }

    public Recipe(String id, String name, String ingredients, String image, User createdUser, String minutes, String category) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.image = image;
        this.createdUser = createdUser;
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

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public String getTitle() {
        return name;
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
}