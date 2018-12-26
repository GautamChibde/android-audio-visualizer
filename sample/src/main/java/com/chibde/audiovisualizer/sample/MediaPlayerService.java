package com.chibde.audiovisualizer.sample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MediaPlayerService extends Service {

    public static final String INTENT_FILTER = "MediaPlayerServiceIntentFilter";
    public static final String INTENT_AUDIO_SESSION_ID = "intent_audio_session_id";

    private IBinder mediaPlayerServiceBinder = new MediaPlayerServiceBinder();
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.red_e);
        mediaPlayer.setLooping(false);

        Intent intent = new Intent(INTENT_FILTER); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra(INTENT_AUDIO_SESSION_ID, mediaPlayer.getAudioSessionId());
        // Send audio session id through
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mediaPlayerServiceBinder;
    }

    public void replay() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public class MediaPlayerServiceBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }
}
