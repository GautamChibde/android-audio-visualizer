/*
* Copyright (C) 2017 Gautam Chibde
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.chibde.audiovisualizer.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.chibde.visualizer.BarVisualizer;
import com.chibde.visualizer.CircleVisualizer;
import com.chibde.visualizer.LineVisualizer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int AUDIO_PERMISSION_REQUEST_CODE = 102;

    private MediaPlayer mediaPlayer;
    private ImageButton btnPlayPause;
    private CircleVisualizer lineVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayPause = findViewById(R.id.ib_play_pause);
        lineVisualizer = findViewById(R.id.visualizer);
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
                requestPermissions(
                        permissions.toArray(new String[permissions.size()]),
                        AUDIO_PERMISSION_REQUEST_CODE);
            }
        } else {
            setPlayer();
        }
        btnPlayPause.setOnClickListener(this);
    }

    private void setPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_jingle_bell);
        mediaPlayer.setLooping(false);
        lineVisualizer.setColor(ContextCompat.getColor(this, R.color.custom));
        lineVisualizer.setPlayer(mediaPlayer);
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
                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_play_red_48dp));
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_pause_red_48dp));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            lineVisualizer.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                setPlayer();
        }
    }
}
