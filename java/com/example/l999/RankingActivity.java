package com.example.l999;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Size;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;

public class RankingActivity extends AppCompatActivity {
        private RecyclerView recyclerView;
        private RankingAdapter adapter;
        private KonfettiView konfettiView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ranking);

            recyclerView = findViewById(R.id.ranking_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<User> userList = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        user.setUsername(userSnapshot.getKey());
                        userList.add(user);
                    }

                    Collections.sort(userList, (u1, u2) -> Integer.compare(u2.getTotalPoints(), u1.getTotalPoints()));

                    for (int i = 0; i < userList.size(); i++) {
                        User user = userList.get(i);
                        String userId = user.getUsername();
                        int rank = i + 1;

                        DatabaseReference rankRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Rank");
                        rankRef.setValue(rank);
                    }

                    updateUIWithUserList(userList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }

        private void updateUIWithUserList(List<User> userList) {
            adapter = new RankingAdapter(userList);
            recyclerView.setAdapter(adapter);
        }
}