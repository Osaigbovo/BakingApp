package com.osaigbovo.udacity.bakingapp.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.ui.viewholder.BakingListViewHolder;
import com.osaigbovo.udacity.bakingapp.util.BakingImageAssets;
import com.osaigbovo.udacity.bakingapp.util.GlideApp;
import com.osaigbovo.udacity.bakingapp.util.GlideRequest;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
            /*GlideApp.with(holder.recipeImage.getContext())
                    .load(recipe.getImage())
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    //.priority(Priority.HIGH)
                    //.skipMemoryCache(true)
                    //.fitCenter()
                    //.transition(withCrossFade()) // .transition(DrawableTransitionOptions.withCrossFade())
                    //.thumbnail(.05f)

                    .transforms(new CenterCrop(), new RoundedCorners(1))

                    //.transform(new RoundedCornersTransformation(30, 10))
                    //.transform(new BlurTransformation())
                    //.transform(new jp.wasabeef.glide.transformations.BlurTransformation(25, 2))
                    //.placeholder(bakingImages.get(position))
                    //.error(R.drawable.ic_movie_error)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.recipeProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.recipeProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.recipeImage);*/
        } else {
            //holder.recipeImage.setImageResource(bakingImages.get(position));
            recipe.setImage(String.valueOf(bakingImages.get(position)));

            GlideRequest<Drawable> thumbnailRequest = GlideApp.with(holder.recipeImage.getContext())
                    .load("https://picsum.photos/50/50?image=0")
                    .load(bakingImages.get(position));

            GlideApp.with(mContext)
                    //.load("https://picsum.photos/2000/2000?image=0")

                    .load(bakingImages.get(position))
                    //.thumbnail(thumbnailRequest)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    //.skipMemoryCache(true)
                    //.fitCenter()
                    //.transition(withCrossFade()) // .transition(DrawableTransitionOptions.withCrossFade())
                    //.thumbnail(0.1f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.recipeProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.recipeProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.recipeImage);
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