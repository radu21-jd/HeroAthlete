package com.example.l999;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.l999.Competition;
import com.example.l999.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InscriereDialog extends DialogFragment {

    private Competition competition;
    private Spinner spinner;
    private InscriptionListener inscriptionListener;

    private DatabaseReference ticketsRef;

    private List<Ticket> ticketList;
    private TicketsCompAdapter adapter;
    private String competitionName;



    public InscriereDialog() {
        // Constructor gol necesar pentru utilizarea DialogFragment
    }

    public static InscriereDialog newInstance(Competition competition) {
        InscriereDialog inscriereDialog = new InscriereDialog();

        Bundle args = new Bundle();
        args.putSerializable("competition", competition);
        inscriereDialog.setArguments(args);

        return inscriereDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_inscriere, null);

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
        TextView nameConcursTextView = dialogView.findViewById(R.id.name_concurs);
        Spinner spinnerCategories = dialogView.findViewById(R.id.spinner);
        Spinner spinnerGender = dialogView.findViewById(R.id.spinner_gender);

        ArrayAdapter<CharSequence> adapterCategories = ArrayAdapter.createFromResource(requireContext(),
                R.array.weight_categories, android.R.layout.simple_spinner_item);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapterCategories);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(requireContext(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        spinner = spinnerCategories;

        Competition competition = (Competition) getArguments().getSerializable("competition");
        String competitionName = competition.getDescription();
        nameConcursTextView.setText(competitionName);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference competitionsRef = database.getReference("Competitions");
        ticketsRef = database.getReference("Tickets");

        competitionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot competitionSnapshot : dataSnapshot.getChildren()) {
                    String competitionId = competitionSnapshot.getKey();
                    if (competitionId.equals(competition.getId())) {
                        String competitionName = competitionSnapshot.child("description").getValue(String.class);
                        nameConcursTextView.setText(competitionName);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        builder.setView(dialogView)
                .setPositiveButton("Înscrie-te", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString();
                        String email = emailEditText.getText().toString();
                        String selectedCategory = spinner.getSelectedItem().toString();
                        String selectedGender = spinner.getSelectedItem().toString();

                        performInscription(name, email, selectedCategory, competition);
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


    public void setInscriptionListener(InscriptionListener listener) {
        this.inscriptionListener = listener;
    }

    public interface InscriptionListener {
        void onInscriptionCompleted(Competition competition);
    }

    private void performInscription(String name, String club, String category, Competition competition) {
        String competitionName = competition.getDescription();
        Ticket ticket = new Ticket(name, club, category, competitionName);
        String ticketId = ticketsRef.push().getKey();
        ticketsRef.child(ticketId).setValue(ticket);
        if (adapter != null) {
            ticketList.add(ticket);
            adapter.notifyDataSetChanged();
        }

        if (inscriptionListener != null) {
            inscriptionListener.onInscriptionCompleted(competition);
        }
    }
}
