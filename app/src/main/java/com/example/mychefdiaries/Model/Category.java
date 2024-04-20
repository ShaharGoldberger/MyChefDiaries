package com.example.mychefdiaries.Model;

import androidx.annotation.DrawableRes;

public class Category {

    @DrawableRes int icon;
    private CategoryType type;

    public Category(int icon, CategoryType type) {
        this.icon = icon;
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public CategoryType getType() {
        return type;
    }
}
