package com.example.l999;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {
    private List<User> userList;
    //private Context context;

    public RankingAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewUser.setText(user.getName());
        holder.textViewPoints.setText(String.valueOf(user.getTotalPoints()));
        holder.place.setText(String.valueOf(position + 1));
        String profileImageUrl = user.getImageProfile();
        String countryImage=user.getCountry();
        holder.cat.setText(user.getCategory());

        Context context = holder.itemView.getContext();
        if (user.getName().equals("Radu")) {
            int highlightColor = ContextCompat.getColor(context, R.color.highlightColor);
            holder.itemView.setBackgroundColor(highlightColor);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 1000);
        holder.itemView.startAnimation(animation);


        int k=0;
        if ("Romania".equals(countryImage)) {
            k = R.drawable.rou_flag;
        } else if ("Japonia".equals(countryImage)) {
            k = R.drawable.jpn_flag;
        } else if ("USA".equals(countryImage)) {
            k = R.drawable.usa_flag;
        }

        if (k != 0) {
            holder.countryImg.setImageResource(k);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUser;
        public TextView textViewPoints;
        public ImageView imageViewProfile;
        public TextView place;
        public ImageView countryImg;
        public TextView cat;


        public ViewHolder(View view) {
            super(view);
            textViewUser = view.findViewById(R.id.textViewUser);
            textViewPoints = view.findViewById(R.id.textViewPoints);
            //imageViewProfile=view.findViewById(R.id.profileImg);
            place=view.findViewById(R.id.place);
            countryImg=view.findViewById(R.id.imageViewCountry);
            cat=view.findViewById(R.id.textViewCat);
        }
    }
}
