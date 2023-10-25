package com.example.l999;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class gl2Dialog extends DialogFragment {
    private Toolbar toolbar3;
    private TextView descriere;
    private TextView nrComp;
    private TextView msg;
    private CircularProgressBar progressBar4;
    private int wonComps;
    private int totalWins;

    public gl2Dialog() {
        //
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.gl2_dialog, null);

        Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);

        descriere = (TextView) view.findViewById(R.id.descriere);
        descriere.startAnimation(fadeIn);
        nrComp = (TextView) view.findViewById(R.id.nrComp);
        Animation fadeInWithDelay1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay1.setStartOffset(1000);
        nrComp.startAnimation(fadeInWithDelay1);
        progressBar4 = (CircularProgressBar) view.findViewById(R.id.progressBar4);
        Animation fadeInWithDelay2 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay2.setStartOffset(1000);
        progressBar4.startAnimation(fadeInWithDelay2);

        msg=(TextView) view.findViewById(R.id.textViewMsg);
        Animation fadeInWithDelay3 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay3.setStartOffset(2000);
        msg.startAnimation(fadeInWithDelay3);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        builder.setView(view)
                .setPositiveButton("Salveaza", (dialog, which) -> {
                })
                .setNegativeButton("Anuleaza", (dialog, which) -> {
                });


        DatabaseReference isCompleted=database.getReference().child("Users").child("w").child("goals").child("-NabCSRIpS7fvoeyq1rb").child("completed");
        DatabaseReference isActive=database.getReference().child("Users").child("w").child("goals").child("-NabCSRIpS7fvoeyq1rb").child("active");
        DatabaseReference goalWinsRef=database.getReference().child("Users").child("w").child("totalWins");
        DatabaseReference titleRef=database.getReference().child("Users").child("w").child("title");
        goalWinsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    totalWins=snapshot.getValue(Integer.class);
                    if(totalWins==5){
                        isCompleted.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    isCompleted.setValue(true);
                                    isActive.setValue(false);
                                    titleRef.setValue("Win Master");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getActivity() != null && isAdded()) {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Goal complet!")
                                            .setContentText("Ați obținut titlul [WIN MASTER]")
                                            .show();
                                }
                            }
                        }, 2500);
                    }
                    updateProgressBar();

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
        String message;
        int maxProgress = 5;
        int progress = (int) ((totalWins / (float) maxProgress) * 100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar4.setProgressWithAnimation(progress, 1000L);
            }
        }, 1000);

        nrComp.setText(totalWins + "/" + maxProgress);
        if (totalWins == 0) {
            message = "";
        } else if (totalWins == 1) {
            message = "Continua să muncești!";
        } else if (totalWins == 3) {
            message = "Vă apropiați de obiectiv!";
        } else if (totalWins == 4) {
            message = "Foarte aproape! Continuați!";
        } else {
            message = "";
        }
        msg.setText(message);
    }

}