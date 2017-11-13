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
 * Created by gautam chibde on 28/10/17.
 */

public class BarVisualizer extends BaseVisualizer {

    private int density = 50;

    public BarVisualizer(Context context) {
        super(context);
    }

    public BarVisualizer(Context context,
                         @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarVisualizer(Context context,
                         @Nullable AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BarVisualizer(Context context,
                         @Nullable AttributeSet attrs,
                         int defStyleAttr,
                         int defStyleRes) {
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
            int div = (int) Math.ceil(bytes.length / density);
            paint.setStrokeWidth(barWidth - 4);
            int k = 0;
            for (int i = barWidth / 2; i < getWidth() && k < bytes.length; i += barWidth) {
                int top = canvas.getHeight() +
                        ((byte) (Math.abs(bytes[k]) + 128)) * canvas.getHeight() / 128;
                canvas.drawLine(i, getHeight(), i, top, paint);
                k += div;
            }
            super.onDraw(canvas);
        }
    }
}

