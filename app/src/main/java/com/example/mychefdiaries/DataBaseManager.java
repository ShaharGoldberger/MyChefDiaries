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
                .document(recipe.getId())
                .set(recipe)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            saveUser(user, null);
                        }
                    }
                });
    }
}
