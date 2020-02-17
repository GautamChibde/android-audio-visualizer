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

import java.util.HashMap;
import java.util.Map;

/**
 * Custom view that creates a Circle and Bar visualizer effect for the android
 * {@link android.media.MediaPlayer}
 * <p>
 * Created by gautam chibde on 20/11/17. Smooth effect added by Ali heidari
 */

public class CircleBarVisualizerSmooth extends BaseVisualizer {
    private final static float _StepsCount = 2;
    private final static int _BarCount = 120;
    private final static float _AngleStep = 360f / _BarCount;
    private float[] points;
    private float[] endPoints;
    private float[] diffs;
    // Stores radius and step-counter which every invoking of "onDraw" requires them
    private Map<String, Integer> configs = null;

    public CircleBarVisualizerSmooth(Context context) {
        super(context);
    }

    public CircleBarVisualizerSmooth(Context context,
                                     @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBarVisualizerSmooth(Context context,
                                     @Nullable AttributeSet attrs,
                                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        paint.setStyle(Paint.Style.STROKE);
    }

    /*
     * Returns the value of given configuration-key with handling
     * @see  java.lang#NullPointerException
     */
    private int getConfig(String key) {
        Object obj = configs.get(key);
        if (obj != null)
            return (int) obj;
        else
            return 0;
    }

    /*
     *set new value of given configuration-key
     */
    private void setConfig(String key, int value) {
        configs.put(key, value);
    }

    /*
     * Fill the initial configurations
     */
    private void fillConfigs() {
        configs = new HashMap<>();
        // Temporary value to get smaller dimension of visualizer
        int smallerDimen;
        if (getHeight() < getWidth()) smallerDimen = getHeight();
        else smallerDimen = getWidth();
        // Calculates the radius of center circle.
        // Formula disclaimer : 0.65 = 3.14 * 0.02
        double t = smallerDimen * 0.65;
        int radius = (int) (t / 2);
        radius = radius * 6 / 10;
        // Width of each bar
        double circumference = Math.PI * radius;
        circumference = 1.5 * circumference;
        paint.setStrokeWidth((float) (circumference / _BarCount));
        // Store initial configs
        configs.put("needsInit", 1);//0 = false, 1 = true
        configs.put("radius", radius);
        configs.put("stepCounter", 0);
    }

    /*
     * Initializes the points
     */
    private void fillPoints() {
        // Set points sizes if it is first time we got here or for any reasons arrays
        // are broken.
        if (getConfig("needsInit") == 1 || points == null || points.length < bytes.length * 2) {
            // It needs to multiply by 4 because for every byte should be
            // StartX,StartY,EndX,EndY
            points = new float[bytes.length * 4];
            // It needs to multiply by 4 because for every byte should be EndX,EndY,OldEndX,OldEndY
            endPoints = new float[bytes.length * 4];
            // It needs to multiply by 2 because there are X and Y differences
            diffs = new float[bytes.length * 2];
        }
    }

    /*
     * Calculates the points of each round. Round represents amount of decrease/increase the length of bar
     */
    private void calcRound(int i, double angle, int t) {
        int indexM2 = i * 2;
        int indexM4 = i * 4;
        // Find the round by
        int round = (int) (getConfig("stepCounter") % _StepsCount);
        if (round == 0) {

            // Set the old ends before assign new value the ends
            endPoints[indexM4 + 2] = endPoints[indexM4];
            endPoints[indexM4 + 3] = endPoints[indexM4 + 1];

            float halfWidth = getWidth() / 2;
            float halfHeight = getWidth() / 2;
            float radius_p_t = getConfig("radius") + t;
            // Find endX
            endPoints[indexM4] = (float) (halfWidth + radius_p_t * Math.cos(Math.toRadians(angle)));
            // Find endY
            endPoints[indexM4 + 1] = (float) (halfHeight + radius_p_t * Math.sin(Math.toRadians(angle)));

            // If it is not first time, so we have oldEnds for calculation of differences
            if (getConfig("needsInit") == 0) {
                // Find differences of Xs
                diffs[indexM2] = (endPoints[indexM4] - endPoints[indexM4 + 2]) / _StepsCount;
                // Find differences of Ys
                diffs[indexM2 + 1] = (endPoints[indexM4 + 1] - endPoints[indexM4 + 3]) / _StepsCount;
            } else {
                // Set the old ends
                endPoints[indexM4 + 2] = endPoints[indexM4];
                endPoints[indexM4 + 3] = endPoints[indexM4 + 1];
            }
        }
        // Increase/Decrease the length of bar so oldEnd can match with ends
        if (round <= _StepsCount) {
            // Find endX to be drawn
            points[indexM4 + 2] = endPoints[indexM4 + 2] + diffs[indexM2] * round;
            // Find endX to be drawn
            points[indexM4 + 3] = endPoints[indexM4 + 3] + diffs[indexM2 + 1] * round;
        }
    }

    /*
     * Calculates the legth of bar
     */
    private int getBarLength(int i, float ceiling) {
        // Find the index of byte inside buffer
        int x = (int) Math.ceil(i * ceiling);
        // Change the sign of byte
        byte a = (byte) (-Math.abs(bytes[x]) + 128);
        // Gets the length of the line
        return a * (getHeight() / 4) / 128;
    }

    /*
     * Draw waveform It calculates the StartX,StartY just once because it never
     * changes. Then calculates EndX, EndY, OldEndX and OldEndY every 3 frames. So
     * OldEndX and OldEndY can increase/decrease toward EndX and EndY respectively.
     * To perform such an action(Animation) you need differences of X and Y. It
     * achieves using EndX - OldEndX and EndY - OldEndY Then find the steps using
     * Differences / 3 Finally when OldEnd(s) matched to End(s) Need to set End with
     * OldEnd value And the action will be repeated until visualizer is running.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Check if bytes initiated before
        if (bytes == null || bytes.length == 0)
            return;

        float halfWidth = getWidth() / 2;
        float halfHeight = getWidth() / 2;
        int t;

        // Calculates ceiling regarded to bytes length. The ceiling is a coefficient for
        // byte indexer.
        // Because we have 120 bars, so the buffer should be filtered and only 120 bytes
        // from the buffer will have chosen to be shown.
        float mod = bytes.length % 4;
        float ceiling = (bytes.length - mod) / _BarCount;

        // The radius of center circle which bars start around it
        if (configs == null)
            fillConfigs();

        // Fill the points
        fillPoints();


        // We start with angle 0 and go against clock's direction
        double angle = 0;
        // Calculates every points and iterate along increasing angle
        for (int i = 0; i < _BarCount; i++, angle += _AngleStep) {
            int indexM4 = i * 4;
            // Get length of bar
            t = getBarLength(i, ceiling);
            // First time calculates the startX and startY for every byte
            if (getConfig("needsInit") == 1) {

                // Find startX
                points[indexM4] = (float) (halfWidth + getConfig("radius") * Math.cos(Math.toRadians(angle)));
                // Find startY
                points[indexM4 + 1] = (float) (halfHeight + getConfig("radius") * Math.sin(Math.toRadians(angle)));
            }
            // Calculates points for current round
            calcRound(i, angle, t);

        }
        if (getConfig("needsInit") == 0)
            canvas.drawLines(points, paint);

        super.onDraw(canvas);
        // The stepCounter increases
        setConfig("stepCounter", getConfig("stepCounter") + 1);
        // Initialized, no longer need initializing
        if (getConfig("needsInit") == 1)
            setConfig("needsInit", 0);
    }
}