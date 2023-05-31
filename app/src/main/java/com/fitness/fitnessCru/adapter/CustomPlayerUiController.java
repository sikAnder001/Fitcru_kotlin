/*
package com.fitness.fitnessCru.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.fitness.fitnessCru.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;


class CustomPlayerUiController extends AbstractYouTubePlayerListener {

//    private final YouTubePlayerTracker playerTracker;
    private Context context;
    private YouTubePlayer youTubePlayer;
    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    private RelativeLayout panelty;
    private View progressbar;

    private void initViews(View playerUi) {
        panel = playerUi.findViewById(R.id.panel);
        panelty = playerUi.findViewById(R.id.panelty);
        progressbar = playerUi.findViewById(R.id.progressbar);
        ImageView playPauseButton = playerUi.findViewById(R.id.exo_play1);
        ImageView pause = playerUi.findViewById(R.id.exo_pause1);

        playPauseButton.setOnClickListener((view) -> {
            if (playerTracker.getState() == PlayerConstants.PlayerState.PAUSED) {
                pause.setVisibility(View.VISIBLE);
                playPauseButton.setVisibility(View.GONE);
                youTubePlayer.play();

            } else {
                youTubePlayer.pause();
            }
        });

        panel.setOnClickListener((view) -> {
            if (panelty.getVisibility() == View.VISIBLE) {
                panelty.setVisibility(View.GONE);

            } else {
                panelty.setVisibility(View.VISIBLE);
            }
        });

        pause.setOnClickListener((view) -> {
            if (playerTracker.getState() == PlayerConstants.PlayerState.PLAYING) {
                playPauseButton.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                youTubePlayer.pause();
            } else youTubePlayer.play();
        });

    }

    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        } else {
            if (state == PlayerConstants.PlayerState.BUFFERING) {
                panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            }

        }
    }

}*/
