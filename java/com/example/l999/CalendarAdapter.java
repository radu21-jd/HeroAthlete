package com.example.l999;

import static android.content.ContentValues.TAG;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<Competition> competitionList;
    private AppCompatActivity activity;
    private HashMap<String, Boolean> registrationStatusMap = new HashMap<>();
    private HashMap<String, Boolean> resultButtonStatusMap = new HashMap<>();
    private String currentUsername; // ID-ul utilizatorului curent (username)


    public CalendarAdapter(AppCompatActivity activity, List<Competition> competitionList) {
        this.activity = activity;
        this.competitionList = competitionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Competition competition = competitionList.get(position);
        holder.bind(competition);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
       holder.itemView.startAnimation(fadeInAnimation);
    }

    @Override
    public int getItemCount() {
        return competitionList.size();
    }

    public void setCompetitionList(List<Competition> newData) {
        competitionList.clear();
        competitionList.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dateView;
        private ImageView imageView;
        private View coverFrame;
        private Button inscriptionButton;
        private boolean token;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            inscriptionButton = itemView.findViewById(R.id.inscriptionButton);
            dateView=itemView.findViewById(R.id.textViewDate);
            coverFrame=itemView.findViewById(R.id.coverFrame);
        }

        public void bind(final Competition competition) {
            titleTextView.setText(competition.getDescription());
            dateView.setText("Data: "+competition.getDate());
            Picasso.get().load(competition.getImage()).into(imageView);

            DatabaseReference isInscrisRef = FirebaseDatabase.getInstance().getReference().child("Competitions").child(competition.getId()).child("isInscris");
            DatabaseReference userToken = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("CMtoken");
            DatabaseReference TokenJO = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("JOtoken");

            isInscrisRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.getValue(Boolean.class)) {
                        String competitionDateString = competition.getDate();

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());
                            Date competitionDate = sdf.parse(competitionDateString);
                            Calendar currentCalendar = Calendar.getInstance();
                            String currentDateString = sdf.format(currentCalendar.getTime());
                            Date currentDate = sdf.parse(currentDateString);
                            long diff = competitionDate.getTime() - currentDate.getTime();
                            long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                            if (diffDays > 0) {
                                String daysRemaining ="Competiția începe peste " + diffDays + " zile!";
                                inscriptionButton.setText(daysRemaining);
                                inscriptionButton.setEnabled(false);
                                coverFrame.setVisibility(View.VISIBLE);
                                coverFrame.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Competiția înca nu a început!")
                                                .setContentText(daysRemaining)
                                                .setConfirmText("OK")
                                                .show();
                                    }
                                });
                            } else {
                                coverFrame.setVisibility(View.GONE);
                                inscriptionButton.setText("Rezultate");
                                inscriptionButton.setEnabled(true);
                                inscriptionButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ResultsDialog resultsDialog = new ResultsDialog(competition.getId(), competition.getDescription());
                                        resultsDialog.setResultsListener(new ResultsDialog.ResultsListener() {
                                            @Override
                                            public void onResultsUploaded() {
                                                DatabaseReference competitionRef = FirebaseDatabase.getInstance().getReference().child("Competitions").child(competition.getId());
                                                competitionRef.child("isFinished").setValue(true);
                                            }
                                        });
                                        resultsDialog.show(activity.getSupportFragmentManager(), "ResultsDialog");
                                    }
                                });
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        userToken.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    boolean userHasToken = snapshot.getValue(Boolean.class);
                                    if (competition.hasToken() && !userHasToken) {
                                        inscriptionButton.setText("Nu ai obținut token-ul CM necesar pentru înscriere!");
                                        inscriptionButton.setEnabled(false);
                                        return;
                                    }
                                }
                                TokenJO.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot joSnapshot) {
                                        boolean userHasJoToken = joSnapshot.exists() && joSnapshot.getValue(Boolean.class);
                                        if (competition.joToken() && !userHasJoToken) {
                                            inscriptionButton.setText("Nu ai obținut token-ul JO necesar pentru înscriere!");
                                            inscriptionButton.setEnabled(false);
                                        } else {
                                            inscriptionButton.setText("Înscriere");
                                            inscriptionButton.setEnabled(true);
                                            inscriptionButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    InscriereDialog inscriptionDialog = InscriereDialog.newInstance(competition);
                                                    inscriptionDialog.setInscriptionListener(new InscriereDialog.InscriptionListener() {
                                                        @Override
                                                        public void onInscriptionCompleted(Competition competition) {
                                                            DatabaseReference competitionRef = FirebaseDatabase.getInstance().getReference().child("Competitions").child(competition.getId());
                                                            competitionRef.child("isInscris").setValue(true);
                                                            notifyDataSetChanged();
                                                        }
                                                    });
                                                    inscriptionDialog.show(activity.getSupportFragmentManager(), "InscriereDialog");
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        }


        public void setResultButtonStatus(String competitionId, boolean hasResults) {
            resultButtonStatusMap.put(competitionId, hasResults);
            notifyDataSetChanged();
        }


        private void openInscriptionDialog(Competition competition) {
            InscriereDialog inscriptionDialog = InscriereDialog.newInstance(competition);
            inscriptionDialog.show(activity.getSupportFragmentManager(), "InscriereDialog");
        }
    }
}