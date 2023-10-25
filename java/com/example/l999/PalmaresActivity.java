package com.example.l999;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;

public class PalmaresActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPalmares;
    private List<String> dataList;
    private String currentUsername="w";
    private PalmaresAdapter adapter;
    private KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palmares);

        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUsername);
        DatabaseReference wonCompsRef = currentUserRef.child("wonComps");

        wonCompsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    KonfettiView konfettiView = findViewById(R.id.konfettiView);
                    konfettiView.build()
                            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                            .setDirection(0.0, 359.0)
                            .setSpeed(1f, 5f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(2000L)
                            .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                            .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                            .streamFor(300, 5000L);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dataList = new ArrayList<>();

        recyclerViewPalmares = findViewById(R.id.recyclerViewPalmares);
        recyclerViewPalmares.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PalmaresAdapter(dataList);
        recyclerViewPalmares.setAdapter(adapter);


        wonCompsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot medalSnapshot : snapshot.getChildren()) {
                        String medalType = medalSnapshot.getKey();


                        for (DataSnapshot compSnapshot : medalSnapshot.getChildren()) {
                            String competitionName = compSnapshot.getValue(String.class);
                            String competitionId = compSnapshot.getKey();
                            dataList.add(competitionName + "," + medalType + "," + competitionId);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}