package com.chibde.audiovisulaizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gautam on 28/10/17.
 */

public class VisualizerView extends View {

    private Paint paint;
    private Visualizer visualizer;
    private byte[] bytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private float amplitude = 0;

    public VisualizerView(Context context) {
        super(context);
        init(null);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
    }

    public void setPlayer(MediaPlayer mediaPlayer) {
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                VisualizerView.this.bytes = bytes;
                invalidate();
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, true);

        visualizer.setEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bytes != null) {
            if (mPoints == null || mPoints.length < bytes.length * 4) {
                mPoints = new float[bytes.length * 4];
            }
            mRect.set(0, 0, getWidth(), getHeight());

            for (int i = 0; i < bytes.length - 1; i++) {
                mPoints[i * 4] = mRect.width() * i / (bytes.length - 1);
                mPoints[i * 4 + 1] = mRect.height() / 2 + ((byte) (bytes[i] + 128)) * (mRect.height() / 3) / 128;
                mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (bytes.length - 1);
                mPoints[i * 4 + 3] = mRect.height() / 2 + ((byte) (bytes[i + 1] + 128)) * (mRect.height() / 3) / 128;
            }
            canvas.drawLines(mPoints, paint);
        }
        super.onDraw(canvas);
    }
}
