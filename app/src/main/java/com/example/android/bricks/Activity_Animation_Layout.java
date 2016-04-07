package com.example.android.bricks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by andrey on 01/03/2016.
 */

public class Activity_Animation_Layout extends View {
    Paint red_paintbrush_fill, blue_paintbrush_fill, green_paintbrush_fill;
    Paint red_paintbrush_stroke, blue_paintbrush_stroke, green_paintbrush_stroke;
    Bitmap paddle_bm;
    int paddle_x, paddle_y;
    int x_dir;
    int paddleWidth;

    public Activity_Animation_Layout(Context context) {
        super(context);
        setBackgroundColor(Color.DKGRAY);
        paddle_x = 320;
        paddle_y = 1270;
        x_dir = 5;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        red_paintbrush_fill = new Paint();
        red_paintbrush_fill.setColor(Color.RED);
        red_paintbrush_fill.setStyle(Paint.Style.FILL);

        blue_paintbrush_fill = new Paint();
        blue_paintbrush_fill.setColor(Color.BLUE);
        blue_paintbrush_fill.setStyle(Paint.Style.FILL);

        green_paintbrush_fill = new Paint();
        green_paintbrush_fill.setColor(Color.GREEN);
        green_paintbrush_fill.setStyle(Paint.Style.FILL);

        red_paintbrush_stroke = new Paint();
        red_paintbrush_stroke.setColor(Color.RED);
        red_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        red_paintbrush_stroke.setStrokeWidth(10);

        blue_paintbrush_stroke = new Paint();
        blue_paintbrush_stroke.setColor(Color.BLUE);
        blue_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        blue_paintbrush_stroke.setStrokeWidth(10);

        green_paintbrush_stroke = new Paint();
        green_paintbrush_stroke.setColor(Color.GREEN);
        green_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        green_paintbrush_stroke.setStrokeWidth(10);

        Rect rectangle001 = new Rect();
        rectangle001.set(210, 125, 250, 175);
        canvas.drawRect(rectangle001, green_paintbrush_fill);
        canvas.drawCircle(400, 400, 70, red_paintbrush_stroke);

        paddle_bm = BitmapFactory.decodeResource(getResources(),R.drawable.paddle);
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),R.drawable.paddle,option);
        paddleWidth = option.outWidth;

        if (paddle_x >= (canvas.getWidth() - paddleWidth)) { // still goes too far to the right
            x_dir = -5;
        }
        if (paddle_x <= 0) {
            x_dir = 5;
        }
        paddle_x = paddle_x + x_dir;

        canvas.drawBitmap(paddle_bm, paddle_x, paddle_y, null);
        invalidate();

        super.onDraw(canvas);
    }
}
