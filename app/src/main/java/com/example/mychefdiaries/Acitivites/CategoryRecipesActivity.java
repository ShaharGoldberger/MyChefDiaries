package com.example.mychefdiaries.Acitivites;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychefdiaries.Adapters.RecipeAdapter;
import com.example.mychefdiaries.Utilities.DataBaseManager;
import com.example.mychefdiaries.Model.CategoryType;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CategoryRecipesActivity extends AppCompatActivity {

    private CategoryType categoryType = CategoryType.BEEF;
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_recipes);
        categoryType = (CategoryType) CategoryType.valueOf(getIntent().getStringExtra("category"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        //recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        findViews();
        adapter = new RecipeAdapter(recipeList, true);
        recipesRecyclerView.setAdapter(adapter);

        // Fetch category from intent
        fetchRecipesForCategory();

    }

    private void findViews() {
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
    }

        private void fetchRecipesForCategory() {
        DataBaseManager.getRecipesByCategory(categoryType, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.getDocuments().forEach(new Consumer<DocumentSnapshot>() {
                    @Override
                    public void accept(DocumentSnapshot documentSnapshot) {
                        Recipe recipe = documentSnapshot.toObject(Recipe.class);
                        recipe.setFavorite(recipe.getFavoritedBy().containsKey(FirebaseAuth.getInstance().getUid()));
                        recipeList.add(documentSnapshot.toObject(Recipe.class));
                    }
                });
                adapter.notifyDataSetChanged();

            }
        });
    }


}


