package com.example.android.bricks;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.RelativeLayout;

/**
 * Created by andrey on 01/03/2016.
 */
public class PlayButton extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playbutton);
     //   RelativeLayout playLayout = new RelativeLayout(this);
     //   playLayout.setBackgroundColor(Color.BLUE);
     //   setContentView(playLayout);
    }
}
