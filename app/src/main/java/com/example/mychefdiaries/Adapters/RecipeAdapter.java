package com.example.mychefdiaries.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.Model.RecipeViewActivity;
import com.example.mychefdiaries.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> recipesArray;
    private Context context;


    //constructor with the arrayList of recipes attribute
    public RecipeAdapter(ArrayList<Recipe> recipesArray) {
        this.recipesArray = recipesArray;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connecting the viewHolder to the proper layout (single_row_recipe layout)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_one_row, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        //sending recipe's information to the holder, so it will appear in the single row view
        holder.recipeName.setText(recipesArray.get(position).getName());
        holder.recipeDescription.setText(recipesArray.get(position).getText());
        holder.categoryTV.setText(recipesArray.get(position).getCategory());
        Glide.with(holder.itemView)
                .load(recipesArray.get(position).getImage())
                .placeholder(R.drawable.icon)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        //returning the size of the arrayList
        return recipesArray.size();
    }

    //creating class RecipeViewHolder, for displaying the recipes in the recycler view
    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        //each single row will contain the recipe name and recipe description
        TextView recipeName, recipeDescription, categoryTV;
        ImageView image;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            //setting on click which will open the recipe's view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Sending recipe's information the the RecipeView activity to show the recipe's details (using intent)
                    Intent intent = new Intent(v.getContext(), RecipeViewActivity.class);
                    intent.putExtra("nameKey", recipesArray.get(getAdapterPosition()).getName());
                    intent.putExtra("descriptionKey", recipesArray.get(getAdapterPosition()).getText());
                    intent.putExtra("duration", recipesArray.get(getAdapterPosition()).getMinutes());
                    intent.putExtra("categoryKey", recipesArray.get(getAdapterPosition()).getCategory());

                    String ingredients = recipesArray.get(getAdapterPosition()).getIngredients();

                    intent.putExtra("ingredientsKey", ingredients);
                    v.getContext().startActivity(intent);
                }
            });

            recipeName = itemView.findViewById(R.id.recipeNameRow);
            recipeDescription = itemView.findViewById(R.id.recipeDescriptionRow);
            categoryTV = itemView.findViewById(R.id.type);
            image = itemView.findViewById(R.id.imageSingleRow);
        }
    }
}
