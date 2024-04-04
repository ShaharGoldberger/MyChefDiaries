package com.example.mychefdiaries.Model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mychefdiaries.R;

public class RecipeViewActivity extends AppCompatActivity {

    private TextView namePreview, descriptionPreview, ingredientsPreview, durationPreview, categoryPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        namePreview = findViewById(R.id.namePreview);
        descriptionPreview = findViewById(R.id.descriptionPreview);
        ingredientsPreview = findViewById(R.id.ingredientsPreview);
        durationPreview = findViewById(R.id.duration);
        categoryPreview = findViewById(R.id.categoryPreview);

        //get intent "putExtra" keys, for displaying each recipe's details
        // Get intent extras
        Intent intent = getIntent();

        // Safe retrieval and setting of intent extras
        if (intent != null) {
            namePreview.setText(intent.getStringExtra("nameKey") != null ? intent.getStringExtra("nameKey") : "N/A");
            descriptionPreview.setText(intent.getStringExtra("descriptionKey") != null ? intent.getStringExtra("descriptionKey") : "N/A");
            ingredientsPreview.setText(intent.getStringExtra("ingredientsKey") != null ? intent.getStringExtra("ingredientsKey") : "N/A");
            durationPreview.setText(intent.getStringExtra("duration") != null ? intent.getStringExtra("duration") : "N/A"); // Corrected to match intent extra key for duration
            categoryPreview.setText(intent.getStringExtra("categoryKey") != null ? intent.getStringExtra("categoryKey") : "N/A");
        }
    }
}
