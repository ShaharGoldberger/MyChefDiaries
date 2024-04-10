package com.example.mychefdiaries.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Map<String, Object> ingredientsObj = new HashMap<>();
    private ArrayList<String> ingredientsArrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Button addIngredientsBtn;
    private EditText ingredientsInput;
    private ListView ingredientsListView;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_cart, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        addIngredientsBtn = view.findViewById(R.id.addIngredientToListBtn);
        ingredientsInput = view.findViewById(R.id.ingredientInputText);
        ingredientsListView = view.findViewById(R.id.ingredientsListView);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, ingredientsArrayList);
        ingredientsListView.setAdapter(adapter);

        readFromDB();

        addIngredientsBtn.setOnClickListener(v -> {
            String ingredient = ingredientsInput.getText().toString().trim();
            if (!ingredient.isEmpty()) {
                addIngredientToDB(ingredient);
            } else {
                Toast.makeText(getActivity(), "Please input an ingredient", Toast.LENGTH_LONG).show();
            }
        });

        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                if (item != null) {
                    removeIngredientFromDB(item, position);
                }
            }
        });

        return view;
    }

    private void addIngredientToDB(String ingredient) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Map<String, Object> update = new HashMap<>();
            update.put(ingredient, true);

            // Using set with merge option to ensure document is created if it does not exist
            db.collection("users").document(uid).collection("Shopping List").document("Ingredients")
                    .set(update, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        ingredientsArrayList.add(ingredient);
                        adapter.notifyDataSetChanged();
                        ingredientsInput.setText("");
                        Toast.makeText(getActivity(), "Ingredient added", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error adding item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error adding item", e);
                    });
        }
    }

    private void removeIngredientFromDB(String ingredient, int position) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Map<String, Object> update = new HashMap<>();
            update.put(ingredient, FieldValue.delete());

            db.collection("users").document(uid).collection("Shopping List").document("Ingredients")
                    .update(update)
                    .addOnSuccessListener(aVoid -> {
                        ingredientsArrayList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Ingredient removed", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error removing item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error removing item", e);
                    });
        }
    }

    private void readFromDB() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            db.collection("users").document(uid).collection("Shopping List").document("Ingredients")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                            ingredientsObj.clear();
                            ingredientsObj.putAll(documentSnapshot.getData());
                            ingredientsArrayList.clear();
                            ingredientsArrayList.addAll(ingredientsObj.keySet());
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error fetching ingredients: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error fetching ingredients", e);
                    });
        }
    }
}
