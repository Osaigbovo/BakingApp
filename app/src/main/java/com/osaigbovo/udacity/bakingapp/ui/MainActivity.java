package com.osaigbovo.udacity.bakingapp.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.ui.adapter.BakingListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
// This activity is responsible for displaying the master list of all images
public class MainActivity extends AppCompatActivity implements BakingListAdapter.OnBakingItemSelectedListener {

    // Whether or not the activity is in two-pane mode, i.e. running on a table device.
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.baking_details) != null) {
            // The detail container view will be present only in the large-screen layouts.
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;

            if (savedInstanceState == null) {
                // In two-pane mode, add initial Fragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();
                RecipeDetailFragment fragment = new RecipeDetailFragment();

                fragmentManager
                        .beginTransaction()
                        .add(R.id.baking_details_ingredients, fragment)
                        .commit();
            }

        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }
    }

    @Override
    public void onBakingItemSelected(int position) {
        Toast.makeText(this, "Position clicked : " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBakingSelected(Recipe recipe) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeDetailFragment.ARG_RECIPE, recipe);

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.baking_details_ingredients, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            /*Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_RECIPE, recipe);
            startActivity(intent);*/


            Intent intent = new Intent(this, ExoplayerActivity.class);

            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

}
