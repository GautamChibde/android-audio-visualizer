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
import android.graphics.Paint;

import androidx.annotation.Nullable;

import android.util.AttributeSet;


/**
 * Custom view that creates a Circle and Bar visualizer effect for
 * the android {@link android.media.MediaPlayer}
 * <p>
 * Created by gautam chibde on 20/11/17.
 * Smooth effect added by Ali heidari
 */

public class CircleBarVisualizerSmooth extends BaseVisualizer {
    private final static float _StepsCount = 2;
    private final static int _BarCount = 120;
    private final static float _AngleStep = 360f / _BarCount;
    private double angle = 0;
    private float[] points;
    private float[] endPoints;
    private float[] oldEndPoints;
    private float[] diffs;
    private int stepCounter = 0;
    private int round = 0;
    private int radius;
    private int x, t;
    private boolean needsInit = true;

    public CircleBarVisualizer(Context context) {
        super(context);
    }

    public CircleBarVisualizer(Context context,
                               @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBarVisualizer(Context context,
                               @Nullable AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        paint.setStyle(Paint.Style.STROKE);
        radius = -1;
    }



    /*
    Draw waveform
    It calculates the StartX,StartY just once because it never changes.
    Then calculates EndX, EndY, OldEndX and OldEndY every 3 frames.
    So OldEndX and OldEndY can increase/decrease toward EndX and EndY respectively.
    To perform such an action(Animation) you need differences of X and Y.
    It achieves using EndX - OldEndX and EndY - OldEndY
    Then find the steps using Differences / 3
    Finally when OldEnd(s) matched to End(s) Need to set End with OldEnd value
    And the action will be repeated until visualizer is running.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Check if bytes initiated before
        if (bytes == null || bytes.length == 0)
            return;

        // Calculates ceiling regarded to bytes length. Ceiling is a coefficient for byte indexer.
        // Because we have 120 bars, so the buffer should be filtered and only 120 bytes from buffer will have chosen to be shown.
        float ceiling = (bytes.length - (bytes.length % 4)-4) / _BarCount;

        // The radius of center circle which bars start around it
        if (needsInit) {
            // Temporary value to get smaller dimension of visualizer
            radius = getHeight() < getWidth() ? getHeight() : getWidth();
            // Calculates the radius of center circle.
            // Formula disclaimer : 0.65 = 3.14 * 0.02
            radius = (int) (radius * 0.65 / 2) * 6 / 10;
            // Width of each bar
            double circumference = 1.5 * Math.PI * radius;
            paint.setStrokeWidth((float) (circumference  / _BarCount));
        }


        // Set points sizes if it is first time we got here or for any reasons arrays are broken.
        if (needsInit || points == null || points.length < bytes.length * 2) {
            //It needs to multiply by 4 because for every byte should be StartX,StartY,EndX,EndY
            points = new float[bytes.length * 4];
            //It needs to multiply by 2 because for every byte should be EndX,EndY
            endPoints = new float[bytes.length * 2];
            //It needs to multiply by 2 because for every byte should be OldEndX,OldEndY
            oldEndPoints = new float[bytes.length * 2];
            //It needs to multiply by 2 because there are X and Y differences
            diffs = new float[bytes.length * 2];
        }

        // Find the round by
        round = (int) (stepCounter % _StepsCount);

        // We start with angle 0 and go against clock's direction
        angle = 0;
        // Calculates every points and iterate along increasing angle
        for (int i = 0; i < _BarCount; i++, angle += _AngleStep) {
            // Find the index of byte inside buffer
            x = (int) Math.ceil(i * ceiling);
            // Change the sign of byte
            byte a = (byte) (-Math.abs(bytes[x]) + 128);
            // Gets the length of the line
            t = a * (getHeight() / 4) / 128;

            // First time calculates the startX and startY for every byte
            if (needsInit) {
                // Find startX
                points[i * 4] = (float) (getWidth() / 2
                        + radius
                        * Math.cos(Math.toRadians(angle)));
                // Find startY
                points[i * 4 + 1] = (float) (getHeight() / 2
                        + radius
                        * Math.sin(Math.toRadians(angle)));
            }

            if (round == 0) {

                // Set the old ends before assign new value the ends
                oldEndPoints[i * 2] = endPoints[i * 2];
                oldEndPoints[i * 2 + 1] = endPoints[i * 2 + 1];

                // Find endX
                endPoints[i * 2] = (float) (getWidth() / 2
                        + (radius + t)
                        * Math.cos(Math.toRadians(angle)));
                // Find endY
                endPoints[i * 2 + 1] = (float) (getHeight() / 2
                        + (radius + t)
                        * Math.sin(Math.toRadians(angle)));

                // If it is not first time, so we have oldEnds for calculation of differences
                if (!needsInit) {
                    // Find differences of Xs
                    diffs[i * 2] = (endPoints[i * 2] - oldEndPoints[i * 2]) / _StepsCount;
                    // Find differences of Ys
                    diffs[i * 2 + 1] = (endPoints[i * 2 + 1] - oldEndPoints[i * 2 + 1]) / _StepsCount;
                } else {
                    // Set the old ends
                    oldEndPoints[i * 2] = endPoints[i * 2];
                    oldEndPoints[i * 2 + 1] = endPoints[i * 2 + 1];
                }
            }
            // Increase/Decrease the length of bar so oldEnd can match with ends
            if (round <= _StepsCount) {
                // Find endX to be drawn
                points[i * 4 + 2] = oldEndPoints[i * 2] + diffs[i * 2] * round;
                // Find endX to be drawn
                points[i * 4 + 3] = oldEndPoints[i * 2 + 1] + diffs[i * 2 + 1] * round;
            }


        }
        if (!needsInit)
            canvas.drawLines(points, paint);

        super.onDraw(canvas);
        // The stepCounter increases
        stepCounter++;
        // Initialized, no longer need initializing
        if (needsInit)
            needsInit = false;
    }
}