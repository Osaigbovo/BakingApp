package com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.ui.adapter.BakingListAdapter;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity.ARG_RECIPE;

// Displays a list of Cakes
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        BakingListAdapter.OnBakingItemSelectedListener {

    @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar.setTitle(getTitle());
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
}
