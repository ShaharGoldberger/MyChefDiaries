package com.example.mychefdiaries;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    public static void saveRecipes(Recipe recipe, OnCompleteListener<Void> listener) {
        FirebaseFirestore.getInstance()
                .collection(RECIPES)
                .document(recipe.getId()) // Ensure recipe.getId() is not null
                .set(recipe)
                .addOnCompleteListener(listener);
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(FirebaseAuth.getInstance().getUid())
                .collection(RECIPES)
                .document(recipe.getId())
                .set(recipe);
    }


    public static String getRecipesCollectionName() {
        return RECIPES;
    }

    public static void getRecipes(OnSuccessListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance()
                .collection(RECIPES)
                .get()
                .addOnSuccessListener(listener);
    }
}
