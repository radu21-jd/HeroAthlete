package com.example.l999;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TicketsCompAdapter extends RecyclerView.Adapter<TicketsCompAdapter.ViewHolder> {

    private List<Ticket> ticketList;

    public TicketsCompAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bilete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTicketTitle;
        private TextView textViewTicketDetails;
        private TextView textViewTicketCategory;
        private TextView textViewTicketNumeComp;
        private TextView textViewNume;
        private TextView textViewClubSportiv;
        private TextView textViewCategorie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTicketNumeComp = itemView.findViewById(R.id.textViewTicketNumeComp);
            textViewTicketTitle = itemView.findViewById(R.id.textViewTicketTitle);
            textViewTicketDetails = itemView.findViewById(R.id.textViewTicketDetails);
            textViewTicketCategory = itemView.findViewById(R.id.textViewTicketCategory);
            textViewNume = itemView.findViewById(R.id.textViewNume);
            textViewClubSportiv = itemView.findViewById(R.id.textViewClubSportiv);
            textViewCategorie = itemView.findViewById(R.id.textViewCategorie);
        }

        public void bind(Ticket ticket) {
            textViewTicketNumeComp.setText(ticket.getNumeCompetitie());
            textViewNume.setText("Nume: " + ticket.getUserName());
            textViewClubSportiv.setText("Club Sportiv: " + ticket.getDetails());
            textViewCategorie.setText("Categorie: " + ticket.getCategory());
        }
    }
}
