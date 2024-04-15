package com.example.mychefdiaries.Acitivites;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mychefdiaries.Utilities.CameraHelper;
import com.example.mychefdiaries.Utilities.DataBaseManager;
import com.example.mychefdiaries.Model.User;
import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateProfileActivity extends AppCompatActivity {

    private TextInputEditText emailET, passwordET, fullNameET;
    private ImageView profileImage;
    private Bitmap imageBitmap;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private ImageView cameraIV;
    private ImageView galleryIV;
    private Button createBT;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_layout);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        findViews();

        cameraLauncher = CameraHelper.setupCameraLauncher(this, bitmap -> {
            profileImage.setImageBitmap(bitmap);
        });
        galleryLauncher = CameraHelper.setupGalleryLauncher(this, bitmap -> {
            profileImage.setImageBitmap(bitmap);
        });

        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper.openCamera(cameraLauncher);
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper.openGallery(galleryLauncher);
            }
        });

        //Button createBT = findViewById(R.id.create);
        createBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void findViews() {
        profileImage = findViewById(R.id.profile_image);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        fullNameET = findViewById(R.id.full_name);
        cameraIV = findViewById(R.id.camera);
        galleryIV = findViewById(R.id.gallery);
        createBT = findViewById(R.id.create);
    }

    private void createUser() {
        boolean isValidInput = true;
        if (emailET.getText().toString().isEmpty()) {
            emailET.setError(getString(R.string.please_enter_email));
            isValidInput = false;
        }
        if (passwordET.getText().toString().isEmpty()) {
            passwordET.setError(getString(R.string.please_enter_password));
            isValidInput = false;
        }
        if (fullNameET.getText().toString().isEmpty()) {
            fullNameET.setError(getString(R.string.enter_full_name));
            isValidInput = false;
        }

        if (isValidInput) {
            progressDialog.show();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveUser(task.getResult().getUser().getUid());
                            } else {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(CreateProfileActivity.this)
                                        .setMessage(task.getException().getMessage())
                                        .setTitle(R.string.error)
                                        .show();
                            }
                        }
                    });
        }
    }

    private void saveUser(String uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (imageBitmap != null) {
            StorageReference ref = storage.getReference().child("images/"+uid+".jpg");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = ref.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            saveUserToFireStore(uid, uri.toString());
                        }
                    });
                }
            });
         } else {
            saveUserToFireStore(uid,null);
        }
    }

    private void saveUserToFireStore(String uid, String imageUrl) {
        User user = new User(uid, FirebaseAuth.getInstance().getCurrentUser().getEmail(), fullNameET.getText().toString(), imageUrl);
        DataBaseManager.saveUser(user, task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(CreateProfileActivity.this, getString(R.string.create_profile_successfully), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                new AlertDialog.Builder(CreateProfileActivity.this)
                        .setTitle(R.string.error)
                        .setMessage(task.getException().getMessage())
                        .show();
            }
        });
    }

}