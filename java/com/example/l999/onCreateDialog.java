package com.example.l999;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class onCreateDialog extends DialogFragment {
    //var
    public Dialog onCreateDialog(Bundle savedInstanceStace){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.oncreate_dialog, null);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerCountry);
        List<String> countries = new ArrayList<>();
        countries.add("Japonia");
        countries.add("Romania");
        countries.add("USA");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, countries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        EditText name=(EditText)view.findViewById(R.id.editTextText);
        TextView tara=(TextView)view.findViewById(R.id.textViewTara);
        TextView cat=(TextView)view.findViewById(R.id.textViewCat);
        Spinner spinnerCategory= (Spinner)view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.weight_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        Spinner spinnerVarsta=(Spinner)view.findViewById(R.id.spinnerVarsta);
        ArrayAdapter<CharSequence> varstaAdapter=ArrayAdapter.createFromResource(getActivity(), R.array.categories_array1, android.R.layout.simple_spinner_item);
        varstaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVarsta.setAdapter(varstaAdapter);
        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImagesRef = storageRef.child("images/" + UUID.randomUUID().toString());

        ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                imageViewProfile.setImageURI(imageUri);

                                // Încărcați imaginea în Firebase Storage
                                UploadTask uploadTask = profileImagesRef.putFile(imageUri);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Obțineți URL-ul imaginii încărcate
                                                profileImagesRef.getDownloadUrl()
                                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String imageUrl = uri.toString();

                                                                DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Profile").child("w").child("image");
                                                                imageRef.setValue(imageUrl);
                                                                DatabaseReference imageRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child("w").child("imageProfile");
                                                                imageRef2.setValue(imageUrl);
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Aici puteți să logați eroarea pentru a vedea ce nu funcționează
                                                Log.d("ProfileImageUpload", "Eroare la încărcarea imaginii", e);
                                            }
                                        });
                            }
                        }
                    }
                });



        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch(intent);
            }
        });

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference profileRef=database.getReference().child("Profile").child("w").child("name");
        DatabaseReference countryRef=database.getReference().child("Profile").child("w").child("country");
        DatabaseReference categoryRef=database.getReference().child("Profile").child("w").child("category");
        DatabaseReference categoryRef2=database.getReference().child("Users").child("w").child("category");
        DatabaseReference ageRef=database.getReference().child("Profile").child("w").child("age");
        DatabaseReference countryUser=database.getReference().child("Users").child("w").child("country");

        builder.setView(view)
                .setPositiveButton("Salveaza", (dialog, which) -> {
                    String nameString = name.getText().toString();
                    profileRef.setValue(nameString);

                    String countryString=spinner.getSelectedItem().toString();
                    String ageString=spinnerVarsta.getSelectedItem().toString();
                    String categroyString=spinnerCategory.getSelectedItem().toString();

                    countryRef.setValue(countryString);
                    countryUser.setValue(countryString);
                    categoryRef.setValue(categroyString);
                    categoryRef2.setValue(categroyString);
                    ageRef.setValue(ageString);

                })
                .setNegativeButton("Anuleaza", (dialog, which) -> {

                });
        Dialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);
        }

        return dialog;
    }
}
