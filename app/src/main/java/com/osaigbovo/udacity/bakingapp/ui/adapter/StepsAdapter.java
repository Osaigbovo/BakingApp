package com.osaigbovo.udacity.bakingapp.ui.adapter;

import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Step;
import com.osaigbovo.udacity.bakingapp.ui.viewholder.StepsViewHolder;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepsAdapter extends RecyclerView.Adapter<StepsViewHolder> {

    private OnStepSelectedListener onStepSelectedListener;
    private ArrayList<Step> steps;
    private int stepId;
    private String description;
    private SparseBooleanArray selectedSteps = new SparseBooleanArray();

    public StepsAdapter(ArrayList<Step> steps, OnStepSelectedListener onStepSelectedListener) {
        this.steps = steps;
        this.onStepSelectedListener = onStepSelectedListener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        Step step = steps.get(position);

        holder.stepMaterialCardView.setSelected(selectedSteps.get(position, false));

        stepId = step.getId();
        description = step.getShortDescription();
        holder.stepDescriptionTextView
                .setText(String.format(Locale.US, "%d. %s",
                        step.getId(), step.getShortDescription()));

        if (TextUtils.isEmpty(step.getVideoURL())) {
            holder.videoImageView.setVisibility(View.INVISIBLE);
        } else {
            holder.videoImageView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (onStepSelectedListener != null) {
               /* //holder.stepMaterialCardView.setSelected(true);


                // Save the selected positions to the SparseBooleanArray
                if (selectedSteps.get(position, false)) {
                    selectedSteps.delete(position);
                    Timber.w("FALSE!!!!");
                    holder.stepMaterialCardView.setSelected(false);
                } else {
                    Timber.w("TRUE!!!!"+ position);
                    selectedSteps.put(position, true);
                    holder.stepMaterialCardView.setSelected(true);
                }*/
                onStepSelectedListener.onStepSelected(steps, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    @Override
    public long getItemId(int position) {
        return steps.get(position).getId();
    }


    public interface OnStepSelectedListener {
        void onStepSelected(ArrayList<Step> steps, int position);
    }

}
