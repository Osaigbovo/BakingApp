package com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.model.Step;
import com.osaigbovo.udacity.bakingapp.ui.adapter.StepsAdapter;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.MainActivity;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep.StepInfoActivity;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep.StepInfoFragment;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep.StepInfoActivity.ARG_POSITION;
import static com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep.StepInfoActivity.ARG_STEP;

/**
 * An activity representing a single Recipe detail screen. This activity is only used on narrow width devices.
 * On tablet-size devices, item details are presented side-by-side with a list of recipes in a {@link MainActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        StepsAdapter.OnStepSelectedListener {

    public static final String ARG_RECIPE = "recipe";

    @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Recipe recipe;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(ARG_RECIPE)) {
            recipe = Objects.requireNonNull(intent.getExtras()).getParcelable(ARG_RECIPE);
        } else {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        ButterKnife.bind(this);

        // Show the Up button in the action bar.
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(recipe.getName());
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        if (savedInstanceState == null) {
            if (findViewById(R.id.media_fragment) != null) {
                setupUI(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.media_fragment,
                                StepInfoFragment.newInstance(recipe.getSteps(), 0))
                        .commit();
            } else {
                setupUI(false);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_RECIPE, recipe);
    }

    private void setupUI(Boolean twoPane) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.step_fragment, StepFragment.newInstance(recipe.getSteps(),
                recipe.getIngredients()));
        if (!twoPane) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public void onStepSelected(ArrayList<Step> steps, int position) {
        if (findViewById(R.id.media_fragment) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.media_fragment, StepInfoFragment.newInstance(steps, position))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        } else {
            Intent intent = new Intent(this, StepInfoActivity.class);
            intent.putParcelableArrayListExtra(ARG_STEP, steps);
            intent.putExtra(ARG_POSITION, position);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }

    }

}
