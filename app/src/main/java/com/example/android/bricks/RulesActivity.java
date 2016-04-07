package com.example.android.bricks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by andrey on 01/03/2016.
 */
public class RulesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rules);
        Bundle extras = getIntent().getExtras();
        TextView tv = (TextView)findViewById(R.id.textView_activity_rules);
        tv.setText(extras.getString("Message"));
        super.onCreate(savedInstanceState);

    }
}
