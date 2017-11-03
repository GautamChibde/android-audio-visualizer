package com.chibde.audiovisulaizer.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.chibde.audiovisulaizer.BaseVisualizer;

/**
 * Created by gautam on 29/10/17.
 */

public class BlazingColorVisualizer extends BaseVisualizer {
    private Shader shader;

    public BlazingColorVisualizer(Context context) {
        super(context);
    }

    public BlazingColorVisualizer(Context context,
                                  @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlazingColorVisualizer(Context context,
                                  @Nullable AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlazingColorVisualizer(Context context,
                                  @Nullable AttributeSet attrs,
                                  int defStyleAttr,
                                  int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        shader = new LinearGradient(0,
                0,
                0,
                getHeight(),
                Color.BLUE,
                Color.GREEN,
                Shader.TileMode.MIRROR /*or REPEAT*/);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bytes != null) {
            paint.setShader(shader);
            for (int i = 0; i < bytes.length - 1; i++) {
                int top = canvas.getHeight() +
                        ((byte) (Math.abs(bytes[i]) + 128)) * canvas.getHeight() / 128;
                canvas.drawLine(i, getHeight(), i, top, paint);
            }
            super.onDraw(canvas);
        }
    }
}
