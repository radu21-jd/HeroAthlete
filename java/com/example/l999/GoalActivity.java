package com.example.l999;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GoalActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference goalRef;
    private GoalAdapter adapter;
    private List<Goal> goalList = new ArrayList<>();
    private List<Goal> selectedGoals = new ArrayList<>();
    private List<Goal> activeGaols = new ArrayList<>();
    private CheckBox buttonSelectGoal;
    private CheckBox buttonActiveGoals;
    private Button selectedButton;
    private CheckBox buttonAcomplishedGoals;

    private List<Goal> getUnselectedGoals() {
        List<Goal> unselectedGoals = new ArrayList<>();
        for (Goal goal : goalList) {
            if (!goal.isSelected()) {
                unselectedGoals.add(goal);
            }
        }
        return unselectedGoals;
    }

    private List<Goal> getActiveGoals() {
        List<Goal> activeGoals = new ArrayList<>();
        for (Goal goal : goalList) {
            if (goal.isActive() && !goal.isCompleted()) {
                activeGoals.add(goal);
            }
        }
        return activeGoals;
    }

    private List<Goal> getAccomplishedGoals() {
        List<Goal> accomplishedGoals = new ArrayList<>();
        for (Goal goal : goalList) {
            if (goal.isCompleted()) {
                accomplishedGoals.add(goal);
            }
        }
        return accomplishedGoals;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        recyclerView = findViewById(R.id.goalRecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonSelectGoal = findViewById(R.id.checkBoxSelectGoal);
        buttonActiveGoals = findViewById(R.id.checkBoxActiveGoals);
        buttonAcomplishedGoals= findViewById(R.id.checkBoxAcomplishedGoals);

        selectedButton = buttonSelectGoal;

        buttonSelectGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                buttonActiveGoals.setChecked(false);
                buttonSelectGoal.setChecked(true);
                buttonAcomplishedGoals.setChecked(false);
                selectedButton = buttonSelectGoal;

                adapter.setGoalList(getUnselectedGoals());
                adapter.notifyDataSetChanged();
            }
        });


        buttonActiveGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                buttonSelectGoal.setChecked(false);
                buttonActiveGoals.setChecked(true);
                buttonAcomplishedGoals.setChecked(false);
                selectedButton = buttonActiveGoals;

                adapter.setGoalList(getActiveGoals());
                adapter.notifyDataSetChanged();
            }
        });


        buttonAcomplishedGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                buttonSelectGoal.setChecked(false);
                buttonActiveGoals.setChecked(false);
                buttonAcomplishedGoals.setChecked(true);
                selectedButton=buttonAcomplishedGoals;

                adapter.setGoalList(getAccomplishedGoals());
                adapter.notifyDataSetChanged();
            }
        });



        FirebaseDatabase database=FirebaseDatabase.getInstance();
        goalRef = database.getReference("Users").child("w").child("goals");

        goalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    for (int i = 1; i <= 4; i++) {
                        String goalId = goalRef.push().getKey();
                        Goal goal = new Goal(goalId, "Goal " + i, false,false, false);
                        goalRef.child(goalId).setValue(goal);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new GoalAdapter(goalList);
        recyclerView.setAdapter(adapter);

        goalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                goalList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Goal goal = dataSnapshot.getValue(Goal.class);
                    if (goal != null) {
                        goalList.add(goal);
                    }
                }

                if (selectedButton == buttonSelectGoal) {
                    adapter.setGoalList(getUnselectedGoals());
                } else if (selectedButton == buttonActiveGoals) {
                    adapter.setGoalList(getActiveGoals());
                } else if (selectedButton == buttonAcomplishedGoals) {
                    adapter.setGoalList(getAccomplishedGoals());
                }

                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}