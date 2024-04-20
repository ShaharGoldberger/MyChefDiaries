package com.example.mychefdiaries.Acitivites;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mychefdiaries.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // This variable will be used to handle Firebase authentication operations.
    private TextInputEditText emailET, passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //FirebaseAuth.getInstance().signOut();
        setContentView(R.layout.login_layout); // setContentView(R.layout.login); sets the UI layout for the activity from a layout resource file named login.xml.
        // This block of code sets an OnApplyWindowInsetsListener on the view with ID main. It adjusts the padding of the view to account for system bars like the status and navigation bars, ensuring the content doesn't overlap with them.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        mAuth = FirebaseAuth.getInstance(); // Initializes the mAuth variable with an instance of FirebaseAuth, allowing you to use Firebase for authentication in this activity.

        //get the user
        FirebaseUser user = mAuth.getCurrentUser(); // Retrieves the current user. If the user is not logged in, user will be null.
        //If I don't have a user --> we will offer him to connect
        if (user != null) {
            onUserLoggedIn();
        }

        Button signInBT= findViewById(R.id.sign_in);
        signInBT.setOnClickListener(v -> signIn());

        TextView signUpTV = findViewById(R.id.sign_up);
        signUpTV.setOnClickListener(v -> {
            goToCreateProfileActivity();
        });

    }

    private void goToCreateProfileActivity() {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        boolean isValidInput = true;
        if (emailET.getText().toString().isEmpty()) {
            emailET.setError(getString(R.string.please_enter_email));
            isValidInput = false;
        }
        if (passwordET.getText().toString().isEmpty()) {
            passwordET.setError(getString(R.string.please_enter_password));
            isValidInput = false;
        }

        if (isValidInput) {
            mAuth.signInWithEmailAndPassword(emailET.getText().toString(),
                    passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        onUserLoggedIn();
                    } else {
                        new AlertDialog.Builder(LogInActivity.this)
                                .setTitle(R.string.error)
                                .setMessage(task.getException().getMessage())
                                .show();
                    }
                }
            });
        }
    }

    private void onUserLoggedIn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
    }







}