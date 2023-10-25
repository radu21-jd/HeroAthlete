package com.example.l999;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TitluriAdapter extends RecyclerView.Adapter<TitluriAdapter.ViewHolder> {

    private List<Titluri> listaTitluri;
    private Context context;
    int uPoints;
    int tPoints;
    int shopPoints;
    String activeTitle;


    public TitluriAdapter(Context context, List<Titluri> listaTitluri) {
        this.context = context;
        this.listaTitluri = listaTitluri;

        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("title");
        titleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    activeTitle = snapshot.getValue(String.class);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_titluri, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Titluri titluCurent = listaTitluri.get(position);
        holder.titlu.setText(titluCurent.getName());
        if ("Campion Mondial".equals(titluCurent.getName()) || "Campion Olimpic".equals(titluCurent.getName())) {
            holder.points.setText("Token");
        }
        else if("WIN MASTER".equals(titluCurent.getName())){
            holder.points.setText("Obiectiv complet");
        }
        else {
            holder.points.setText(String.valueOf(titluCurent.getPoints()));
        }


        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("title");
        DatabaseReference titleRef1 = FirebaseDatabase.getInstance().getReference().child("Titluri").child(titluCurent.getId()).child("isBuy");
        DatabaseReference userPoints=FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("titlePoints");
        DatabaseReference titlePoints=FirebaseDatabase.getInstance().getReference().child("Titluri").child(titluCurent.getId()).child("points");


        if (titluCurent.getIsBuy()) {
            holder.buy.setText("");
            holder.buy.setBackgroundResource(R.drawable.biff);
            //holder.titlu.setBackgroundResource(R.drawable.animated_border);

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogTitluri dialogTitluri = new DialogTitluri(context);
                    dialogTitluri.showDialog("Titluri", "Achizitioneaza titlul.", titluCurent.getId(), titluCurent.getName());

                    //titleRef.setValue(titluCurent.getName());
                }
            });
        } else {
            holder.buy.setText("Buy");
            holder.buy.setBackgroundResource(R.drawable.ticket_shape);
            //holder.titlu.setBackground(null);

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userPoints.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                uPoints = snapshot.getValue(Integer.class);
                                titlePoints.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotTitlePoints) {
                                        if(snapshotTitlePoints.exists()) {
                                            tPoints = snapshotTitlePoints.getValue(Integer.class);

                                            shopPoints = uPoints-tPoints;
                                            if(shopPoints >= 0) {
                                                userPoints.setValue(shopPoints);
                                                titleRef1.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            titluCurent.setIsBuy(true);
                                                            notifyDataSetChanged();
                                                        }
                                                    }
                                                });

                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Titlu achzitionat cu succes!")
                                                        .setContentText("Punctele au fost actualizate!")
                                                        .show();
                                            }else if(titluCurent.getName().equals("Campion Olimpic")) {
                                                if (titluCurent.getTokenJO()) {
                                                    titleRef1.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                titluCurent.setIsBuy(true);
                                                                notifyDataSetChanged();

                                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setTitleText("CAMPION OLIMPIC!")
                                                                        .setContentText("Felicitari pentru obtinerea titlului!" + "\n\n" + "@heroAthlete")
                                                                        .show();
                                                            }
                                                        }
                                                    });
                                                }else{
                                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Token inexistent!")
                                                            .setContentText("Trebuie sa devii Campion Olimpic pentru deblocarea acestui titlu!" + "\n\n" + "@heroAthlete")
                                                            .show();
                                                }
                                            }
                                            else if(titluCurent.getName().equals("Campion Mondial")) {
                                                if (titluCurent.getTokenCM()) {
                                                    titleRef1.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                titluCurent.setIsBuy(true);
                                                                notifyDataSetChanged();

                                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setTitleText("CAMPION MONDIAL!")
                                                                        .setContentText("Felicitari pentru obtinerea titlului!" + "\n\n" + "@heroAthlete")
                                                                        .show();
                                                            }
                                                        }
                                                    });
                                                }else{
                                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Token inexistent!")
                                                            .setContentText("Trebuie sa devii Campion Mondial pentru deblocarea acestui titlu!" + "\n\n" + "@heroAthlete")
                                                            .show();
                                                }
                                            }
                                            else if(titluCurent.getName().equals("WIN MASTER")) {
                                                if (titluCurent.getTokenObj()) {
                                                    titleRef1.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                titluCurent.setIsBuy(true);
                                                                notifyDataSetChanged();

                                                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setTitleText("WIN MASTER!")
                                                                        .setContentText("Felicitari pentru obtinerea titlului!" + "\n\n" + "@heroAthlete")
                                                                        .show();
                                                            }
                                                        }
                                                    });
                                                }else{
                                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Achizitionare restricționată!")
                                                            .setContentText("Trebuie să completezi obiectivul WIN Master pentru deblocarea titlului!" + "\n\n" + "@heroAthlete")
                                                            .show();
                                                }
                                            }
                                            else{
                                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Puncte insuficiente!")
                                                        .setContentText("Mai ai nevoie de " + Math.abs(shopPoints) + " puncte.")
                                                        .show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }

        if (titluCurent.getIsActive() && titluCurent.getName().equals(activeTitle)) {
            holder.titlu.setBackgroundResource(R.drawable.animated_border);
        } else {
            holder.titlu.setBackground(null);
        }


    }

    @Override
    public int getItemCount() {
        return listaTitluri.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titlu;
        TextView points;
        Button buy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titlu = itemView.findViewById(R.id.textViewTitluri);
            points =itemView.findViewById(R.id.textViewPoints);
            buy=itemView.findViewById(R.id.buyButton);
        }
    }
}