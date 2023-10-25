package com.example.l999;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TicketsCompActivity extends AppCompatActivity {

    private List<Ticket> ticketList = new ArrayList<>();
    private TicketsCompAdapter adapter;
    private FirebaseAuth database;
    private DatabaseReference TicketsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilete);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TicketsRef = FirebaseDatabase.getInstance().getReference("Tickets");

        TicketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ticketList.clear();
                for(DataSnapshot ticketSnapshot: dataSnapshot.getChildren()){
                    Ticket ticket = ticketSnapshot.getValue(Ticket.class);
                    ticketList.add(ticket);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(TicketsCompActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new TicketsCompAdapter(ticketList);
        recyclerView.setAdapter(adapter);

        Ticket ticket1 = new Ticket("Nume Utilizator 1", "Detalii 1", "categorie1","numecomp1");
        ticketList.add(ticket1);

        adapter.notifyDataSetChanged();
    }
}
