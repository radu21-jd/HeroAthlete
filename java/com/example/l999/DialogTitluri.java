package com.example.l999;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogTitluri {

    private Context context;

    public DialogTitluri(Context context) {
        this.context = context;
    }

    DatabaseReference titleRef= FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("title");
    public void showDialog(String title, String message, String titluId, String titluName) {

        DatabaseReference activeRef = FirebaseDatabase.getInstance().getReference().child("Titluri").child(titluId).child("isActive");
        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("title");
        activeRef.setValue(true);
        new AlertDialog.Builder(context)
                .setTitle("Activare titlu")
                .setMessage("Doresti sa activezi acest titlu?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        titleRef.setValue(titluName);
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Titlu activat!")
                                .setContentText("@heroAthlete!")
                                .show();
                    }
                })
                .setNegativeButton("Anulează", null)
                .show();
    }
}
