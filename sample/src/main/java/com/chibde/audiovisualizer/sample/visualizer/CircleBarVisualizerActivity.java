package com.chibde.audiovisualizer.sample.visualizer;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.chibde.audiovisualizer.sample.BaseActivity;
import com.chibde.audiovisualizer.sample.R;
import com.chibde.visualizer.CircleBarVisualizer;

public class CircleBarVisualizerActivity extends BaseActivity {

    @Override
    protected void init() {
        CircleBarVisualizer circleBarVisualizer = findViewById(R.id.visualizer);
        circleBarVisualizer.setColor(ContextCompat.getColor(this, R.color.custom));
        circleBarVisualizer.setPlayer(mediaPlayer);
    }

    public void replay(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_circle_bar_visualizer;
    }
}
