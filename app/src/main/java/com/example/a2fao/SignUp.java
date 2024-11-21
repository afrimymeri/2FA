package com.example.a2fao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2fao.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText nameField;
    private EditText surnameField;
    private EditText phoneField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;

    private Button loginButton;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameField = findViewById(R.id.editTextTextName);
        surnameField = findViewById(R.id.editTextTextSurname);
        phoneField = findViewById(R.id.editTextPhone);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        confirmPasswordField = findViewById(R.id.editTextTextConfirmPassword);
        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(view -> registerUser());
        loginButton.setOnClickListener(view -> startActivity(new Intent(SignUp.this, LoginActivity.class)));

    }
    private void registerUser(){
        String name = nameField.getText().toString().trim();
        String surname = surnameField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords dont match", Toast.LENGTH_SHORT).show();
        }
        if (mAuth != null) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user !=null) {
                                user.sendEmailVerification().addOnCompleteListener(t-> {
                                    if (t.isSuccessful()){
                                        Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                        saveUserData(user.getUid(), name, surname, phone, email);
                                    }else{
                                        Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } else {
            Toast.makeText(this, "Authentication error. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveUserData(String userId, String name, String surname, String phone, String email){
        User user = new User(name,surname,phone,email);
        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(task ->{
            if(task.isSuccessful()) {
                Toast.makeText(SignUp.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SignUp.this, "Failed saving user data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}