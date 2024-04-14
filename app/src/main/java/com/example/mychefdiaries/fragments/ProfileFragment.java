package com.example.mychefdiaries.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mychefdiaries.CreateProfileActivity;
import com.example.mychefdiaries.DataBaseManager;
import com.example.mychefdiaries.Model.LogInActivity;
import com.example.mychefdiaries.Model.MainActivity;
import com.example.mychefdiaries.Model.User;
import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ProfileFragment extends Fragment {

    private ImageView profileImage;

    private Bitmap imageBitmap;

    private ProgressDialog progressDialog;

    private User currentUser;

    private  TextInputEditText name;


    private ActivityResultLauncher<Intent> cameraLauncher;

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(getString(R.string.please_wait));
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->{
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                        Glide.with(requireActivity())
                                .load(imageBitmap)
                                .placeholder(R.drawable.baseline_accessibility_24)
                                .into(profileImage);

                    }
                });

        galleryLauncher =  registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->{
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Glide.with(requireActivity())
                                .load(imageBitmap)
                                .placeholder(R.drawable.baseline_accessibility_24)
                                .into(profileImage);
                    }

                });
         name = view.findViewById(R.id.full_name);
        TextInputEditText email = view.findViewById(R.id.email);
        Button signOut = view.findViewById(R.id.sign_out);
        signOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireActivity(), LogInActivity.class));
            requireActivity().finish();
        });

        profileImage = view.findViewById(R.id.profile_image);
        ImageView cameraIV = view.findViewById(R.id.camera);
        ImageView galleryIV = view.findViewById(R.id.gallery);

        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button saveBt = view.findViewById(R.id.save);
        saveBt.setOnClickListener(v -> saveUser(FirebaseAuth.getInstance().getUid()));
        DataBaseManager.getUserById(FirebaseAuth.getInstance().getUid(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
                name.setText(currentUser.getFullName() == null ? "" : currentUser.getFullName());
                email.setText(currentUser.getEmail());
                Glide.with(requireActivity())
                        .load(currentUser.getImageUrl())
                        .placeholder(R.drawable.baseline_accessibility_24)
                        .into(profileImage);
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

    private void saveUser(String uid) {
        progressDialog.show();
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
            saveUserToFireStore(uid,currentUser.getImageUrl());
        }
    }

    private void saveUserToFireStore(String uid, String imageUrl) {
        User user = new User(uid, FirebaseAuth.getInstance().getCurrentUser().getEmail(), name.getText().toString(), imageUrl);
        DataBaseManager.saveUser(user, task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), getString(R.string.update_profile_successfully), Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.error)
                        .setMessage(task.getException().getMessage())
                        .show();
            }
        });
    }
}