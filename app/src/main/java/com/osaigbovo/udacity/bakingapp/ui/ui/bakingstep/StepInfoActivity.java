package com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Step;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity;
import com.osaigbovo.udacity.bakingapp.ui.view.ElasticDragDismissFrameLayout;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class StepInfoActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static final String ARG_STEP = "step";
    public static final String ARG_POSITION = "position";

    @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout draggableFrame;
    @BindView(R.id.navigation_bar)
    RelativeLayout navigationBar;
    @BindView(R.id.back_wrapper)
    FrameLayout wrapperFrameLayout;
    @BindView(R.id.container)
    NestedScrollView nestedScrollView;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.pip)
    ImageButton pip;
    @BindView(R.id.step_number)
    TextView mStepText;
    @BindView(R.id.prev_step)
    ImageButton mPrevButton;
    @BindView(R.id.next_step)
    ImageButton mNextButton;

    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;
    private ArrayList<Step> steps;
    private int stepPosition;

    PackageManager packageManager;

    private Boolean isPIPModeEnabled = true; //Has the user disabled PIP mode in AppOpps?

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(ARG_STEP) && intent.hasExtra(ARG_POSITION)) {
            steps = Objects.requireNonNull(intent.getExtras()).getParcelableArrayList(ARG_STEP);
            stepPosition = intent.getExtras().getInt(ARG_POSITION);
        } else {
            Intent mainIntent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
            startActivity(mainIntent);
            finish();
        }

        setContentView(R.layout.activity_step_info);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.media_container, StepInfoFragment.newInstance(steps, stepPosition))
                    .commitNow();
        } else {
            stepPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Set correct position on navigation bar
        updateStepNumberText(stepPosition);

        packageManager = this.getPackageManager();

        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                finishAfterTransition();
            }
        };
        back.setOnClickListener(v -> StepInfoActivity.this.onBackPressed());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_POSITION, stepPosition);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        draggableFrame.addListener(chromeFader);
    }

    @Override
    protected void onPause() {
        draggableFrame.removeListener(chromeFader);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //PIPmode activity.finish() does not remove the activity from the recents stack.
        //Only finishAndRemoveTask does this.
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            finishAndRemoveTask();
        }
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }

    // Switches to PiP mode when the user clicks the PiP icon:
    @OnClick(R.id.pip)
    public void minimize() {
        Toast.makeText(this, "Picture In Picture", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
                && isPIPModeEnabled) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder mPictureInPictureParamsBuilder
                        = new PictureInPictureParams.Builder();
                // Calculate the aspect ratio of the PiP screen.
                Rational aspectRatio = new Rational(nestedScrollView.getWidth() * 2,
                        nestedScrollView.getHeight());
                mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                this.enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());

            } else {
                this.enterPictureInPictureMode();
            }
        } else {
            pip.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.prev_step)
    public void prevStep() {
        stepPosition--;
        updateStepNumberText(stepPosition);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.media_container, StepInfoFragment.newInstance(steps, stepPosition))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitNow();
    }

    @OnClick(R.id.next_step)
    public void nextStep() {
        stepPosition++;
        updateStepNumberText(stepPosition);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.media_container, StepInfoFragment.newInstance(steps, stepPosition))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitNow();
    }


    // Method to update the current step number in navigation bar
    private void updateStepNumberText(int stepPosition) {
        mStepText.setText(String.valueOf(stepPosition));
        // Check position and hide arrow if at 0 or last position,
        // preventing going negative and higher than list size
        if (stepPosition == 0) {
            mPrevButton.setVisibility(View.GONE);
        } else if (stepPosition == steps.size() - 1) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustFullScreen(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            adjustFullScreen(getResources().getConfiguration());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration configuration) {
        if (isInPictureInPictureMode) {
            wrapperFrameLayout.setVisibility(View.GONE);
            navigationBar.setVisibility(View.GONE);
        } else {
            wrapperFrameLayout.setVisibility(View.VISIBLE);
            navigationBar.setVisibility(View.VISIBLE);
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, configuration);
    }


    /**
     * Adjusts immersive full-screen flags depending on the screen orientation.
     *
     * @param config The current {@link Configuration}.
     */
    private void adjustFullScreen(Configuration config) {
        final View decorView = getWindow().getDecorView();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            wrapperFrameLayout.setVisibility(View.GONE);
            navigationBar.setVisibility(View.GONE);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            wrapperFrameLayout.setVisibility(View.VISIBLE);
            navigationBar.setVisibility(View.VISIBLE);
        }
    }

}
