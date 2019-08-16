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
package com.chibde.audiovisualizer.sample.visualizer;

import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.chibde.audiovisualizer.sample.BaseActivity;
import com.chibde.audiovisualizer.sample.R;
import com.chibde.visualizer.SquareBarVisualizer;

public class SquareBarVisualizerActivity extends BaseActivity {

    @Override
    protected void init() {
        SquareBarVisualizer squareBarVisualizer = findViewById(R.id.visualizer);

        // set custom color to the line.
        squareBarVisualizer.setColor(ContextCompat.getColor(this, R.color.custom));

        // define custom number of bars you want in the visualizer between (10 - 256).
        squareBarVisualizer.setDensity(65);

        // set Gap
        squareBarVisualizer.setGap(2);

        // Set your media player to the visualizer.
        squareBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
    }

    public void replay(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    public void playPause(View view) {
        playPauseBtnClicked((ImageButton) view);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_square_bar_visualizer;
    }
}
