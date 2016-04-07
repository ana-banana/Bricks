package com.example.android.bricks;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by andrey on 01/03/2016.
 */

public class AnimationActivity extends Activity {
    Activity_Animation_Layout animation_LayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animation_LayoutView = new Activity_Animation_Layout(this);
        setContentView(animation_LayoutView);

    }
}
