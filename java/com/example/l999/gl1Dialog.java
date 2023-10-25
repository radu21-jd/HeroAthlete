package com.example.l999;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class gl1Dialog extends DialogFragment {
    private Spinner spinner2;
    private String[] zile_array = {"3 zile", "5 zile", "7 zile"};
    private FirebaseDatabase data=FirebaseDatabase.getInstance();
    private String id="-NabCSRFKE-CH6kdlTmH";
    private TextView nrkg;
    private TextView kgprop;
    private EditText EditTextNumber;
    private EditText EditTextNumber2;
    private EditText EditTextNumber3;
    private EditText EditTextNumber4;
    private Integer actual_kg;
    private Integer wanted_kg;
    private String zileSelected;
    private CircularProgressBar progressBar2;
    private int initialDiffKg;
    private TextView textViewClock;
    private ProgressBar progressBar3;
    private Long diffKg;
    long millisUntilFinished;
    int currentDiffKg;
    private TextView progresT;
    private TextView textBar;
    private TextView clock;

    private ArrayAdapter<String> adapter;
    private Timer timer;
    private TimerTask timerTask;
    private CountDownTimer countDownTimer;
    public gl1Dialog() {
    }

    private BroadcastReceiver countdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long millisUntilFinished = intent.getLongExtra("millisUntilFinished", 0);

            long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
            String timeLeft = days + " zile, " + hours + " ore, " + minutes + " minute, " + seconds + " secunde";

            textViewClock.setText(timeLeft);
        }
    };

    /*private BroadcastReceiver countdownStoppedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long stoppedAt = intent.getLongExtra("stoppedAt", 0);

            long days = TimeUnit.MILLISECONDS.toDays(stoppedAt);
            long hours = TimeUnit.MILLISECONDS.toHours(stoppedAt) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(stoppedAt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(stoppedAt));
            long seconds = TimeUnit.MILLISECONDS.toSeconds(stoppedAt) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(stoppedAt));
            String timeLeft = days + " zile, " + hours + " ore, " + minutes + " minute, " + seconds + " secunde";

            textViewClock.setText(timeLeft);
        }
    };*/
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void stopCountdownService() {
        if (isServiceRunning(CountdownService.class)) {
            Intent intent = new Intent(getActivity(), CountdownService.class);
            getActivity().stopService(intent);}}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.gl1_dialog, null);

        Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);

        //Toolbar toolbar = view.findViewById(R.id.toolbar);
        //toolbar.setTitle("Slabire");
        nrkg=(TextView) view.findViewById(R.id.textViewKg);
        //nrkg.startAnimation(fadeIn);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, zile_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        EditTextNumber = (EditText) view.findViewById(R.id.editTextNumber);
        EditTextNumber2=(EditText)view.findViewById(R.id.editTextNumber2);
        EditTextNumber3=(EditText)view.findViewById(R.id.editTextNumber3);

        EditTextNumber4=(EditText)view.findViewById(R.id.editTextNumber4);
        Animation fadeInWithDelay3 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay3.setStartOffset(2000);
        EditTextNumber4.startAnimation(fadeInWithDelay3);

        progressBar2=(CircularProgressBar)view.findViewById(R.id.progressBar2);
        Animation fadeInWithDelay2 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay2.setStartOffset(2000);
        progressBar2.startAnimation(fadeInWithDelay2);

        textViewClock=(TextView)view.findViewById(R.id.textViewClock);
        Animation fadeInWithDelay5 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay5.setStartOffset(2200);
        textViewClock.startAnimation(fadeInWithDelay5);


        progresT=view.findViewById(R.id.textViewProgrs);
        Animation fadeInWithDelay1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay1.setStartOffset(1500);
        progresT.startAnimation(fadeInWithDelay1);

        textBar=view.findViewById(R.id.textViewProgressBar);
        Animation fadeInWithDelay4 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay4.setStartOffset(2000);
        textBar.startAnimation(fadeInWithDelay4);

        clock=view.findViewById(R.id.textViewCL);
        Animation fadeInWithDelay6 = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInWithDelay6.setStartOffset(2200);
        clock.startAnimation(fadeInWithDelay6);




        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view)
                .setPositiveButton("Salveaza", (dialog, which) -> {
                    onClickSave();

                })
                .setNegativeButton("Anuleaza", (dialog, which) -> {
                });

        Dialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);
        }

        return dialog;
    }

    private void onClickSave() {
        if (EditTextNumber2 != null) {
            String wanted_kgStr = EditTextNumber2.getText().toString();
            if (!wanted_kgStr.isEmpty()) {
                wanted_kg = Integer.parseInt(wanted_kgStr);
                DatabaseReference kgwantedRef = data.getReference().child("Users").child("w").child("goals").child(id).child("wanted_kg");
                kgwantedRef.setValue(wanted_kg);
            }
        }

        if (EditTextNumber != null) {
            String actual_kg_str = EditTextNumber.getText().toString();
            if (!actual_kg_str.isEmpty()) {
                actual_kg = Integer.parseInt(actual_kg_str);
                DatabaseReference kgRef = data.getReference().child("Users").child("w").child("goals").child(id).child("actual_kg");
                kgRef.setValue(actual_kg);
                DatabaseReference initialdifRef= data.getReference().child("Users").child("w").child("goals").child(id).child("initial_diff_kg");
                initialdifRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            int iDif=actual_kg-wanted_kg;
                            initialdifRef.setValue(iDif);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }


        if (spinner2 != null) {
            zileSelected = (String) spinner2.getSelectedItem();

            DatabaseReference daysRef = data.getReference().child("Users").child("w").child("goals").child(id).child("days");
            daysRef.setValue(zileSelected);
            daysRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Oprirea serviciul dacă intrarea a fost ștearsă
                        stopCountdownService();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestionare eroare
                }
            });

            if (zileSelected != null && zileSelected.length() >= 4) {
                String zileString = zileSelected.substring(0, zileSelected.length() - 4).trim();
                int zile = Integer.parseInt(zileString);
                millisUntilFinished = zile * 24 * 60 * 60 * 1000;}
        }

        if (!isServiceRunning(CountdownService.class)) {
            Intent intent = new Intent(getActivity(), CountdownService.class);
            intent.putExtra("millisUntilFinished", millisUntilFinished);
            getActivity().startService(intent);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(countdownReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseReference actualKgRef = data.getReference().child("Users").child("w").child("goals").child(id).child("actual_kg");
        DatabaseReference wantedKgRef = data.getReference().child("Users").child("w").child("goals").child(id).child("wanted_kg");
        DatabaseReference newKgRef = data.getReference().child("Users").child("w").child("goals").child(id).child("diff_kg");

        actualKgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer currentKg = dataSnapshot.getValue(Integer.class);
                    if (currentKg != null) {
                        EditTextNumber3.setText(String.valueOf(currentKg));
                        wantedKgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Integer wantedKg = snapshot.getValue(Integer.class);
                                    if (wantedKg != null) {
                                        DatabaseReference initialDiffKgRef = data.getReference().child("Users").child("w").child("goals").child(id).child("initial_diff_kg");
                                        initialDiffKgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    initialDiffKg = snapshot.getValue(Integer.class);
                                                    if (initialDiffKg != 0) {
                                                        currentDiffKg = currentKg - wantedKg;
                                                        newKgRef.setValue(currentDiffKg);
                                                        updateProgressBar();

                                                        if (currentDiffKg != 0) {
                                                            EditTextNumber4.setText(String.valueOf(currentDiffKg));
                                                        } else {
                                                            DatabaseReference isComplete = data.getReference().child("Users").child("w").child("goals").child("-NabCSRFKE-CH6kdlTmH").child("completed");
                                                            isComplete.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists())
                                                                    {
                                                                        isComplete.setValue(true);
                                                                        long stoppedTime = getStoppedTime();
                                                                        String timeLeft = formatTime(stoppedTime);

                                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                .setTitleText("Succes!")
                                                                                .setContentText("Goal completat!")
                                                                                //.setContentText("Goal completat în: " + timeLeft)
                                                                                .show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Error handling...
                                            }
                                        });
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

            }
        });

        DatabaseReference kgwantedRef = data.getReference().child("Users").child("w").child("goals").child(id).child("wanted_kg");
        kgwantedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer wanted_kg = dataSnapshot.getValue(Integer.class);
                if (wanted_kg != null) {
                    EditTextNumber2.setText(String.valueOf(wanted_kg));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });

        DatabaseReference daysRef = data.getReference().child("Users").child("w").child("goals").child(id).child("days");
        daysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String zileSelected = dataSnapshot.getValue(String.class);
                    int spinnerPosition = adapter.getPosition(zileSelected);
                    spinner2.setSelection(spinnerPosition);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(countdownReceiver, new IntentFilter("COUNTDOWN_UPDATE"));
        //LocalBroadcastManager.getInstance(requireContext()).registerReceiver(countdownStoppedReceiver, new IntentFilter("COUNTDOWN_STOPPED"));

        long stoppedTime = getStoppedTime();
        if (stoppedTime > 0) {
            String timeLeft = formatTime(stoppedTime);
            //textViewClock.setText(timeLeft);
        }
    }
    private long getStoppedTime() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("timer_prefs", MODE_PRIVATE);
        return sharedPreferences.getLong("stopped_time", -1);
    }



    private String formatTime(long millisUntilFinished) {
        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
        return days + " zile, " + hours + " ore, " + minutes + " minute, " + seconds + " secunde";
    }
    private void updateProgressBar() {
        String message;
        float percentage = ((float) (initialDiffKg - currentDiffKg) / initialDiffKg) * 100;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar2.setProgressWithAnimation(percentage, 1000L);
            }
        }, 2200);
    }
}