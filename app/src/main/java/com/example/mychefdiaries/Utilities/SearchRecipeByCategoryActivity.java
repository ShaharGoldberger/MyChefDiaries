package com.example.mychefdiaries.Utilities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychefdiaries.R;

public class SearchRecipeByCategoryActivity extends AppCompatActivity {

    private CardView beefCard, dairyCard, fishCard, veganCard, cocktailsCard, dessertsCard;


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

        beefCard = findViewById(R.id.beef_card);
        dairyCard = findViewById(R.id.dairy_card);
        fishCard = findViewById(R.id.fish_card);
        veganCard = findViewById(R.id.vegan_card);
        cocktailsCard = findViewById(R.id.cocktails_card);
        dessertsCard = findViewById(R.id.desserts_card);

        // Setting up click listeners for each CardView
        setupCardClickListener(beefCard, "Beef");
        setupCardClickListener(dairyCard, "Dairy");
        setupCardClickListener(fishCard, "Fish");
        setupCardClickListener(veganCard, "Vegan");
        setupCardClickListener(cocktailsCard, "Cocktails");
        setupCardClickListener(dessertsCard, "Desserts");

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