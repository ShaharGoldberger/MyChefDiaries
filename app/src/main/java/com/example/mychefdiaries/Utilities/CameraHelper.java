package com.example.mychefdiaries.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import java.io.IOException;

public class CameraHelper {

    public static ActivityResultLauncher<Intent> setupCameraLauncher(AppCompatActivity activity, Consumer<Bitmap> onImageReceived) {
        return activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                        onImageReceived.accept(imageBitmap);
                    }
                });
    }

    public static ActivityResultLauncher<Intent> setupGalleryLauncher(AppCompatActivity activity, Consumer<Bitmap> onImageReceived) {
        return activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                            onImageReceived.accept(imageBitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    public static void openGallery(ActivityResultLauncher<Intent> galleryLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        galleryLauncher.launch(intent);
    }

    public static void openCamera(ActivityResultLauncher<Intent> cameraLauncher) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

}
