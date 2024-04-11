package com.example.mychefdiaries.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.mychefdiaries.Adapters.RecipeAdapter;
import com.example.mychefdiaries.DataBaseManager;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FavoritesFragment extends Fragment {

    private RecyclerView recipesRecyclerView;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserId;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance(String userId) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        setupRecyclerView(view);
        loadFavoriteRecipes();
        return view;
    }

    private void setupRecyclerView(View view) {
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recipeAdapter = new RecipeAdapter(favoriteRecipes, true, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                favoriteRecipes.remove(position);
                recipeAdapter.notifyItemRemoved(position);
            }
        });
        recipesRecyclerView.setAdapter(recipeAdapter);
    }

    private void loadFavoriteRecipes() {
        DataBaseManager.getRecipes(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.getDocuments().forEach(new Consumer<DocumentSnapshot>() {
                    @Override
                    public void accept(DocumentSnapshot documentSnapshot) {
                        Recipe recipe = documentSnapshot.toObject(Recipe.class);
                        if (recipe.getFavoritedBy().containsKey(FirebaseAuth.getInstance().getUid())){
                            favoriteRecipes.add(recipe);
                        }
                    }
                });
                recipeAdapter.notifyDataSetChanged();
            }
        });
    }
}