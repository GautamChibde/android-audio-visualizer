package com.chibde.audiovisulaizer.visualizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.chibde.audiovisulaizer.BaseVisualizer;

/**
 * Created by gautam on 28/10/17.
 */

public class LineVisualizer extends BaseVisualizer {
    private float[] points;
    private Rect rect = new Rect();

    public LineVisualizer(Context context) {
        super(context);
    }

    public LineVisualizer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bytes != null) {
            if (points == null || points.length < bytes.length * 4) {
                points = new float[bytes.length * 4];
            }
            rect.set(0, 0, getWidth(), getHeight());

            for (int i = 0; i < bytes.length - 1; i++) {
                points[i * 4] = rect.width() * i / (bytes.length - 1);
                points[i * 4 + 1] = rect.height() / 2 + ((byte) (bytes[i] + 128)) * (rect.height() / 3) / 128;
                points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
                points[i * 4 + 3] = rect.height() / 2 + ((byte) (bytes[i + 1] + 128)) * (rect.height() / 3) / 128;
            }
            canvas.drawLines(points, paint);
        }
        super.onDraw(canvas);
    }
}
