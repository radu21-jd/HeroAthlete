package com.example.l999;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class gl4Dialog extends DialogFragment {
    private TextView textView14;
    private TextView textView15;
    private TextView textView16;
    private TextView textView18;
    private TextView textView17;
    private ProgressBar progressBar6;
    int points;

    public gl4Dialog() {
        //
    }

    public Dialog onCreateDialog(@NonNull Bundle savedInstaceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.gl4_dialog, null);

        textView14 = (TextView) view.findViewById(R.id.textView14);
        textView15 = (TextView) view.findViewById(R.id.textView15);
        textView16 = (TextView) view.findViewById(R.id.textView16);
        textView17 = (TextView) view.findViewById(R.id.textView17);
        textView18 = (TextView) view.findViewById(R.id.textView18);
        progressBar6 = (ProgressBar) view.findViewById(R.id.progressBar6);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Obtine calificarea la JO!");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pointsRef = database.getReference().child("Users").child("w").child("JOpoints");
        DatabaseReference isComplete = database.getReference().child("Users").child("w").child("goals").child("-NabCSRIpS7fvoeyq1rd").child("completed");
        DatabaseReference joToken = database.getReference().child("Users").child("w").child("JOtoken");
        DatabaseReference objsRef = database.getReference().child("Users").child("w").child("objsCompleted");
        pointsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    points = snapshot.getValue(Integer.class);
                    textView16.setText("Puncte utilizator: " + points);
                    if (points == 750) {
                        joToken.setValue(true);
                        isComplete.setValue(true);
                        objsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    int objs = snapshot.getValue(Integer.class);
                                    objsRef.setValue(objs + 1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    updateProgressBar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder.setView(view)

                .setPositiveButton("Salveaza", (dialog, which) -> {

                })
                .setNegativeButton("Anuleaza", (dialog, which) -> {
                });

        return builder.create();
    }

    private void updateProgressBar() {
        int maxProgress = 750;
        int progress = (int) ((points / (float) maxProgress) * 100);
        progressBar6.setProgress(progress);
        textView17.setText(points + "/" + maxProgress);
        int pointsRemaining = maxProgress - points;
        String message;

        if (points == 0) {
            message = "";
        } else if (pointsRemaining > 100) {
            message = "Continua să muncești!";
        } else if (pointsRemaining > 50) {
            message = "Vă apropiați de obiectiv!";
        } else if (pointsRemaining > 0) {
            message = "Foarte aproape! Continuați!";
        } else {
            message = "Felicitări! Ați atins obiectivul!";
        }
        textView18.setText(message);
    }
}
