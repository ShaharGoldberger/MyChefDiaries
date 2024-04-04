package com.example.mychefdiaries.Utilities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychefdiaries.Adapters.RecipeAdapter;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CategoryRecipesActivity extends AppCompatActivity {

    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(recipeList);
        recipesRecyclerView.setAdapter(adapter);

        // Fetch category from intent
        String category = getIntent().getStringExtra("category");
        if (category != null) {
            fetchRecipesForCategory(category);
        }
    }

    private void fetchRecipesForCategory(String category) {
        db.collection("recipes")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        recipeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipeList.add(recipe);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("CategoryRecipesActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

}