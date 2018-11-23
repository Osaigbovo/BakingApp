package com.osaigbovo.udacity.bakingapp.ui;

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
import android.widget.Toast;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.ui.view.ElasticDragDismissFrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExoplayerActivity extends AppCompatActivity {

    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout draggableFrame;
    @BindView(R.id.back_wrapper)
    FrameLayout constraintLayout;
    @BindView(R.id.container)
    NestedScrollView nestedScrollView;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.pip)
    ImageButton pip;

    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    PackageManager packageManager;

    Boolean isInPipMode = false;
    Boolean isPIPModeEnabled = true; //Has the user disabled PIP mode in AppOpps?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exoplayer_activity);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ExoplayerFragment.newInstance())
                    .commitNow();
        }

        packageManager = this.getPackageManager();

        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                /*getWindow().setReturnTransition(TransitionInflater.from(ExoplayerActivity.this)
                        .inflateTransition(R.transition.return_transition_draggable));*/
                finishAfterTransition();
            }
        };


        back.setOnClickListener(v -> ExoplayerActivity.this.onBackPressed());
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
                Rational aspectRatio = new Rational(nestedScrollView.getWidth()*2, nestedScrollView.getHeight());
                mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                this.enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());

            } else {
                this.enterPictureInPictureMode();
            }

        } else {
            super.onBackPressed();
        }

    }

    // Called when the user touches the Home or Recents button to leave the app.
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder mPictureInPictureParamsBuilder
                        = new PictureInPictureParams.Builder();
                // Calculate the aspect ratio of the PiP screen.
                //Rational aspectRatio = new Rational(mMovieView.getWidth(), mMovieView.getHeight());
                //mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                this.enterPictureInPictureMode(mPictureInPictureParamsBuilder.build());

            } else {
                this.enterPictureInPictureMode();
            }
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
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration configuration) {
        if(isInPictureInPictureMode){
            isInPipMode = !isInPictureInPictureMode;

            constraintLayout.setVisibility(View.GONE);
        }else {
            constraintLayout.setVisibility(View.VISIBLE);
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
            constraintLayout.setVisibility(View.GONE);
            //mMovieView.setAdjustViewBounds(false);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            constraintLayout.setVisibility(View.VISIBLE);
            //mMovieView.setAdjustViewBounds(true);
        }
    }


}
