package com.example.l999;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ResultsDialog extends DialogFragment {

    private ResultsListener resultsListener;
    private String competitionId; // ID-ul competiției curente
    private int totalPoints = 0;
    private FirebaseUser currentUser;
    private String competitionDescription;
    private List<String> wonCompetitions = new ArrayList<>();
    private boolean hasSelectedResult = false;
    private int titlePointsGold = 30;
    private int titlePointsSilver=20;
    private int titlePointsBronze=10;

    private String currentUsername = "w";

    public boolean hasSelectedResult() {
        return hasSelectedResult;
    }

    public ResultsDialog() {
    }

    public ResultsDialog(String competitionId, String competitionDescription) {
        this.competitionId = competitionId;
        this.competitionDescription = competitionDescription;
    }

    private void addWonCompetition(String competitionName) {
        wonCompetitions.add(competitionName);
    }

    private void animateImage(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", -20f, 0f);
        animator.setDuration(300);
        animator.start();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_results, null);

        ImageView goldMedalImageView = dialogView.findViewById(R.id.goldMedalImageView);
        ImageView silverMedalImageView = dialogView.findViewById(R.id.silverMedalImageView);
        ImageView bronzeMedalImageView = dialogView.findViewById(R.id.bronzeMedalImageView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference competitionsRef = database.getReference("Competitions");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        goldMedalImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    animateImage(goldMedalImageView);

                    DatabaseReference goldPointsRef = competitionsRef.child(competitionId).child("points").child("gold");
                    hasSelectedResult = true;
                    goldPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int goldPoints = snapshot.getValue(Integer.class);
                                DatabaseReference currentUserRef = database.getReference("Users").child(currentUsername);
                                currentUserRef.child("userPoints").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int currentPoints = snapshot.getValue(Integer.class);
                                            int newTotalPoints = currentPoints + goldPoints;
                                            currentUserRef.child("userPoints").setValue(newTotalPoints);

                                            DatabaseReference totalPointsRef = database.getReference("Users").child(currentUsername).child("totalPoints");
                                            totalPointsRef.setValue(newTotalPoints);

                                            DatabaseReference wonComps = currentUserRef.child("wonComps").child("gold");
                                            wonComps.push().setValue(competitionDescription).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if (competitionDescription.equals("World Championships")) {
                                                        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUsername).child("title");
                                                        titleRef.setValue("Campion Mondial");
                                                    } else if (competitionDescription.equals("Olympic Games")) {
                                                        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUsername).child("title");
                                                        titleRef.setValue("Campion Olimpic");
                                                    }
                                                }
                                            });

                                            DatabaseReference CMpoints=currentUserRef.child("CMpoints");
                                            if(newTotalPoints<=350){
                                                CMpoints.setValue(newTotalPoints);
                                            }

                                            DatabaseReference JOpoints=currentUserRef.child("JOpoints");
                                            if(newTotalPoints<=750){
                                                JOpoints.setValue(newTotalPoints);
                                            }

                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                    .setTitleText("Felicări!")
                                                    .setContentText("Ai obținut " + goldPoints + " puncte!")
                                                    .setCustomImage(R.drawable.g1)
                                                    .show();

                                            /*new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Felicitări pentru obținerea medaliei de AUR!")
                                                    .setContentText("Ai obținut " + goldPoints + " puncte!")
                                                    .show();*/
                                        } else {
                                            Toast.makeText(requireContext(), "Eroare la obținerea punctelor utilizatorului!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(requireContext(), "Eroare la obținerea punctelor utilizatorului!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(requireContext(), "Eroare la obținerea punctelor pentru medalie!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "Eroare la obținerea punctelor pentru medalie!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DatabaseReference titleRef=database.getReference("Users").child(currentUsername).child("titlePoints");
                    titleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                int titleTotal=snapshot.getValue(Integer.class);
                                int titlePoints=titleTotal+titlePointsGold;

                                titleRef.setValue(titlePoints);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference wonComps = database.getReference().child("Users").child(currentUsername).child("wonComps").child("gold");
                    wonComps.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DatabaseReference totalWinsRef = database.getReference().child("Users").child(currentUsername).child("totalWins");
                            DatabaseReference allWinsRef = database.getReference().child("Users").child(currentUsername).child("allWins");

                            totalWinsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot totalWinsSnapshot) {
                                    if (totalWinsSnapshot.exists()) {
                                        int totalWins = totalWinsSnapshot.getValue(Integer.class);
                                        if (totalWins < 5) {
                                            totalWinsRef.setValue(totalWins + 1);
                                        } else {
                                            allWinsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        int allwins = snapshot.getValue(Integer.class);
                                                        if (allwins == 0) {
                                                            allWinsRef.setValue(6);
                                                        } else {
                                                            allWinsRef.setValue(allwins + 1);
                                                        }
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
                                    // Handle the error if needed
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error if needed
                        }
                    });

                }
                return true;
            }
        });


        silverMedalImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    animateImage(silverMedalImageView);

                    DatabaseReference silverPointsRef = competitionsRef.child(competitionId).child("points").child("silver");
                    hasSelectedResult = true;
                    silverPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int silverPoints = snapshot.getValue(Integer.class);
                                DatabaseReference currentUserRef = database.getReference("Users").child(currentUsername);
                                currentUserRef.child("userPoints").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int currentPoints = snapshot.getValue(Integer.class);
                                            int newTotalPoints = currentPoints + silverPoints;
                                            currentUserRef.child("userPoints").setValue(newTotalPoints);

                                            DatabaseReference totalPointsRef = database.getReference("Users").child(currentUsername).child("totalPoints");
                                            totalPointsRef.setValue(newTotalPoints);

                                            DatabaseReference wonCompsRef = currentUserRef.child("wonComps").child("silver");
                                            wonCompsRef.push().setValue(competitionDescription);

                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                    .setTitleText("Felicitări!")
                                                    .setContentText("Ai obținut " + silverPoints + " puncte!")
                                                    .setCustomImage(R.drawable.s1)
                                                    .show();

                                        } else {
                                            // Tratează situația în care nu există "userPoints" în baza de date
                                            Toast.makeText(requireContext(), "Eroare la obținerea punctelor utilizatorului!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(requireContext(), "Eroare la obținerea punctelor utilizatorului!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(requireContext(), "Eroare la obținerea punctelor pentru medalie!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "Eroare la obținerea punctelor pentru medalie!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DatabaseReference titleRef=database.getReference("Users").child(currentUsername).child("titlePoints");
                    titleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                int titleTotal=snapshot.getValue(Integer.class);
                                int titlePoints=titleTotal+titlePointsSilver;

                                titleRef.setValue(titlePoints);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                return true;
            }
        });

        bronzeMedalImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    animateImage(bronzeMedalImageView);

                    DatabaseReference bronzePointsRef = competitionsRef.child(competitionId).child("points").child("bronze");
                    hasSelectedResult = true;
                    bronzePointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int bronzePoints = snapshot.getValue(Integer.class);
                                DatabaseReference currentUserRef = database.getReference("Users").child(currentUsername);
                                currentUserRef.child("userPoints").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int currentPoints = snapshot.getValue(Integer.class);
                                            int newTotalPoints = currentPoints + bronzePoints;
                                            currentUserRef.child("userPoints").setValue(newTotalPoints);

                                            DatabaseReference totalPointsRef = database.getReference("Users").child(currentUsername).child("totalPoints");
                                            totalPointsRef.setValue(newTotalPoints);

                                            DatabaseReference wonCompsRef = currentUserRef.child("wonComps").child("zbronze");
                                            wonCompsRef.push().setValue(competitionDescription);

                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                    .setTitleText("Felicări!")
                                                    .setContentText("Ai obținut " + bronzePoints + " puncte!")
                                                    .setCustomImage(R.drawable.b1)
                                                    .show();
                                        } else {
                                            // Tratează situația în care nu există "userPoints" în baza de date
                                            Toast.makeText(requireContext(), "Eroare la obținerea punctelor utilizatorului!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(requireContext(), "Eroare la obținerea punctelor utilizatorului!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(requireContext(), "Eroare la obținerea punctelor pentru medalie!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "Eroare la obținerea punctelor pentru medalie!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DatabaseReference titleRef=database.getReference("Users").child(currentUsername).child("titlePoints");
                    titleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                int titleTotal=snapshot.getValue(Integer.class);
                                int titlePoints=titleTotal+titlePointsBronze;

                                titleRef.setValue(titlePoints);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                return true;
            }
        });

        builder.setView(dialogView)
                //.setTitle("Încarcă rezultatele")
                .setPositiveButton("Salvează", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Procesați rezultatele încărcate de utilizator și apelați metoda de ascultare a rezultatelor
                        if (resultsListener != null) {
                            resultsListener.onResultsUploaded(/* transmiterea rezultatelor încărcate */);
                        }
                    }
                })
                .setNegativeButton("Anulează", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        Dialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);
        }

        return dialog;
    }

    public void setResultsListener(ResultsListener listener) {
        this.resultsListener = listener;
    }

    public interface ResultsListener {
        void onResultsUploaded(/* parametrii rezultatelor încărcate */);
    }
}