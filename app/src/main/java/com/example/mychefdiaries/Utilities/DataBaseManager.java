package com.example.mychefdiaries.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mychefdiaries.Model.CategoryType;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBaseManager {

    private static final String USERS = "users";
    private static final String RECIPES = "recipes";
    //private static final String FAVORITES = "favorites";

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

    public static void getRecipesByUserId(String uid, OnSuccessListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(uid)
                .collection(RECIPES)
                .get()
                .addOnSuccessListener(listener);
    }

    public static void getRecipesByCategory(CategoryType categoryType,  OnSuccessListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance().collection(RECIPES)
                .whereEqualTo("category", categoryType)
                .get()
                .addOnSuccessListener(listener);
    }

    public static void addToFavorites(Recipe recipe, OnSuccessListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance().collection(RECIPES)
                .document(recipe.getId())
                .set(recipe);
    }


    public static void getUserById(String uid, OnSuccessListener<DocumentSnapshot> listener) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(uid)
                .get()
                .addOnSuccessListener(listener);
    }




}
