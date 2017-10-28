package com.chibde.audiovisualizer.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chibde.audiovisulaizer.VisualizerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private ImageButton btnPlayPause;
    private VisualizerView visualizerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayPause = (ImageButton) findViewById(R.id.ib_play_pause);
        visualizerView = (VisualizerView) findViewById(R.id.visualizer);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
            List<String> permissions = new ArrayList<>();
            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 102);
            }
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_jingle_bell);
            mediaPlayer.setLooping(false);
            visualizerView.setPlayer(mediaPlayer);
        }
        btnPlayPause.setOnClickListener(this);
    }

    public void replay(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    @Override
    public void onClick(View view) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_red_48dp));
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_red_48dp));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 102:
                mediaPlayer = MediaPlayer.create(this, R.raw.sample_jingle_bell);
                mediaPlayer.setLooping(false);
                visualizerView.setPlayer(mediaPlayer);
        }
    }
}
