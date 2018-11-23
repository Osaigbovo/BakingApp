package com.osaigbovo.udacity.bakingapp.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.osaigbovo.udacity.bakingapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ExoplayerFragment extends Fragment {

    public static final String ARG_VIDEO_URL = "video_url";
    private static final String VIDEO_POSITION_KEY = "video_positon";
    private static final String TAG = ExoplayerFragment.class.getSimpleName();
    private static final String PLAY_WHEN_READY_KEY = "extra_video_playback_state";

    // Bandwidth meter to measure and estimate bandwidth
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    @BindView(R.id.player_view)
    PlayerView mPlayerView;
    @BindView(R.id.loading)
    ProgressBar mProgressBar;
    PackageManager packageManager;
    private String videoUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
    // "https://mirrors.standaloneinstaller.com/video-sample/metaxas-keller-Bell.mp4"
    private String mUrl;
    private SimpleExoPlayer simpleExoPlayer;
    // ------
    private long videoPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private Boolean isInPipMode = false;
    private Boolean isPIPModeeEnabled = true; //Has the user disabled PIP mode in AppOpps?
    private ExoplayerViewModel mViewModel;
    private Unbinder unbinder;
    private PlayerEventListener playerEventListener;
    private TrackSelection.Factory adaptiveTrackSelectionFactory;
    private TrackSelector mTrackSelector;
    private LoadControl mLoadControl;
    private MediaSessionCompat mMediaSession;
    private MediaSource mMediaSource;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Context context;


    public static ExoplayerFragment newInstance() {
        return new ExoplayerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (Objects.requireNonNull(getArguments()).containsKey(ARG_VIDEO_URL)) {
            mUrl = getArguments().getString(ARG_VIDEO_URL);
        }*/

        packageManager = getActivity().getPackageManager();
        playerEventListener = new PlayerEventListener();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.exoplayer_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        context = getContext();

        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExoplayerViewModel.class);
    }

    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            // A factory to create an AdaptiveVideoTrackSelection || new RandomTrackSelection.Factory();
            adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
            mTrackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            mLoadControl = new DefaultLoadControl();

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultRenderersFactory(getContext()), mTrackSelector, mLoadControl);

            simpleExoPlayer.addListener(playerEventListener);
            mPlayerView.setPlayer(simpleExoPlayer);

            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(currentWindow, videoPosition);
        }

        mMediaSource = buildMediaSource(videoUrl);
        simpleExoPlayer.prepare(mMediaSource, true, false);

        initializeMediaSession();
    }

    private void releasePlayer() {
        mMediaSession.setActive(false);
        if (simpleExoPlayer != null) {
            videoPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();

            simpleExoPlayer.removeListener(playerEventListener);
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            mMediaSource = null;
            mTrackSelector = null;
        }
    }

    private MediaSource buildMediaSource(String videoUrl) {
        Uri videoURI = Uri.parse(videoUrl);
        String userAgent = Util.getUserAgent(context, getString(R.string.app_name));
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER);

        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoURI);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }

        /*if (videoPosition > 0L && !isInPipMode) {
            simpleExoPlayer.seekTo(videoPosition);
        }
        //Makes sure that the media controls pop up on resuming and when going between PIP and non-PIP states.
        mPlayerView.setUseController(true);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(VIDEO_POSITION_KEY, videoPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



   /* @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            this.videoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
            this.playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }
    }*/


    /**
     * Called by the system when the activity changes to and from picture-in-picture mode. This is
     * generally tied to {link Activity#onPictureInPictureModeChanged} of the containing Activity.
     *
     * @param isInPictureInPictureMode True if the activity is in picture-in-picture mode.
     */
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        videoPosition = simpleExoPlayer.getCurrentPosition();

        if (isInPictureInPictureMode) {
            mPlayerView.setUseController(false);
            mPlayerView.hideController();
        } else {
            mPlayerView.setUseController(true);
            mPlayerView.showController();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else {

        }*/
    }

    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(context, TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = initializeThePlaybackState();
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new BakingSessionCallback());
        mMediaSession.setActive(true);
    }

    private PlaybackStateCompat.Builder initializeThePlaybackState() {
        return new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
    }

    private class PlayerEventListener implements Player.EventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE: // The player does not have any media to play
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    mProgressBar.setVisibility(View.VISIBLE);
                    //mmPlayerView.hideController();
                    break;
                case Player.STATE_BUFFERING: // The player needs to load media before playing.
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    mProgressBar.setVisibility(View.VISIBLE);
                    //mPlayPauseLayout.setVisibility(View.GONE);
                    break;
                case Player.STATE_READY: // The player is able to immediately play from its current position.
                    stateString = "ExoPlayer.STATE_READY     -";
                    mProgressBar.setVisibility(View.GONE);

                    if (playWhenReady) {
                        // When ExoPlayer is playing, update the PlayBackState.
                        Log.d(TAG, "onPlayerStateChanged: PLAYING");
                        mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                                simpleExoPlayer.getCurrentPosition(), 1f);
                    } else {
                        // When ExoPlayer is paused, update the PlayBackState.
                        Log.d(TAG, "onPlayerStateChanged: PAUSED");
                        mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                                simpleExoPlayer.getCurrentPosition(), 1f);
                    }
                    mMediaSession.setPlaybackState(mStateBuilder.build());

                    //mPlayPauseLayout.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_ENDED: // The player has finished playing the media.
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }

            Log.d("Exo", "changed state to " + stateString + " playWhenReady: " + playWhenReady);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            switch (repeatMode) {
                case Player.REPEAT_MODE_OFF:
                    mPlayerView.setRepeatToggleModes(repeatMode);
                    break;
                case Player.REPEAT_MODE_ONE:
                    mPlayerView.setRepeatToggleModes(Player.REPEAT_MODE_ALL);
                    break;
                case Player.REPEAT_MODE_ALL:
                    mPlayerView.setRepeatToggleModes(Player.REPEAT_MODE_ALL);
                    break;
                default:
                    // Default
                    break;
            }
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.d(TAG, error.getMessage());
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        }

        @Override
        public void onSeekProcessed() {
        }
    }

    // Media Session Callbacks, where all external clients control the player.
    private class BakingSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }
}
