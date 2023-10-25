package com.example.l999;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TitluriActivity extends AppCompatActivity {
    private RecyclerView titleRecyler;
    private TextView pct;
    private List<Titluri> listaTitluri = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titluri);

        pct=findViewById(R.id.textViewTPoints);

        titleRecyler=findViewById(R.id.titluriRecyler);
        titleRecyler.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference("Titluri");
        titleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaTitluri.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Titluri titluri = postSnapshot.getValue(Titluri.class);
                    titluri.setId(postSnapshot.getKey());
                    listaTitluri.add(titluri);
                }

                TitluriAdapter adapter = new TitluriAdapter(TitluriActivity.this, listaTitluri);
                titleRecyler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TitluriActivity.this, "Eroare: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference pointsRef=FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("titlePoints");
        pointsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int points=snapshot.getValue(Integer.class);
                    pct.setText("Title points: "+points);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
