package com.osaigbovo.udacity.bakingapp.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.osaigbovo.udacity.bakingapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.material_card_step)
    public MaterialCardView stepMaterialCardView;
    @BindView(R.id.text_step_description)
    public TextView stepDescriptionTextView;
    @BindView(R.id.image_step_video_icon)
    public ImageView videoImageView;

    public StepsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
