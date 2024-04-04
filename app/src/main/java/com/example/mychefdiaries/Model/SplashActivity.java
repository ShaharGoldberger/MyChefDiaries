package com.example.mychefdiaries.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.mychefdiaries.R;
import com.example.mychefdiaries.Utilities.BackgroundSound;
import com.google.android.material.imageview.ShapeableImageView;

public class SplashActivity extends AppCompatActivity {

    private ShapeableImageView splash_IMG_logo;
    private BackgroundSound backgroundSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findView();
        startAnimation(splash_IMG_logo);

    }

    private void findView() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
    }

    private void startAnimation(View view){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        view.setY(-height / 2.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationY(0)
                .setDuration(4000)
                .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animation) {
                                TransactToMainActivity();

                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        }
                );

    }
    private void TransactToMainActivity(){
        startActivity(new Intent(this, LogInActivity.class));
        finish();
    }

    protected void onPause() {
        super.onPause();
        backgroundSound.stopSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundSound = new BackgroundSound(this, R.raw.splash_sound);
        backgroundSound.playSound();
    }
}