package com.example.l999;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JudoActivity extends AppCompatActivity {
    String userId="w";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judo);

        ImageView image1 = findViewById(R.id.image1);
        ImageView image2 = findViewById(R.id.image2);
        ImageView image3 = findViewById(R.id.image3);
        ImageView image4 = findViewById(R.id.image4);
        //ImageView image5 = findViewById(R.id.image5);
        ImageView image6 = findViewById(R.id.image6);
        ImageView image7 = findViewById(R.id.image7);
        //ImageView image8 = findViewById(R.id.image8);


        ProgressClickListener progressClickListener = new ProgressClickListener(this);
        image1.setOnClickListener(progressClickListener);

        CalendarClickListener calendarClickListener = new CalendarClickListener(this);
        image2.setOnClickListener(calendarClickListener);

        ProfileClickListener profileClickListener = new ProfileClickListener(this);
        image3.setOnClickListener(profileClickListener);

        PalmaresClickListener palmaresClickListener = new PalmaresClickListener(this);
        image4.setOnClickListener(palmaresClickListener);

        //TicketsClickListener ticketsClickListener = new TicketsClickListener(this);
        //image5.setOnClickListener(ticketsClickListener);

        GoalClickListener goalClickListener = new GoalClickListener(this);
        image6.setOnClickListener(goalClickListener);

        ProgramareClickListener ProgramareClickListener = new ProgramareClickListener(this);
        image7.setOnClickListener(ProgramareClickListener);

        //TitluriClickListener TitluriClickListener = new TitluriClickListener(this);
        //image8.setOnClickListener(TitluriClickListener);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference setupRef=database.getReference().child("Users").child("w").child("isSetup");
        setupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Boolean iscomplete=snapshot.getValue(Boolean.class);
                    if(iscomplete==false)
                    {
                        DialogFragment newFragment= new onCreateDialog();
                        newFragment.show(getSupportFragmentManager(), "profileDialog");
                        setupRef.setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ProfileClickListener implements View.OnClickListener {
        private Context context;

        public ProfileClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RankingActivity.class);
            context.startActivity(intent);
        }
    }

    public class PalmaresClickListener implements View.OnClickListener {
        private Context context;

        public PalmaresClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PalmaresActivity.class);
            context.startActivity(intent);
        }
    }

    public class ProgressClickListener implements View.OnClickListener {
        private Context context;

        public ProgressClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
        }
    }

    public class CalendarClickListener implements View.OnClickListener {
        private Context context;

        public CalendarClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CalendarActivity.class);
            context.startActivity(intent);
        }
    }

    public class TicketsClickListener implements View.OnClickListener {
        private Context context;

        public TicketsClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TicketsCompActivity.class);
            context.startActivity(intent);
        }
    }

    public class GoalClickListener implements View.OnClickListener {
        private Context context;

        public GoalClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            //Intent intent = new Intent(context, CalendarAntrenamenteActivity.class);
            Intent intent = new Intent(context, GoalActivity.class);
            context.startActivity(intent);
        }
    }

    public class ProgramareClickListener implements View.OnClickListener {
        private Context context;

        public ProgramareClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CalendarAntrenamenteActivity.class);
            context.startActivity(intent);
        }
    }

    public class TitluriClickListener implements View.OnClickListener {
        private Context context;

        public TitluriClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TitluriActivity.class);
            context.startActivity(intent);
        }
    }
}
