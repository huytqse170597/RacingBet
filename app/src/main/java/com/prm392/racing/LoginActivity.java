package com.prm392.racing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    
    private TextInputEditText edtUsername;
    private TextInputEditText edtPassword;
    private Button btnLogin;
    private TextView tvRegister;
    
    private static final String PREFS_NAME = "UserCredentials";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
        
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void validateLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }
        
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        if (!preferences.contains(username)) {
            Toast.makeText(this, "Username does not exist. Please register first.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String savedPassword = preferences.getString(username, null);
        
        if (savedPassword != null && savedPassword.equals(password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}