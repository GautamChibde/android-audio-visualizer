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
package com.chibde.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.chibde.BaseVisualizer;

/**
 * Custom view that creates a Bar visualizer effect for
 * the android {@link android.media.MediaPlayer}
 *
 * Created by gautam chibde on 28/10/17.
 */

public class LineVisualizer extends BaseVisualizer {
    private float[] points;
    private Rect rect = new Rect();
    private float strokeWidth = 0.005f;

    public LineVisualizer(Context context) {
        super(context);
    }

    public LineVisualizer(Context context,
                          @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineVisualizer(Context context,
                          @Nullable AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
    }

    /**
     * set Stroke width for your visualizer takes input between 1-10
     *
     * @param strokeWidth stroke width between 1-10
     */
    public void setStrokeWidth(int strokeWidth) {
        if (strokeWidth > 10) {
            this.strokeWidth = 10 * 0.005f;
        } else if (strokeWidth < 1) {
            this.strokeWidth = 0.005f;
        }
        this.strokeWidth = strokeWidth * 0.005f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bytes != null) {
            if (points == null || points.length < bytes.length * 4) {
                points = new float[bytes.length * 4];
            }
            paint.setStrokeWidth(getHeight() * strokeWidth);
            rect.set(0, 0, getWidth(), getHeight());

            for (int i = 0; i < bytes.length - 1; i++) {
                points[i * 4] = rect.width() * i / (bytes.length - 1);
                points[i * 4 + 1] = rect.height() / 2
                        + ((byte) (bytes[i] + 128)) * (rect.height() / 3) / 128;
                points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
                points[i * 4 + 3] = rect.height() / 2
                        + ((byte) (bytes[i + 1] + 128)) * (rect.height() / 3) / 128;
            }
            canvas.drawLines(points, paint);
        }
        super.onDraw(canvas);
    }
}
