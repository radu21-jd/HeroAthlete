package com.example.l999;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://l999-f1fce-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.loginButton);
        final Button authButton = findViewById(R.id.authButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameTxt = username.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (isConnectedToInternet()) {
                    database.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernameTxt)) {
                                String storedPassword = snapshot.child(usernameTxt).child("password").getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(passwordTxt)) {
                                    Toast.makeText(LoginActivity.this, "Autentificare reușită!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, JudoActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(LoginActivity.this, "Parolă incorectă!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Nume de utilizator inexistent!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Te rugăm să te conectezi la internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Deschide RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}
