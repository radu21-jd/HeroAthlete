package com.example.l999;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private List<Competition> competitionList;
    private List<Competition> allCompetitions;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        recyclerView = findViewById(R.id.recyclerView);
        categorySpinner = findViewById(R.id.categorySpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        competitionList = new ArrayList<>();
        allCompetitions = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(this, competitionList);
        recyclerView.setAdapter(calendarAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterCompetitionsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        DatabaseReference competitionsRef = FirebaseDatabase.getInstance().getReference().child("Competitions");
        competitionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allCompetitions.clear();
                List<Competition> inscribedCompetitions = new ArrayList<>();
                inscribedCompetitions.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String image = snapshot.child("image").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String category = snapshot.child("category").getValue(String.class);
                    Boolean isFinished = snapshot.child("isFinished").getValue(Boolean.class);
                    Boolean isInscris = snapshot.child("isInscris").getValue(Boolean.class);
                    Boolean hasToken = snapshot.child("hasToken").getValue(Boolean.class);
                    Boolean joToken = snapshot.child("joToken").getValue(Boolean.class);


                    if (isFinished == null || !isFinished) {
                        Competition competition = new Competition(id, image, description, date, category, isFinished, isInscris, hasToken, joToken);
                        allCompetitions.add(competition);
                    }

                    if (isInscris != null && isInscris) {
                        Competition competition=new Competition(id, image, description, date, category, isFinished, isInscris, hasToken, joToken);
                        inscribedCompetitions.add(competition);
                    }
                }
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                filterCompetitionsByCategory(selectedCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error getting competitions: " + databaseError.getMessage());
            }
        });
    }

    private void filterCompetitionsByCategory(String selectedCategory) {
        if (selectedCategory.equals("Toate")) {
            calendarAdapter.setCompetitionList(allCompetitions);
        } else {
            List<Competition> filteredList = new ArrayList<>();

            for (Competition competition : allCompetitions) {
                if (competition.getCategory().equals(selectedCategory)) {
                    filteredList.add(competition);
                }
            }

            calendarAdapter.setCompetitionList(filteredList);
        }

        calendarAdapter.notifyDataSetChanged();
    }

}


