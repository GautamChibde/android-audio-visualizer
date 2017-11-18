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
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

/**
 * BaseActivity that contains common code for all visualizers
 *
 * Created by gautam chibde on 18/11/17.
 */
abstract public class BaseActivity extends AppCompatActivity {
    private static final int AUDIO_PERMISSION_REQUEST_CODE = 102;

    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.RECORD_AUDIO
    };

    protected MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayout() != 0) {
            setContentView(getLayout());
        } else {
            throw new NullPointerException("Provide layout file for the activity");
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(WRITE_EXTERNAL_STORAGE_PERMS, AUDIO_PERMISSION_REQUEST_CODE);
            } else {
                setPlayer();
            }
        } else {
            setPlayer();
        }
    }

    private void setPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_jingle_bell);
        mediaPlayer.setLooping(false);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
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

    public void playPauseBtnClicked(ImageButton btnPlayPause) {
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

    protected int getLayout() {
        return 0;
    }

    protected abstract void init();
}
