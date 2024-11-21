package com.example.a2fao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import javax.mail.MessagingException;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText privateEditText;
    private FirebaseAuth mAuth;
    private Button loginButton, signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        privateEditText = findViewById(R.id.editTextTextPassword);

        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUp.class)));
        loginButton.setOnClickListener(view -> validateFields());
    }
    private void validateFields(){
        String email = emailEditText.getText().toString().trim();
        String password = privateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email field is empty!" , Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
        }else{
            loginUser(email, password);
        }
    }
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               FirebaseUser user = mAuth.getCurrentUser();
               if(user.isEmailVerified()) {
                   Toast.makeText(this, "Login successfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
               }else{
                   Toast.makeText(this, "Email isnt verified! Email has been sent again", Toast.LENGTH_SHORT).show();
                   user.sendEmailVerification();
               }
           }else{
               Toast.makeText(this, "Account credentials are wrong!", Toast.LENGTH_SHORT).show();
           }
        });
    }
}

