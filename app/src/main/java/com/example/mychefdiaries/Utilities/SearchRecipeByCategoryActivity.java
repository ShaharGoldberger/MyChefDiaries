package com.example.mychefdiaries.Utilities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychefdiaries.Adapters.CategoryAdapter;
import com.example.mychefdiaries.Model.Category;
import com.example.mychefdiaries.Model.CategoryType;
import com.example.mychefdiaries.R;

import java.util.ArrayList;

public class SearchRecipeByCategoryActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    private ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_recipe_by_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView list = findViewById(R.id.list);
        categories.add(new Category(R.drawable.beef, CategoryType.BEEF));
        adapter = new CategoryAdapter(categories);
        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(this, 2));



    }

    private void setupCardClickListener(CardView cardView, String category) {
        cardView.setOnClickListener(view -> navigateToCategoryRecipes(category));
    }

    private void navigateToCategoryRecipes(String category) {
        Intent intent = new Intent(this, CategoryRecipesActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}