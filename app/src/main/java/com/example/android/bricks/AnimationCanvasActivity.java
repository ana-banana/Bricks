package com.example.android.bricks;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by andrey on 03/03/2016.
 */
public class AnimationCanvasActivity extends Activity {

    Activity_AnimationCanvas_Layout  animationCanvas_LayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animationCanvas_LayoutView = new Activity_AnimationCanvas_Layout(this);
        setContentView(animationCanvas_LayoutView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationCanvas_LayoutView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationCanvas_LayoutView.resume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        animationCanvas_LayoutView.paddleUpdate(event);
        return super.onTouchEvent(event);
    }
}
