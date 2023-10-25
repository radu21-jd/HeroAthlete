package com.example.l999;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ProgramareAntrenamente extends DialogFragment {

    public interface ScheduleTrainingListener {
        void onScheduleTraining(int year, int month, int dayOfMonth, int hour, int minute, String type);
    }

    private ScheduleTrainingListener listener;
    private int year, month, dayOfMonth, hour, minute;

    public ProgramareAntrenamente(ScheduleTrainingListener listener, int year, int month, int dayOfMonth) {
        this.listener = listener;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hour=hour;
        this.minute=minute;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_antre, null);

        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        Spinner trainingTypeSpinner = view.findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.training_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainingTypeSpinner.setAdapter(adapter);


        builder.setView(view)
                //.setMessage("Doriți să programați un antrenament pentru această dată?")
                .setPositiveButton("Da", (dialog, which) -> {
                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();
                    String type = trainingTypeSpinner.getSelectedItem().toString();
                    listener.onScheduleTraining(year, month, dayOfMonth, hour, minute, type);
                })
                .setNegativeButton("Nu", (dialog, which) -> {
                    // Anulare
                });
        Dialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);
        }

        return dialog;
    }

}

