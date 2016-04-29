package com.example.android.bricks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class LevelsButton extends Activity {

    public class LevelsButtonRow {
        private int levelMessagePic;
        private String levelNumber;
        private int levelWallPic;

        public LevelsButtonRow(int levelMessagePic, String levelNumber, int levelWallPic) {
            this.levelMessagePic = levelMessagePic;
            this.levelNumber = levelNumber;
            this.levelWallPic = levelWallPic;
        }

        public int getLevelMessagePic() {
            return levelMessagePic;
        }

        public void setLevelMessagePic(int levelMessagePic) {
            this.levelMessagePic = levelMessagePic;
        }

        public String getLevelNumber() {
            return levelNumber;
        }

        public void setLevelNumber(String levelNumber) {
            this.levelNumber = levelNumber;
        }

        public int getLevelWallPic() {
            return levelWallPic;
        }

        public void setLevelWallPic(int levelWallPic) {
            this.levelWallPic = levelWallPic;
        }
    }

    public class LevelsButtonRowAdapter extends ArrayAdapter<LevelsButtonRow> {
        private Context context;

        public LevelsButtonRowAdapter(Context context, ArrayList<LevelsButtonRow> levelsButtonRow) {
            super(context, 0, levelsButtonRow);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LevelsButtonRow levelsButtonRow = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.activity_levelbutton_single_row, parent, false);
            }
            ((ImageView) convertView.findViewById(R.id.imageViewLevelMessage)).setImageResource(levelsButtonRow.getLevelMessagePic());
            ((TextView) convertView.findViewById(R.id.levelTextView)).setText(levelsButtonRow.getLevelNumber());
            ((ImageView) convertView.findViewById(R.id.imageViewLevelWall)).setImageResource(levelsButtonRow.getLevelWallPic());
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelbutton);
        // Levels data
        ArrayList<LevelsButtonRow> levelsList = new ArrayList<LevelsButtonRow>();
        levelsList.add(new LevelsButtonRow(R.drawable.screenshotlevel1two,     getString(R.string.levelOne), R.drawable.screenshotlevel1three));
        levelsList.add(new LevelsButtonRow(R.drawable.screenshotlevel2message, getString(R.string.levelTwo), R.drawable.screenshotlevel2wall));
        levelsList.add(new LevelsButtonRow(R.drawable.screenshotlevel3message, getString(R.string.levelThree), R.drawable.screenshotlevel3wall));
        levelsList.add(new LevelsButtonRow(R.drawable.screenshotlevel4message, getString(R.string.levelFour), R.drawable.screenshotlevel4wall));
        // Adapter for managing the data
        LevelsButtonRowAdapter adapter = new LevelsButtonRowAdapter(LevelsButton.this, levelsList);
        // Listview component to show list of data
        ListView levelsListView = (ListView) findViewById(R.id.activity_levelbutton_listView);
        levelsListView.setAdapter(adapter);
        levelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                LevelsButtonRow levelsButtonRow = (LevelsButtonRow) adapter.getItemAtPosition(position);
                //Toast.makeText(LevelsButton.this, "Play:" + levelsButtonRow.getLevelNumber(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LevelsButton.this, AnimationCanvasActivity.class);
                intent.putExtra("MessageLevel", levelsButtonRow.getLevelNumber());
                startActivity(intent);
            }
        });
    }
}



    /* // If decide to make levels again in grid view //
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.activity_levelsbutton_gridView) {

            switch (position) {

                case 0: {
                    startActivity(new Intent(this,AnimationActivity.class));
                    break;
                }
                case 1: {
                    startActivity(new Intent(this,AnimationActivity.class));
                    break;
                }
                case 2: {
                    startActivity(new Intent(this,AnimationActivity.class));
                    break;
                }
                case 3: {
                    startActivity(new Intent(this,AnimationActivity.class));
                    break;
                }
                default: {
                    Toast.makeText(getApplicationContext(), "No action associated with this item.", Toast.LENGTH_LONG).show();
                    break;
                }
            }

        }
    } */

