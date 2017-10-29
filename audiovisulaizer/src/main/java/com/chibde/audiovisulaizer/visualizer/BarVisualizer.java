package com.chibde.audiovisulaizer.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.chibde.audiovisulaizer.BaseVisualizer;

/**
 * Created by gautam on 28/10/17.
 */

public class BarVisualizer extends BaseVisualizer {

    private int density = 50;

    public BarVisualizer(Context context) {
        super(context);
    }

    public BarVisualizer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BarVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {

    }

    public void setDensity(int density) {
        this.density = density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bytes != null) {
            paint.setStyle(Paint.Style.FILL);
            int barWidth = getWidth() / density;
            paint.setStrokeWidth(barWidth - 4);

            for (int i = 0; i < bytes.length; i += barWidth) {
                int top = canvas.getHeight() + ((byte) (Math.abs(bytes[i]) + 128)) * canvas.getHeight() / 128;
                canvas.drawLine(i, getHeight(), i, top, paint);
            }
            super.onDraw(canvas);
        }
    }
}

