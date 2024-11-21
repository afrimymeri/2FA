package com.example.a2fao;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    private Button signUpButton;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameField = findViewById(R.id.editTextTextName);
        surnameField = findViewById(R.id.editTextTextSurname);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        confirmPasswordField = findViewById(R.id.editTextTextConfirmPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(view -> startActivity(new Intent(SignUp.this, LoginActivity.class)));
        signUpButton.setOnClickListener(view -> registerUser());
    }
    private void registerUser() {
        String name = nameField.getText().toString().trim();
        String surname = surnameField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        if (TextUtils.isEmpty(name) ||TextUtils.isEmpty(surname) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "One of the required fields is missing! ", Toast.LENGTH_SHORT).show();
        }else if (!password.matches(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
        if(mAuth != null) {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null) {
                        user.sendEmailVerification().addOnCompleteListener(t -> {
                           if (t.isSuccessful()) {
                               Toast.makeText(this, "Email has been sent to your email" + user.getEmail(), Toast.LENGTH_SHORT).show();
                               saveUserData(user.getUid(), name, surname, phone, email);
                           }else{
                               Toast.makeText(this, "Verifying email has not been sent!", Toast.LENGTH_SHORT).show();
                           }
                        });
                    }else{
                        Toast.makeText(this, "This user is missing!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Cannot create user!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void saveUserData(String userId, String name, String surname, String email, String phone) {
        User user = new User(name, surname, email, phone);
        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "User saved successfully!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Failed saving user data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
