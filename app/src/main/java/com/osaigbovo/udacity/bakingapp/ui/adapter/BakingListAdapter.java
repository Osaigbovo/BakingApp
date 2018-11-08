package com.osaigbovo.udacity.bakingapp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.ui.viewholder.BakingListViewHolder;
import com.osaigbovo.udacity.bakingapp.util.BakingImageAssets;
import com.osaigbovo.udacity.bakingapp.util.GlideApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BakingListAdapter
        extends RecyclerView.Adapter<BakingListViewHolder> {

    private OnBakingItemSelectedListener onBakingItemSelectedListener;
    private final List<Recipe> recipes;
    private final List<Integer> bakingImages;
    private final Context mContext;

    // Container Activity must implement this interface
    public interface OnBakingItemSelectedListener {
        void onBakingItemSelected(int position);
        void onBakingSelected(Recipe recipe);
    }

    public BakingListAdapter(Context context,
                             List<Recipe> recipes,
                             OnBakingItemSelectedListener onBakingItemSelectedListener) {
        this.mContext = context;
        this.recipes = recipes;
        this.onBakingItemSelectedListener = onBakingItemSelectedListener;

        bakingImages = BakingImageAssets.getBakingImages();
    }

    @NonNull
    @Override
    public BakingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_baking, parent, false);
        return new BakingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BakingListViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.recipeName.setText(String.valueOf(recipe.getName()));

        if (!TextUtils.isEmpty(recipe.getImage())) {
            GlideApp.with(holder.recipeImage.getContext())
                    .load(recipe.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .priority(Priority.HIGH)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .transition(withCrossFade())
                    .placeholder(bakingImages.get(position))
                    //.error(R.drawable.ic_movie_error)
                    .into(holder.recipeImage);
        } else {
            holder.recipeImage.setImageResource(bakingImages.get(position));
            recipe.setImage(String.valueOf(bakingImages.get(position)));
        }

        holder.itemView.setOnClickListener(view -> {
            if (onBakingItemSelectedListener != null) {
                onBakingItemSelectedListener.onBakingSelected(recipe);
                onBakingItemSelectedListener.onBakingItemSelected(recipe.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}