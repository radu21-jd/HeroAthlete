package com.example.l999;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private List<Goal> goalList;
    private List<Goal> selectedGoalsList = new ArrayList<>();

    private DatabaseReference goalRef;

    public GoalAdapter(List<Goal> goalList) {
        this.goalList = goalList;
    }

    public void setGoalList(List<Goal> goalList) {
        this.goalList = goalList;
    }

    public void setSelectedGoalsList(List<Goal> selectedGoalsList) {
        this.selectedGoalsList = selectedGoalsList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.checkBoxGoal.setOnCheckedChangeListener(null);
        holder.checkBoxGoal.setText(goal.getName());
        holder.checkBoxGoal.setChecked(goal.isSelected());
        holder.buttonDetalis.setVisibility(goal.isSelected() ? View.VISIBLE : View.GONE);

        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference goalRef = data.getReference("Users").child("w").child("goals").child(goal.getId());

        holder.checkBoxGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            goal.setSelected(isChecked);

            if (isChecked) {
                if (!selectedGoalsList.contains(goal)) {
                    holder.buttonDetalis.setVisibility(View.VISIBLE);
                    selectedGoalsList.add(goal);
                }
            } else {
                selectedGoalsList.remove(goal);
                holder.buttonDetalis.setVisibility(View.GONE);
            }

            goalRef.child("selected").setValue(isChecked);
            goalRef.child("active").setValue(isChecked);
        });

        holder.buttonDetalis.setOnClickListener(v -> {
            Context context = v.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof FragmentActivity) {
                    switch (goal.getId()) {
                        case "-NabCSRFKE-CH6kdlTmH":
                            gl1Dialog gL1Dialog = new gl1Dialog();
                            gL1Dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "Results Dialog");
                            break;
                        case "-NabCSRIpS7fvoeyq1rb":
                            gl2Dialog gL2Dialog = new gl2Dialog();
                            gL2Dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "Obtine titlul [[unDefeated]]");
                            break;
                        case "-NabCSRIpS7fvoeyq1rc":
                            gl3Dialog gL3Dialog = new gl3Dialog();
                            gL3Dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "Obtine titlul [[unDefeated]]");
                            break;
                        case "-NabCSRIpS7fvoeyq1rd":
                            gl4Dialog gL4Dialog = new gl4Dialog();
                            gL4Dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "Obtine titlul xs");
                            break;
                    }
                    break;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        });
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBoxGoal;
        public Button buttonDetalis;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxGoal = itemView.findViewById(R.id.checkBoxGoal);
            buttonDetalis = itemView.findViewById(R.id.buttonDetalis);
        }
    }
}

