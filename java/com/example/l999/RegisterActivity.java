package com.example.l999;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://l999-f1fce-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);

        final Button registerButton = findViewById(R.id.registerButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameTxt = username.getText().toString();
                final String passwordTxt = password.getText().toString();


                if (isConnectedToInternet()) {
                    database.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernameTxt)) {
                                Toast.makeText(RegisterActivity.this, "Numele de utilizator există deja!", Toast.LENGTH_SHORT).show();
                            } else {
                                database.child("Users").child(usernameTxt).child("username").setValue(usernameTxt);
                                database.child("Users").child(usernameTxt).child("password").setValue(passwordTxt);
                                database.child("Users").child(usernameTxt).child("userPoints").setValue(0);
                                database.child("Users").child(usernameTxt).child("totalPoints").setValue(0);
                                database.child("Users").child(usernameTxt).child("titlePoints").setValue(0);
                                database.child("Users").child(usernameTxt).child("title").setValue(null);
                                database.child("Users").child(usernameTxt).child("goals").setValue(null);
                                database.child("Users").child(usernameTxt).child("isSetup").setValue(false);

                                database.child("Users").child(usernameTxt).child("wonComps").child("gold").setValue(null);
                                database.child("Users").child(usernameTxt).child("wonComps").child("silver").setValue(null);
                                database.child("Users").child(usernameTxt).child("wonComps").child("zbronze").setValue(null);

                                Toast.makeText(RegisterActivity.this, "Utilizator înregistrat cu succes!", Toast.LENGTH_SHORT).show();

                                // Redirecționarea utilizatorului catre pag. de LogIn
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Te rugăm să te conectezi la internet!", Toast.LENGTH_SHORT).show();
                }
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
