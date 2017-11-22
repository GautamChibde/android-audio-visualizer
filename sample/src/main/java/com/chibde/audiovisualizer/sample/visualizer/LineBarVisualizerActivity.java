package com.chibde.audiovisualizer.sample.visualizer;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chibde.audiovisualizer.sample.BaseActivity;
import com.chibde.audiovisualizer.sample.R;
import com.chibde.visualizer.LineBarVisualizer;

public class LineBarVisualizerActivity extends BaseActivity {

    private LineBarVisualizer lineBarVisualizer;
    private int density = 50;
    private TextView textView;

    @Override
    protected void onStop() {
        super.onStop();
        lineBarVisualizer.release();
    }

    @Override
    protected void init() {
        lineBarVisualizer = findViewById(R.id.visualizer);
        textView = findViewById(R.id.density);
        textView.setText(String.valueOf(density));

        // set custom color to the line.
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.custom));

        // define custom number of bars you want in the visualizer between (10 - 256).
        lineBarVisualizer.setDensity(density);

        // Set your media player to the visualizer.
        lineBarVisualizer.setPlayer(mediaPlayer);
    }

    public void replay(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    public void add(View view) {
        density++;
        lineBarVisualizer.setDensity(density);
        textView.setText(String.valueOf(density));
    }

    public void remove(View view) {
        density--;
        lineBarVisualizer.setDensity(density);
        textView.setText(String.valueOf(density));
    }

    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_line_bar_visualizer;
    }
}
