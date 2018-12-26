package com.chibde.audiovisualizer.sample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.chibde.visualizer.BarVisualizer;

public class ServiceExampleActivity extends AppCompatActivity {
    private MediaPlayerService mBoundService;
    private BarVisualizer barVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_service_example);
        barVisualizer = findViewById(R.id.visualizer);

        // set custom color to the line.
        barVisualizer.setColor(ContextCompat.getColor(this, R.color.custom));

        // define custom number of bars you want in the visualizer between (10 - 256).
        barVisualizer.setDensity(70);
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // register LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter(MediaPlayerService.INTENT_FILTER));
        Intent intent = new Intent(this, MediaPlayerService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initialize() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(BaseActivity.WRITE_EXTERNAL_STORAGE_PERMS, BaseActivity.AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case BaseActivity.AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
        }
    }


    public void replay(View view) {
        mBoundService.replay();
    }

    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    /**
     * receive audio session id required for visualizer through
     * broadcast receiver from service
     * ref https://stackoverflow.com/a/27652660/5164673
     */
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int audioSessionId = intent.getIntExtra(MediaPlayerService.INTENT_AUDIO_SESSION_ID, -1);
            if (audioSessionId != -1) {
                barVisualizer.setPlayer(audioSessionId);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MediaPlayerService.class));
        unbindService(serviceConnection);
    }

    protected void onPause() {
        super.onPause();
        // unregister LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }

    public void playPauseBtnClicked(ImageButton btnPlayPause) {
        if (mBoundService.isPlaying()) {
            mBoundService.pause();
            btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_play_red_48dp));
        } else {
            mBoundService.start();
            btnPlayPause.setImageDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_pause_red_48dp));
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerServiceBinder myBinder = (MediaPlayerService.MediaPlayerServiceBinder) service;
            mBoundService = myBinder.getService();
        }
    };
}
