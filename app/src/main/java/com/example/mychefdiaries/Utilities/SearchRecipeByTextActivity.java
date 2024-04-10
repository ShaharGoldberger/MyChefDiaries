package com.example.mychefdiaries.Utilities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychefdiaries.Adapters.RecipeAdapter;
import com.example.mychefdiaries.DataBaseManager;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchRecipeByTextActivity extends AppCompatActivity {

    private EditText ingredientsSearchInput;
    private Button searchButton;

    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private ArrayList<Recipe> filteredRecipe = new ArrayList<>();


    private RecipeAdapter adapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_recipe_by_text);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(filteredRecipe);
        recyclerView.setAdapter(adapter);
        ingredientsSearchInput = findViewById(R.id.ingredientsSearchInput);
        searchButton = findViewById(R.id.searchButton);

        DataBaseManager.getRecipes(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.getDocuments().forEach(new Consumer<DocumentSnapshot>() {
                    @Override
                    public void accept(DocumentSnapshot documentSnapshot) {
                        recipeArrayList.add(documentSnapshot.toObject(Recipe.class));
                    }
                });
            }
        });

        searchButton.setOnClickListener(v -> arrangeData());

    }

    private void arrangeData(){
        filteredRecipe.clear();
        filteredRecipe.addAll(recipeArrayList.stream().filter(new Predicate<Recipe>() {
            @Override
            public boolean test(Recipe recipe) {
                return recipe.getIngredients() != null && recipe.getIngredients().contains(ingredientsSearchInput.getText().toString());
            }
        }).collect(Collectors.toList()));

        adapter.notifyDataSetChanged();
    }
}