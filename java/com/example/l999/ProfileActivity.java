package com.example.l999;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextSport;
    private Spinner weightCategorySpinner;
    private EditText editTextAge;
    private ImageView profileImage;
    private TextView nrWons;
    private TextView goals;
    private TextView wrl;
    private ImageView countryImg;
    private TextView antrenamente;
    private ProgressBar OverALLprogress;
    private TextView procentaj;
    private TextView text;
    private TextView titlu;
    String name;
    String category;
    String age;
    String country;
    String image;
    String spinnerSelected;
    int nrGoals;
    int Rank;
    int nrWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextSport = findViewById(R.id.editTextSport);
        weightCategorySpinner = findViewById(R.id.weightCategorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("-60");
        adapter.add("-66");
        adapter.add("-73");
        adapter.add("-81");
        adapter.add("-90");
        weightCategorySpinner.setAdapter(adapter);
        editTextAge = findViewById(R.id.editTextAge);

        profileImage = findViewById(R.id.imageViewLogo);
        Animation fadeInAnimation1 = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation1.setStartOffset(0);
        fadeInAnimation1.setDuration(1000);
        profileImage.startAnimation(fadeInAnimation1);

        nrWons = findViewById(R.id.textViewCompetitions);
        Animation fadeInAnimationNrWons = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationNrWons.setDuration(1000);
        fadeInAnimationNrWons.setStartOffset(1500);
        nrWons.startAnimation(fadeInAnimationNrWons);

        goals = findViewById(R.id.textViewObjectives);
        Animation fadeInAnimationGoals = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationGoals.setDuration(1000);
        fadeInAnimationGoals.setStartOffset(2000);
        goals.startAnimation(fadeInAnimationGoals);

        wrl = findViewById(R.id.textViewRank);
        Animation fadeInAnimationWrl = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationWrl.setDuration(1000);
        fadeInAnimationWrl.setStartOffset(1000);
        wrl.startAnimation(fadeInAnimationWrl);

        countryImg = findViewById(R.id.countryImageView);
        antrenamente = findViewById(R.id.textViewAntrenamente);
        Animation fadeInAnimationAntre = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationAntre.setDuration(1000);
        fadeInAnimationAntre.setStartOffset(2500);
        antrenamente.startAnimation(fadeInAnimationAntre);

        CircularProgressBar OverALLprogress = findViewById(R.id.progressBarAll);
        Animation fadeInAnimationBara = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationBara.setDuration(1000);
        fadeInAnimationBara.setStartOffset(3000);
        OverALLprogress.startAnimation(fadeInAnimationBara);

        procentaj = findViewById(R.id.progressText);
        Animation fadeInAnimationProcentaj = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationProcentaj.setDuration(1000);
        fadeInAnimationProcentaj.setStartOffset(3000);
        procentaj.startAnimation(fadeInAnimationProcentaj);

        text = findViewById(R.id.textViewText);
        Animation fadeInAnimationText = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimationText.setDuration(1000);
        fadeInAnimationText.setStartOffset(3000);
        text.startAnimation(fadeInAnimationText);

        titlu=findViewById(R.id.textViewTitlu);
        /*ObjectAnimator scaleX = ObjectAnimator.ofFloat(titlu, "scaleX", 1.0f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(titlu, "scaleY", 1.0f, 1.1f);

        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet pulsingSet = new AnimatorSet();
        pulsingSet.play(scaleX).with(scaleY);
        pulsingSet.setDuration(800);  // durata de 800 ms pentru fiecare ciclu
        pulsingSet.start();*/
        final Context context = this;
        DatabaseReference titluRef=FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("title");
        titluRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String title= snapshot.getValue(String.class);
                    if (!TextUtils.isEmpty(title)) {
                        titlu.setText("[" + title + "]");
                    }
                    /*if(title.equals("Campion Olimpic"))
                    {
                        titlu.setTextColor(ContextCompat.getColor(context, R.color.gold));
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference nameRef = database.getReference().child("Profile").child("w").child("name");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name = snapshot.getValue(String.class);
                    editTextUsername.setText("    "+name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference countryRef = database.getReference().child("Profile").child("w").child("country");
        countryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    country = snapshot.getValue(String.class);
                    editTextSport.setText("  "+ country);

                    int flagResource = 0;
                    if ("Romania".equals(country)) {
                        flagResource = R.drawable.rou_flag;
                    } else if ("Japonia".equals(country)) {
                        flagResource = R.drawable.jpn_flag;
                    } else if ("USA".equals(country)) {
                        flagResource = R.drawable.usa_flag;
                    }

                    if (flagResource != 0) {
                        countryImg.setImageResource(flagResource);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference categoryRef = database.getReference().child("Profile").child("w").child("category");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    category = snapshot.getValue(String.class);
                    //

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ageRef = database.getReference().child("Profile").child("w").child("age");
        ageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    age = snapshot.getValue(String.class);
                    editTextAge.setText("    "+age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference imageRef = database.getReference().child("Profile").child("w").child("image");
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    image = snapshot.getValue(String.class);
                    Glide.with(ProfileActivity.this)
                            .load(image)
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference compsWon = database.getReference().child("Users").child("w").child("allWins");
        compsWon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nrWon = snapshot.getValue(Integer.class);
                    if(nrWon!=0){
                    nrWons.setText("Competiții câștigate: " + nrWon);}
                    else{
                        DatabaseReference comps=database.getReference().child("Users").child("w").child("totalWins");
                        comps.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    int nrW=snapshot.getValue(Integer.class);
                                    nrWons.setText("Competiții câștigate: " + nrW);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    float progress = calculateProgress(nrWon, nrGoals, Rank);
                    animateProgressBar(OverALLprogress, progress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference objsRef = database.getReference().child("Users").child("w").child("objsCompleted");
        objsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nrGoals = snapshot.getValue(Integer.class);
                    goals.setText("Obiective completate: " + nrGoals);

                    float progress = calculateProgress(nrWon, nrGoals, Rank);
                    animateProgressBar(OverALLprogress, progress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference rankRef = database.getReference().child("Users").child("w").child("Rank");
        rankRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Rank = snapshot.getValue(Integer.class);
                    wrl.setText("WRL (world ranking): " + Rank);

                    float progress = calculateProgress(nrWon, nrGoals, Rank);
                    animateProgressBar(OverALLprogress, progress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nrAntre = database.getReference().child("Users").child("w").child("trainings");
        nrAntre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int nrAntre = snapshot.getValue(Integer.class);
                    antrenamente.setText("Antrenamente: " + nrAntre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public float calculateProgress(int nrWon, int nrGoals, int Rank) {
        int maxCompetitions = 20;
        int maxGoals = 5;
        int maxTrainings=1000;
        int maxRank = 1;
        int minRank = 10;

        float competitionsWeight = 0.4f;
        float goalsWeight = 0.2f;
        float rankWeight = 0.2f;
        float trainingsWeight=0.2f;

        float competitionsProgress = (float) nrWon / maxCompetitions;
        float goalsProgress = (float) nrGoals / maxGoals;
        float rankProgress = (float) (maxRank + minRank - Rank) / (minRank - maxRank + 1);
        //float trainingsWeight=(float) ()

        float totalProgress = competitionsProgress * competitionsWeight + goalsProgress * goalsWeight + rankProgress * rankWeight;
        totalProgress *= 100;
        totalProgress = Math.max(0, Math.min(100, totalProgress));

        return totalProgress;
    }

    public void animateProgressBar(final CircularProgressBar progressBar, final float targetProgress) {
        final float step = 0.2f;
        final int delay = 3;
        final long animationDuration = 2000;
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        long currentTime = System.currentTimeMillis();
                        long elapsedTime = currentTime - startTime;
                        float progress = (targetProgress * elapsedTime) / animationDuration;

                        progressBar.setProgress((int) progress);
                        procentaj.setText(String.format("%.0f%%", progress));

                        if (elapsedTime < animationDuration) {
                            handler.postDelayed(this, delay);
                        } else {
                            progressBar.setProgress((int) targetProgress);
                            procentaj.setText(String.format("%.0f%%", targetProgress));
                        }
                    }
                });
            }
        }, delay * 1000);
    }
}
