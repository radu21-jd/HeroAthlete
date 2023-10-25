package com.example.l999;

import static java.util.Calendar.AUGUST;
import static java.util.Calendar.SEPTEMBER;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private TextView data;
       private TextView ora;
       private TextView tip;
       private TextView nrAntre;
       private TextView antre;
       private TextView hour;
       private CheckBox completed;
       private final HashMap<String, String> programariCuAntrenamenteRandom = new HashMap<>();
        public ViewHolder(View itemView) {
            super(itemView);
            data= itemView.findViewById(R.id.textViewData);
            ora=itemView.findViewById(R.id.textViewOra);
            completed=itemView.findViewById(R.id.checkBoxAntre);
            tip=itemView.findViewById(R.id.textViewType);
            nrAntre=itemView.findViewById(R.id.textViewNrAntre);
            antre=itemView.findViewById(R.id.Antres);
        }
    }

    private final List<Trainings> trainingList;

    public TrainingsAdapter(List<Trainings> trainingList) {
        this.trainingList = trainingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_antre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trainings training = trainingList.get(position);

        String numarAntrenament = String.valueOf(position + 1);
        holder.nrAntre.setText("#"+numarAntrenament);

        String dataString = training.getDayOfMonth() + "/" + (training.getMonth() + 1) + "/" + training.getYear();
        holder.data.setText("Data: " + dataString);

        String oraString = String.format("%02d:%02d", training.getHour(), training.getMinute());
        holder.ora.setText("Ora: " + oraString);

        String tipString=training.getType();
        holder.tip.setText("Tipul antrenamentului: "+tipString);
        if(tipString.equals("Judo")) {
            if (!holder.programariCuAntrenamenteRandom.containsKey(training.getId())) {
                String[] antrenamente = {
                        "\nx1000 NageKomi (proiectări)\nx15 Randori Tachi Waza\nStreching\nLocație: Centrul Olimpic de Judo\nDurata antrenemtnului: 2h",
                        "\nAnaliză Tehinco-Tactică\nAbordarea diferitelor stiluri de ruperi de prize\nx5 seturi Uchi-Chomi x20secunde\n5x Randori Tachi-Waza\nLocație: Gloria Dojo\nDurata antrenemtnului: 2h 35min",
                        "\nx100 NageKomi (proiectări) pe partea neîndemânatică x6 serii\nx250 proiectări pe partea îndemânatică x8 serii\n UchiKomi x25secunde/ serie\nLocație: Gloria Dojo\nDurata antrenemtnului: 2h 20min",
                        "\nAntrenament Techino-Tactic Ne-Waza (lupta la sol)\nAbordarea techinilor de ShimeWaza\nx8 Randori Ne-Waza x4minute/ randori\nLocație: Academia de Judo\nDurata antrenemtnului: 1h 45",
                        "\nAntrenament Randori\nx5 Randori Ne-Waza x3min/ randori\nx15 Randori Tachi-Waza x5min/ randori\nLocație: Centrul Olimpic de Judo\nDurata antrenemtnului: 2h 30min",
                        "\nAnaliza ultimelor progrese competițioanle\nDiferite procese pentru recuperarea sportivului\nLocație: Centrul Olimpic de Judo\nDurata antrenemtnului: 2h"
                };
                Random random = new Random();
                String antrenamentRandom = antrenamente[random.nextInt(antrenamente.length)];
                holder.antre.setText("Planul antrenamentului: " + antrenamentRandom);


                holder.programariCuAntrenamenteRandom.put(training.getId(), antrenamentRandom);
            } else {
                String antrenamentAlocat = holder.programariCuAntrenamenteRandom.get(training.getId());
                holder.antre.setText("Planul antrenamentului: " + antrenamentAlocat);
            }
        }
        else{
            if (!holder.programariCuAntrenamenteRandom.containsKey(training.getId())) {
                String[] antrenamente = {
                        "\nCircuit forță -5aparate x6serii x30sec/ exercițiu \nfrânghii\nîmpins la piept\ngenuflexiuni\nflotări\nabdomene",
                        "\nCircuit pentru creșterea rezistenței\nLocație: Stadionul Gloria\nx15 ture (400m/ tură)",
                        "\nCreșterea anduranței\nLocație: Stadionul Gloria\nx10 sprint/ 150m\nx15sprint în pantă/ 100m\nx5 ture alergare ușoară",
                        "\nCircuit forță\nLocație: Sala de forță Gloria\nAntrenament full body (biceps/ triceps+ picioare+ umeri+ piept)"
                };
                Random random = new Random();
                String antrenamentRandom = antrenamente[random.nextInt(antrenamente.length)];
                holder.antre.setText("Planul antrenamentului: " + antrenamentRandom);

                holder.programariCuAntrenamenteRandom.put(training.getId(), antrenamentRandom);
            } else {
                String antrenamentAlocat = holder.programariCuAntrenamenteRandom.get(training.getId());
                holder.antre.setText("Planul antrenamentului: " + antrenamentAlocat);
            }
        }

        holder.completed.setOnCheckedChangeListener(null);
        holder.completed.setChecked(training.getCompleted());
        holder.completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (isChecked) {
                    Animation fadeOut = AnimationUtils.loadAnimation(buttonView.getContext(), R.anim.fade_out);
                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Toast.makeText(buttonView.getContext(), "Antrenament finalizat! Felicitări!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            DatabaseReference completedRef = FirebaseDatabase.getInstance().getReference().child("Trainings").child("w").child(training.getId()).child("completed");
                            completedRef.setValue(true);

                            DatabaseReference nrAntre = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("trainings");
                            nrAntre.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        int numAntre = snapshot.getValue(Integer.class);
                                        nrAntre.setValue(numAntre + 1);
                                        int currentMonth = training.getMonth();
                                        Log.d("Month", "Current Month: " + currentMonth);

                                        switch (currentMonth) {
                                            case Calendar.AUGUST:
                                                DatabaseReference nrAugustRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrAug");
                                                nrAugustRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        int nrAug=snapshot.getValue(Integer.class);
                                                        nrAugustRef.setValue(nrAug+1);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                            case Calendar.SEPTEMBER:
                                                DatabaseReference nrSeptembrieRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrSept");
                                                nrSeptembrieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrSept=snapshot.getValue(Integer.class);
                                                            nrSeptembrieRef.setValue(nrSept+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                            case Calendar.OCTOBER:
                                                DatabaseReference nrOctRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrOct");
                                                nrOctRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrOct=snapshot.getValue(Integer.class);
                                                            nrOctRef.setValue(nrOct+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                            case Calendar.NOVEMBER:
                                                DatabaseReference nrNovRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrNov");
                                                nrNovRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrNovs=snapshot.getValue(Integer.class);
                                                            nrNovRef.setValue(nrNovs+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.DECEMBER:
                                                DatabaseReference nrDecRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrDec");
                                                nrDecRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrDecs=snapshot.getValue(Integer.class);
                                                            nrDecRef.setValue(nrDecs+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.JANUARY:
                                                DatabaseReference nrIanRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrIan");
                                                nrIanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrIans=snapshot.getValue(Integer.class);
                                                            nrIanRef.setValue(nrIans+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.FEBRUARY:
                                                DatabaseReference nrFebRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrFeb");
                                                nrFebRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrFebs=snapshot.getValue(Integer.class);
                                                            nrFebRef.setValue(nrFebs+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.MARCH:
                                                DatabaseReference nrMarRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrMar");
                                                nrMarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrMars=snapshot.getValue(Integer.class);
                                                            nrMarRef.setValue(nrMars+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.APRIL:
                                                DatabaseReference nrAprRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrApr");
                                                nrAprRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrAprs=snapshot.getValue(Integer.class);
                                                            nrAprRef.setValue(nrAprs+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.MAY:
                                                DatabaseReference nrMayRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrMay");
                                                nrMayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrMays=snapshot.getValue(Integer.class);
                                                            nrMayRef.setValue(nrMays+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.JUNE:
                                                DatabaseReference nrJunRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrJun");
                                                nrJunRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrJuns=snapshot.getValue(Integer.class);
                                                            nrJunRef.setValue(nrJuns+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;

                                            case Calendar.JULY:
                                                DatabaseReference nrJulRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("nrTrainings").child("nrJul");
                                                nrJulRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            int nrJuls=snapshot.getValue(Integer.class);
                                                            nrJulRef.setValue(nrJuls+1);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            // act. listei si notif adapter-ului
                            trainingList.remove(currentPosition);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    holder.itemView.startAnimation(fadeOut);
                } else {
                    DatabaseReference completedRef = FirebaseDatabase.getInstance().getReference().child("Trainings").child("w").child(training.getId()).child("completed");
                    completedRef.setValue(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }
}
