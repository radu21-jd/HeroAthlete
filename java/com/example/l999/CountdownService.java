package com.example.l999;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class CountdownService extends Service {
    private CountDownTimer countDownTimer;
    private long totalTime;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            long millisUntilFinished = intent.getLongExtra("millisUntilFinished", 0);
            totalTime = millisUntilFinished;
            countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
                public void onTick(long millisUntilFinished) {
                    //int progress = (int) ((totalTime - millisUntilFinished) * 100 / totalTime);
                    Intent intent = new Intent("COUNTDOWN_UPDATE");
                    intent.putExtra("millisUntilFinished", millisUntilFinished);
                    LocalBroadcastManager.getInstance(CountdownService.this).sendBroadcast(intent);
                }
                public void onFinish() {
                }
            }.start();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("goals").child("-NabCSRFKE-CH6kdlTmH").child("completed");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Boolean ok = dataSnapshot.getValue(Boolean.class);
                        if (ok != null && ok) {
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                saveStoppedTime(totalTime);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            return START_REDELIVER_INTENT;
        } else {
            return START_NOT_STICKY;
        }
    }
    private void showNotification() {
    }

    private void saveStoppedTime(long millisUntilFinished) {
        SharedPreferences sharedPreferences = getSharedPreferences("timer_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("stopped_time", millisUntilFinished);
        editor.apply();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
