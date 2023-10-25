package com.example.l999;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAntrenamenteActivity extends AppCompatActivity implements ProgramareAntrenamente.ScheduleTrainingListener {

    private CalendarView calendar;
    private RecyclerView listaAntre;
    private List<Trainings> trainingList;
    private TrainingsAdapter trainingsAdapter;
    private CircularProgressBar antrenamente2;
    private Switch showList;
    private TextView progres;
    int nr, nrSept, nrIun, nrAug,nrOct,nrMar,nrApr,nrMay,nrNov,nrDec,nrIan,nrFeb,nrIul;

    private void updateProgressBar() {
        int totalAntre=100;
        int progress = (int) (((float) nr / totalAntre) * 100);
        antrenamente2.setProgressWithAnimation(progress, 1000L);

        progres.setText("Total: "+nr);

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth1 = currentDate.get(Calendar.MONTH);

        Calendar lastDayOfMonth = Calendar.getInstance();
        lastDayOfMonth.set(currentYear, currentMonth1, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDay = lastDayOfMonth.get(Calendar.DAY_OF_MONTH);

        if (calendar.get(Calendar.DAY_OF_MONTH) == lastDay) {
            // Este ultima zi a lunii, deci resetăm bara de progres și calculăm statisticile
            //resetProgressBar(); // Implementează această metodă pentru a reseta bara de progres
            //calculateStatistics(); // Implementează această metodă pentru a calcula statisticile
            //showStatisticsDialog();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrenamente);

        calendar = findViewById(R.id.calendarView);
        listaAntre = findViewById(R.id.antreRecyler);
        showList=findViewById(R.id.switch1);
        antrenamente2=findViewById(R.id.progressBarAntrenament);
        progres= findViewById(R.id.textViewProgresz);



        trainingList = new ArrayList<>();
        trainingsAdapter = new TrainingsAdapter(trainingList);

        listaAntre.setLayoutManager(new LinearLayoutManager(this));
        listaAntre.setAdapter(trainingsAdapter);

        listaAntre.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "heroAthlete";
            String description = "licentaUVT2023";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1245", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                ProgramareAntrenamente dialog = new ProgramareAntrenamente(CalendarAntrenamenteActivity.this, year, month, dayOfMonth);
                dialog.show(getSupportFragmentManager(), "scheduleTraining");

                Calendar lastDayOfMonth = Calendar.getInstance();
                lastDayOfMonth.set(year, month, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                int lastDay = lastDayOfMonth.get(Calendar.DAY_OF_MONTH);

                Button statisticsButton = findViewById(R.id.statisticsButton);
                statisticsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CalendarAntrenamenteActivity.this, StatisticsActivity.class);
                        intent.putExtra("totalTrainings", nr);
                        intent.putExtra("AugTrainings", nrAug);
                        intent.putExtra("SeptTrainings", nrSept);
                        intent.putExtra("OctTrainings", nrOct);
                        intent.putExtra("NovTrainings", nrNov);
                        intent.putExtra("DecTrainings", nrDec);
                        intent.putExtra("IanTrainings", nrIan);
                        intent.putExtra("FebTrainings", nrFeb);
                        intent.putExtra("MarTrainings", nrMar);
                        intent.putExtra("AprTrainings", nrApr);
                        intent.putExtra("IunTrainings", nrIun);
                        intent.putExtra("IulTrainings", nrIul);
                        startActivity(intent);
                    }
                });

            }
        });

        DatabaseReference trainingsRef = FirebaseDatabase.getInstance().getReference().child("Trainings").child("w");

        trainingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trainingList.clear();
                for (DataSnapshot trainingSnapshot: dataSnapshot.getChildren()) {
                    Trainings training = trainingSnapshot.getValue(Trainings.class);
                    if (training != null && !training.getCompleted()) {
                        training.setId(trainingSnapshot.getKey());
                        trainingList.add(training);
                    }
                }

                trainingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors here
            }
        });

        showList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listaAntre.setVisibility(View.VISIBLE);

                }else{
                    listaAntre.setVisibility(View.GONE);
                }
            }
        });

        DatabaseReference nrTrainings=FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("trainings");
        nrTrainings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nr=snapshot.getValue(Integer.class);
                updateProgressBar();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrAugustRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrAug");
        nrAugustRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrAug=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrSeptRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrSept");
        nrSeptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrSept=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrOctRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrOct");
        nrOctRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrOct=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrNovRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrNov");
        nrNovRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrNov=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrDecRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrDec");
        nrDecRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrDec=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrIanRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrIan");
        nrIanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrIan=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrFebRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrFeb");
        nrFebRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrFeb=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrMarRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrMar");
        nrMarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrMar=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrAprRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrApr");
        nrAprRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrApr=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrMayRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrMay");
        nrMayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrMay=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrIunRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrIun");
        nrIunRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrIun=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrIulRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrIul");
        nrIulRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nrIul=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onScheduleTraining(int year, int month, int dayOfMonth, int hour, int minute, String type) {
        Trainings trainingData = new Trainings(year, month, dayOfMonth, hour, minute, false, type);
        scheduleNotification(trainingData);

        trainingList.add(trainingData);
        trainingsAdapter.notifyDataSetChanged();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference trainingRef= database.getReference().child("Trainings");

        String id = trainingRef.push().getKey();
        trainingRef.child("w").child(id).setValue(trainingData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Programare realizata cu succes!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
                    }
                });
    }

    private void scheduleNotification(Trainings training) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(training.getYear(), training.getMonth(), training.getDayOfMonth(), training.getHour(), training.getMinute());
        calendar.add(Calendar.MINUTE, -30);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
