package com.example.l999;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class gl3Dialog extends DialogFragment {
    private TextView textView8;
    private TextView textView10;
    private TextView textView13;
    private TextView textView12;
    private Toolbar toolbar4;
    private CircularProgressBar progressBar5;
    int points;
    int CMpoints;
    private TextView textView11;

    public gl3Dialog() {
        //
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.gl3_dialog, null);

        Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);

        //toolbar4=(Toolbar) view.findViewById(R.id.toolbar4);
        //textView8 = (TextView) view.findViewById(R.id.textView8);
        textView10=(TextView) view.findViewById(R.id.textView10);
        textView10.startAnimation(fadeIn);

        textView13=(TextView) view.findViewById(R.id.textView13);
        Animation fadeIn1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeIn1.setStartOffset(3000);
        textView13.startAnimation(fadeIn1);

        textView11=(TextView) view.findViewById(R.id.textView11);
        Animation fadeIn2 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeIn2.setStartOffset(1300);
        textView11.startAnimation(fadeIn2);

        textView12=(TextView) view.findViewById(R.id.textView12);
        Animation fadeIn3 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeIn3.setStartOffset(2000);
        textView12.startAnimation(fadeIn3);

        textView11=(TextView) view.findViewById(R.id.textView11);

        progressBar5=(CircularProgressBar) view.findViewById(R.id.progressBar5);
        Animation fadeIn4 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeIn4.setStartOffset(2000);
        progressBar5.startAnimation(fadeIn4);

        textView11=(TextView) view.findViewById(R.id.textView11);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setView(view)

                .setPositiveButton("Salveaza", (dialog, which) -> {

                })
                .setNegativeButton("Anuleaza", (dialog, which) -> {
                });

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference pointsCM = database.getReference().child("Users").child("w").child("CMpoints");
        DatabaseReference token=database.getReference().child("Users").child("w").child("CMtoken");
        DatabaseReference completed=database.getReference().child("Users").child("w").child("goals").child("-NabCSRIpS7fvoeyq1rc").child("completed");
        pointsCM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CMpoints=snapshot.getValue(Integer.class);
                    textView11.setText("Numar actual puncte:" + CMpoints);
                    updateProgressBar();
                }
                if(CMpoints==350){
                    token.setValue(true);
                    completed.setValue(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isAdded()) {
                                new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Goal complet!")
                                        .setContentText("Ați obținut calificarea la Campionatele Mondiale!")
                                        .show();
                            }
                        }
                    }, 3500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference isComplete=database.getReference().child("Users").child("w").child("goals").child("-NabCSRIpS7fvoeyq1rb").child("completed");
        isComplete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Boolean completed=snapshot.getValue(Boolean.class);
                    if(completed==true){
                        DatabaseReference nrObjs=database.getReference().child("Users").child("w").child("objsCompleted");
                        nrObjs.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    int nrObj=snapshot.getValue(Integer.class);
                                    nrObjs.setValue(nrObj+1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Dialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);
        }

        return dialog;
    }
    private void updateProgressBar() {
        final int maxProgress = 350;
        final int progress = (int) ((CMpoints / (float) maxProgress) * 100);
        final int pointsRemaining = maxProgress - CMpoints;
        String message;

        if (CMpoints == 0) {
            message = ""; // Dacă începeți cu 0/350 puncte, lăsați mesajul gol
        } else if (pointsRemaining > 100) {
            message = "Continua să muncești!";
        } else if (pointsRemaining > 50) {
            message = "Vă apropiați de obiectiv!";
        } else if (pointsRemaining > 0) {
            message = "Foarte aproape! Continuați!";
        } else {
            message = "";
        }
        textView13.setText(message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar5.setProgressWithAnimation(progress, 1000L);
                textView12.setText(CMpoints + "/" + maxProgress);
            }
        }, 2000); // Delay de 1 secundă (1000 milisecunde)
    }
}
