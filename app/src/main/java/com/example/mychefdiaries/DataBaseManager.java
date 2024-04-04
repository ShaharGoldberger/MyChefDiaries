package com.example.mychefdiaries;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataBaseManager {

    private static final String USERS = "users";
    private static final String RECIPES = "recipes";

    public static void saveUser(User user, OnCompleteListener<Void> listener) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.getUserId())
                .set(user)
                .addOnCompleteListener(listener);
    }

    public static void saveRecipes(Recipe recipe, User user, OnCompleteListener<Void> listener) {
        FirebaseFirestore.getInstance()
                .collection(RECIPES)
                .document(recipe.getId()) // Ensure recipe.getId() is not null
                .set(recipe)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Optionally, save user here if that's required logic
                        saveUser(user, listener); // Pass listener here to chain completion
                    } else if (listener != null) {
                        listener.onComplete(task); // Notify if saving recipe failed
                    }
                });
    }


    public static String getRecipesCollectionName() {
        return RECIPES;
    }
}
