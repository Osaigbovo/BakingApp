package com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist;

import android.content.Intent;
import android.os.Bundle;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.ui.adapter.BakingListAdapter;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity.ARG_RECIPE;

// Displays a list of Cakes
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        /*BakingListFragment.OnIdlingResourceChangeListener,*/
        BakingListAdapter.OnBakingItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    //CountingIdlingResource mCountingIdleResource = new CountingIdlingResource(TAG);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof BakingListFragment) {
            BakingListFragment bakingListFragment = (BakingListFragment) fragment;
            bakingListFragment.setOnRecipeSelectedListener(this);
        }
    }

    @Override
    public void onBakingItemSelected(int position) {
        //Toast.makeText(this, "Position clicked : " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBakingSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(ARG_RECIPE, recipe);
        startActivity(intent);
    }


    /*@Override
    public void idlingResourceChangeListener(boolean countingIdlingResource) {
        if (!countingIdlingResource) {
            mCountingIdleResource.increment();
        } else {
            mCountingIdleResource.decrement();
        }
    }*/

    /*@VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        return mCountingIdleResource;
    }*/

}
