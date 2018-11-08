package com.osaigbovo.udacity.bakingapp.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.osaigbovo.udacity.bakingapp.R;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_recipe_name)
    public TextView recipeName;
    @BindView(R.id.image_recipe)
    public ImageView recipeImage;

    public BakingListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
