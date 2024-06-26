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

import com.example.mychefdiaries.Model.Category;
import com.example.mychefdiaries.R;
import com.example.mychefdiaries.Acitivites.CategoryRecipesActivity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecipeViewHolder> {
    private ArrayList<Category> categories;
    private Context context;


    //constructor with the arrayList of recipes attribute
    public CategoryAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Connecting the viewHolder to the proper layout (single_row_recipe layout)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        //sending recipe's information to the holder, so it will appear in the single row view
        Category category = categories.get(position);

        holder.titleTV.setText(categories.get(position).getType().toString());
        holder.image.setImageResource(categories.get(position).getIcon());

    }

    @Override
    public int getItemCount() {
        //returning the size of the arrayList
        return categories.size();
    }



    //creating class RecipeViewHolder, for displaying the recipes in the recycler view
    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        //each single row will contain the recipe name and recipe description
        TextView titleTV;
        ImageView image;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            //setting on click which will open the recipe's view
            titleTV = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Category clickedCategory = categories.get(position);
                        Intent intent = new Intent(context, CategoryRecipesActivity.class);
                        intent.putExtra("category", clickedCategory.getType().toString());
                        context.startActivity(intent);
                    }
                }
            });


        }
    }
}
