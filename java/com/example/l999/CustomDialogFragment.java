package com.example.l999;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends AppCompatDialogFragment {

    private String heroAthlete;

    public CustomDialogFragment(String heroAthlete) {
        this.heroAthlete = heroAthlete;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog);

        TextView textViewHeroAthlete = dialog.findViewById(R.id.textViewHeroAthlete);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        ImageView imageView = dialog.findViewById(R.id.imageView);

        textViewHeroAthlete.setText(heroAthlete);
        textViewMessage.setText("Punctele au fost adăugate la activitatea profilului!");
        imageView.setImageResource(R.drawable.succes);

        return dialog;
    }
}
