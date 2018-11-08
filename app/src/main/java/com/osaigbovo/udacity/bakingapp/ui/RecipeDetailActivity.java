package com.osaigbovo.udacity.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a single Recipe detail screen. This activity is only used on narrow width devices.
 * On tablet-size devices, item details are presented side-by-side with a list of recipes in a {@link MainActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar_detail)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.image_recipe_backdrop)
    ImageView backdropImage;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        // Show the Up button in the action bar.
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        Intent intent = getIntent();
        if (intent.hasExtra(RecipeDetailFragment.ARG_RECIPE)) {

            Recipe recipe = getIntent().getExtras().getParcelable(RecipeDetailFragment.ARG_RECIPE);

            if (collapsingToolbarLayout != null) {
                collapsingToolbarLayout.setTitle(recipe.getName());
            }

            backdropImage.setImageResource(Integer.valueOf(recipe.getImage()));

            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeDetailFragment.ARG_RECIPE,
                        recipe);

                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.baking_details_ingredients, fragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.fab)
    public void replaceFab(View view) {
        Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
