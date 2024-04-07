package com.example.mychefdiaries;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychefdiaries.Model.Recipe;
import com.example.mychefdiaries.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CreateRecipeActivity extends AppCompatActivity {

    private TextInputEditText recipeName, ingredients, instructions, duration, category;
    private ImageView recipeImage;
    private Bitmap imageBitmapForRecipe;
    private String selectedCategory = "";
    private RadioGroup categoryRadioGroup;
    private RadioButton beef_btn, dairy_btn, fish_btn, vegan_btn, cocktails_btn, desserts_btn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String recipeImageUrl;


    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    imageBitmapForRecipe = (Bitmap) result.getData().getExtras().get("data");
                    recipeImage.setImageBitmap(imageBitmapForRecipe);
                }

            });

    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri uri = result.getData().getData();
                    try {
                        imageBitmapForRecipe = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    recipeImage.setImageBitmap(imageBitmapForRecipe);
                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        recipeName = findViewById(R.id.name);
        ingredients = findViewById(R.id.ingridients); // Remember to fix the typo if you update it in XML
        instructions = findViewById(R.id.instructions);
        duration = findViewById(R.id.duration);
        recipeImage = findViewById(R.id.profile_image);

        categoryRadioGroup= findViewById(R.id.categoryRadioGroup);
        beef_btn = findViewById(R.id.beef_button);
        dairy_btn = findViewById(R.id.dairy_button);
        fish_btn = findViewById(R.id.fish_button);
        vegan_btn = findViewById(R.id.vegan_button);
        cocktails_btn = findViewById(R.id.cocktails_button);
        desserts_btn = findViewById(R.id.desserts_button);

        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Directly obtain the RadioButton from the RadioGroup and checkedId
                RadioButton selectedButton = group.findViewById(checkedId);
                if (selectedButton != null) {
                    // Get the text from the selected RadioButton
                    selectedCategory = selectedButton.getText().toString();
                } else {
                    selectedCategory = ""; // or null, depending on how you want to handle no selection
                }
            }
        });

        ImageView cameraIV = findViewById(R.id.camera);
        ImageView galleryIV = findViewById(R.id.gallery);

        cameraIV.setOnClickListener(v-> openCamera());
        galleryIV.setOnClickListener(v-> openGallery());

        Button createBT = findViewById(R.id.create);
        createBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipe();
            }
        });
    }

    private void createRecipe() {
        boolean isValidInput = true;
        if (recipeName.getText().toString().isEmpty()) {
            recipeName.setError(getString(R.string.name_of_recipe));
            isValidInput = false;
        }
        if (ingredients.getText().toString().isEmpty()) {
            ingredients.setError(getString(R.string.ingredients_of_recipe));
            isValidInput = false;
        }
        if (instructions.getText().toString().isEmpty()) {
            instructions.setError(getString(R.string.instruction_of_recipe));
            isValidInput = false;
        }
        if (duration.getText().toString().isEmpty()) {
            duration.setError(getString(R.string.duration_of_recipe));
            isValidInput = false;
        }
        if (selectedCategory.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.category_of_recipe), Toast.LENGTH_LONG).show();
            isValidInput = false;
        }
        // need to get the user and create the recipe
        if (isValidInput) {
            String id = UUID.randomUUID().toString();
           uploadImage(id, new Runnable(){
               @Override
               public void run() {
                   saveRecipeToDataBase(id);
               }
           });
        }
    }

    private void uploadImage(String uid, Runnable onDone) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (imageBitmapForRecipe != null) {
            StorageReference ref = storage.getReference().child("images/"+uid+".jpg");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmapForRecipe.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = ref.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            recipeImageUrl = uri.toString();
                            onDone.run();
                        }
                    });
                }
            });
        } else {
            onDone.run();
        }
    }

    private void saveRecipeToDataBase(String id) {
        FirebaseUser currentUser =  mAuth.getCurrentUser();;
        String uid = currentUser.getUid(); // Get user's UID
        Recipe newRecipe = new Recipe();
        newRecipe.setId(id);
        newRecipe.setTitle(recipeName.getText().toString());
        newRecipe.setIngredients(ingredients.getText().toString());
        newRecipe.setText(instructions.getText().toString()); // Correct method for instructions
        newRecipe.setImage(recipeImageUrl); // Here you should set the image URL of the recipe, not the user's image URL
        newRecipe.setMinutes(duration.getText().toString());
        newRecipe.setCategory(selectedCategory.toString());
        newRecipe.setCreatedUserId(uid);

        DataBaseManager.saveRecipes(newRecipe, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Operation successful
                    Toast.makeText(getApplicationContext(), "Recipe saved successfully!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // Operation failed, handle the error
                    Toast.makeText(getApplicationContext(), "Failed to save recipe.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }


}