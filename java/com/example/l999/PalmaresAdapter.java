package com.example.l999;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PalmaresAdapter extends RecyclerView.Adapter<PalmaresAdapter.ViewHolder> {

    private List<String> dataList;

    public PalmaresAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_palmares, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String competitionDescription = dataList.get(position);
        holder.bind(competitionDescription);

        Context context = holder.itemView.getContext();
        Animation shineAnimation = AnimationUtils.loadAnimation(context, R.anim.shine);

        //animația la ImageView-ul corespunzător
        holder.imageViewMedalie.startAnimation(shineAnimation);

        /*Context context = holder.itemView.getContext();
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 1000);
        holder.itemView.startAnimation(animation);*/

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewItem;
        private ImageView imageViewMedalie;
        private ImageView imageViewMedalie1;

        private ImageView imageViewMedalie2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewNumeCompetitie);
            imageViewMedalie = itemView.findViewById(R.id.imageViewMedalie);
            imageViewMedalie1= itemView.findViewById(R.id.imageViewMedalie1);
            imageViewMedalie2= itemView.findViewById(R.id.imageViewMedalie2);
        }
        public void bind(String competitionDescription) {
            String[] parts = competitionDescription.split(",");
            String competitionName = parts[0];
            String medalType = parts[1];
            textViewItem.setText(competitionName);

            int medalResId = 0;
            if (medalType.equals("gold")) {
                medalResId = R.drawable.g1;
            } else if (medalType.equals("silver")) {
                medalResId = R.drawable.s1;
            } else if (medalType.equals("zbronze")) {
                medalResId = R.drawable.b1;
            }

            if (medalResId != 0) {
                imageViewMedalie.setImageResource(medalResId);
                imageViewMedalie.setVisibility(View.VISIBLE);
            } else {
                imageViewMedalie.setImageDrawable(null);
                imageViewMedalie.setVisibility(View.GONE);
            }
        }
    }
}