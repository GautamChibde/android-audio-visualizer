package com.chibde.audiovisualizer.sample.visualizer;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.chibde.audiovisualizer.sample.BaseActivity;
import com.chibde.audiovisualizer.sample.R;
import com.chibde.visualizer.LineBarVisualizer;

public class LineBarVisualizerActivity extends BaseActivity {

    @Override
    protected void init() {
        LineBarVisualizer lineBarVisualizer = findViewById(R.id.visualizer);

        // set custom color to the line.
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.custom));

        // define custom number of bars you want in the visualizer between (10 - 256).
        lineBarVisualizer.setDensity(90f);

        // Set your media player to the visualizer.
        lineBarVisualizer.setPlayer(mediaPlayer);
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
        return R.layout.activity_line_bar_visualizer;
    }
}
