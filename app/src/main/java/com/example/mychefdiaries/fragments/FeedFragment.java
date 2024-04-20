package com.example.mychefdiaries.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mychefdiaries.Adapters.RecipeAdapter;
import com.example.mychefdiaries.Acitivites.CreateRecipeActivity;
import com.example.mychefdiaries.Utilities.DataBaseManager;
import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.Model.User;
import com.example.mychefdiaries.R;
import com.example.mychefdiaries.Acitivites.SearchRecipeByCategoryActivity;
import com.example.mychefdiaries.Acitivites.SearchRecipeByTextActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private RecipeAdapter adapter;
    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private boolean refreshFeed = false;

    private TextView helloTV;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recipeArrayList.clear();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView list = view.findViewById(R.id.list);
        adapter = new RecipeAdapter(recipeArrayList);
        list.setAdapter(adapter);

        helloTV = view.findViewById(R.id.helloUserText);

        Button createBT = view.findViewById(R.id.create);
        createBT.setOnClickListener(v->{
            goToAddRecepie();
        });

        Button SearchByCategoryBT = view.findViewById(R.id.search_by_category);
        SearchByCategoryBT.setOnClickListener(v->{
            goToSearchRecipeByCategory();
        });

        Button SearchByTextBT = view.findViewById(R.id.search_by_text);
        SearchByTextBT.setOnClickListener(v->{
            goToSearchRecipeByText();
        });

        DataBaseManager.getUserById(FirebaseAuth.getInstance().getUid(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                helloTV.setText(getString(R.string.hello_user, user.getFullName()));

            }
        });
        fetchData();
    }

    private void fetchData() {
        recipeArrayList.clear();
        DataBaseManager.getRecipesByUserId(FirebaseAuth.getInstance().getUid(), new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //queryDocumentSnapshots.getDocuments().forEach(documentSnapshot -> recipeArrayList.add(documentSnapshot.toObject(Recipe.class)));
                //adapter.notifyDataSetChanged();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Recipe newRecipe = documentSnapshot.toObject(Recipe.class);
                    // Check if the list already contains a recipe with the same ID
                    boolean recipeExists = false;
                    for (Recipe existingRecipe : recipeArrayList) {
                        if (existingRecipe.getId().equals(newRecipe.getId())) {
                            recipeExists = true;
                            break;
                        }
                    }
                    if (!recipeExists) {
                        // Only add the recipe if it's not already in the list
                        recipeArrayList.add(newRecipe);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(refreshFeed){
            //recipeArrayList.clear();
            fetchData(); // Fetch the data again
            refreshFeed = false;
        }

    }

    private void goToAddRecepie() {
        Intent intent = new Intent(requireContext(), CreateRecipeActivity.class);
        startActivity(intent);
        refreshFeed = true;
    }

    private void goToSearchRecipeByCategory() {
        Intent intent = new Intent(requireContext(), SearchRecipeByCategoryActivity.class);
        startActivity(intent);
    }

    private void goToSearchRecipeByText() {
        Intent intent = new Intent(requireContext(), SearchRecipeByTextActivity.class);
        startActivity(intent);
    }






}