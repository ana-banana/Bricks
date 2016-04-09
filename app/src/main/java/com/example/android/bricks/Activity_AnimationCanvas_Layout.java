package com.example.android.bricks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by andrey on 03/03/2016.
 */
public class Activity_AnimationCanvas_Layout extends SurfaceView implements Runnable {

    final static String TAG = "TEST";
    Thread thread = null;
    boolean CanDraw = false;

    boolean collision = false; // true when ball hits some brick
    int screenWidth, screenHeight;

///////////*****   LEVEL & LEVEL MESSAGES   *****//////////

    int level = 1;

    boolean levelMessage = true;
    int message_x, message_y;
    float messageWidth, messageHeight;
    Bitmap levelOne, levelTwo, levelThree, levelFour;

    int gapForLevel, numOfRowsForLevel, squareHeightForLevel;
    int bricksLeftForLevel;

///////////*****   PAUSING   *****//////////

    Bitmap pausePlease, playPlease;
    boolean paused = false;
    float pause_x, pause_y, pauseWidth, pauseHeight;

///////////*****   WIN   *****//////////

    Bitmap win;
    boolean winLevel = false;
    boolean winMessage = true;

///////////*****   LOOSE   *****//////////

    boolean lostLevel = false;
    boolean restart = false;
    Bitmap lost;

///////////*****   BACKGROUNDS & CANVAS   *****//////////

    Bitmap backGroundDark, backGroungLevel2, backGroungLevel3, backGroungLevel4;

    Canvas canvas;
    SurfaceHolder surfaceHolder;

///////////*****   TOUCHING   *****//////////

    boolean userTouched = false;
    float lastTouchedX = -100;

///////////*****   PADDLE   *****//////////

    // Bitmap paddle;
    float paddle_x, paddle_y;
    float paddleWidth, paddleHeight;
    float paddleStopRight;
    char dir = 'r'; // direction for paddle

///////////*****   SMILE   *****//////////

    Bitmap smilyFace;
    float smile_x, smile_y, smileWidth, smileHeight;
    float smileXToStart, smileYToStart;
    int smileRotations = 0;

///////////*****   BALL   *****//////////

    Bitmap ball;
    float ball_x, ball_y, ball_rad;
    int ballWidth, ballHeight, ballStopRight;
    char ballDir = 'u'; // direction for ball
    float angle;

///////////*****   DRAWING   *****//////////

    Path square;
    Paint red_paintbrush_fill, blue_paintbrush_fill, green_paintbrush_fill, cian_paintbrush_fill,
            red_paintbrush_stroke, blue_paintbrush_stroke, green_paintbrush_stroke;


    public void drawPaddle() {
        drawSquare(paddle_x, paddle_y, (paddle_x + paddleWidth), (paddle_y + paddleHeight), green_paintbrush_fill);
        drawSquare(paddle_x, paddle_y, (paddle_x + paddleWidth), (paddle_y + paddleHeight), blue_paintbrush_stroke);
    }

    public  void smileMove(float leftX, float topY,  int speed) {
        float rightX = message_x + messageWidth - (smileWidth/2);// messageWidth;
        float bottomY = message_y + messageHeight - (smileHeight/2);// messageHeight;
        if (smileRotations < 1) {

            if ((smile_y <= topY) && (smile_x <= rightX)) {
                smile_x = smile_x + speed;
            }
            if ((smile_x >= rightX) && (smile_y <= bottomY)) {
                smile_y = smile_y + speed;
            }
            if ((smile_y >= bottomY) && (smile_x >= leftX)) {
                smile_x = smile_x - speed;
            }
            if ((smile_x <= leftX) && (smile_y >= topY)) {
                smile_y = smile_y - speed;
            }
            if ((smile_x == leftX) &&
                    ((smile_y == topY))) {
                smileRotations = smileRotations + 1;
                String debug1 = "I INCREASED SMILEROTATIONS: " + smileRotations;
                Log.d(TAG, debug1);
            }
        } else {
            Log.d(TAG, "I EXITED SMILE CIRCLE");
            smileRotations = 0;
            smile_x = message_x - (smileWidth/2);
            smile_y = message_y - (smileHeight/2);
            levelMessage = false;
            if (winLevel) {
                winMessage = false;
            }
        }
    }

    public class Brick {
        float x1, x2, y1, y2;
        float midX, midY;

        Paint brush;
        int myRow; // row where the brick is located
        boolean exist = true;

        public Brick() {
            brush = new Paint();
        }
    }

    public class WallOfBricks {
        int squareWidth;
        int squareHeight = 60;
        int gap = 5;
        int numOfBricks = 7;
        int numOfRows = 3;

        int bricksLeft;

        Brick[] stateOfBricks;

        public WallOfBricks(int columns, int rows, int gapping, int brickHeight) {
            numOfBricks = columns;
            numOfRows = rows;
            bricksLeft = numOfBricks*numOfRows;
            bricksLeftForLevel = bricksLeft;
            gap = gapping;
            squareHeight = brickHeight;
            stateOfBricks = new Brick[numOfBricks*numOfRows];
            for (int row = 0; row < numOfRows; row++) {
                for (int i = 0; i < numOfBricks; i++) {
                    int myInd = (i + (row * numOfBricks));
                    String debugWall = "myInd: " + myInd;
                    Log.d(TAG, debugWall);
                    stateOfBricks[myInd] = new Brick();
                    stateOfBricks[myInd].brush = randomBrush();
                }
            }
        }

        public void updateMetrics() {
            for (int row = 0; row < numOfRows; row++) {
                for (int i = 0; i < numOfBricks; i++) {
                    int myInd = (i + (row*numOfBricks));
                    stateOfBricks[myInd].x1 = ((gap * (i + 1)) + (squareWidth * i));
                    stateOfBricks[myInd].x2 = (stateOfBricks[myInd].x1 + squareWidth);
                    stateOfBricks[myInd].y1 = ((gap * (row + 1)) + (squareHeight * row));
                    stateOfBricks[myInd].y2 = stateOfBricks[myInd].y1 + squareHeight;
                    stateOfBricks[myInd].midX = stateOfBricks[myInd].x2 - stateOfBricks[myInd].x1;
                    stateOfBricks[myInd].midY = stateOfBricks[myInd].y2 - stateOfBricks[myInd].y1;
                    stateOfBricks[myInd].myRow = row;
                }
            }
        }

        // draws on canvas all existing bricks
        public void drawWallOfBricks() {

            for (int i = 0; i < (numOfBricks*numOfRows); i++) {
                if (stateOfBricks[i].exist) {
                    drawSquare(stateOfBricks[i].x1, stateOfBricks[i].y1, stateOfBricks[i].x2, stateOfBricks[i].y2, stateOfBricks[i].brush);
                    drawSquare(stateOfBricks[i].x1, stateOfBricks[i].y1, stateOfBricks[i].x2, stateOfBricks[i].y2, blue_paintbrush_stroke);

                }
            }
        }

        public void setBallDirection(float xBall, float yBall, int brickIndex) {
            if (ballDir == 'u') { // ball goes up
                if (yBall >= stateOfBricks[brickIndex].y1) {
                       // from the bottom
                       if (((xBall + ballWidth +2) >= stateOfBricks[brickIndex].x1) &&
                               ((xBall + ballWidth) <= stateOfBricks[brickIndex].x2)){
                           angle = -angle;
                           ballDir = 'd';
                        } else {
                           // from the left side
                           angle = -angle;
                       }
                }

            } else { // ball goes down
                if ((yBall + ballWidth) <= stateOfBricks[brickIndex].y2) {
                    // from the top
                    if (((xBall + ballWidth +2) >= stateOfBricks[brickIndex].x1) &&
                            ((xBall + ballWidth) <= stateOfBricks[brickIndex].x2)){
                        angle = -angle;
                        ballDir = 'u';
                    } else {
                        // from the left side
                        angle = -angle;
                    }
                }
            }
        }

        // brickInARow sets a brick's existence to false if hit and collision to true;
        // changes direction of ball's movement
        public void brickInAWall (float x, float y) {

            for (int i = 0; i < numOfBricks; i++) { //first looking for the column
                if (((x + ballWidth) >= stateOfBricks[i].x1) && (x <= stateOfBricks[i].x2)) {
                    for (int j = 0; j < numOfRows; j++) { // now for the row
                        int ind = (i+(j*numOfBricks));
                        if (((y <= stateOfBricks[ind].y2) && ((y + ballHeight) >=  stateOfBricks[ind].y1)) ||
                                ((((y + ballHeight) >= stateOfBricks[ind].y1)) && (y <= stateOfBricks[ind].y2))) {
                            if (stateOfBricks[ind].exist) {

                                stateOfBricks[ind].exist = false;
                                bricksLeft = bricksLeft - 1; // count down bricks left
                                collision = true;
                                setBallDirection(x, y, ind);
                            }
                        }
                    }
                }
            }
        }



    }

    WallOfBricks myWall1, myWall2, myWall3, myWall4;

    public Activity_AnimationCanvas_Layout(Context context, int level) {
        super(context);
        this.level = level;
        surfaceHolder = getHolder();
        backGroundDark = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
        backGroungLevel2 = BitmapFactory.decodeResource(getResources(), R.drawable.background2);
        backGroungLevel3 = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
        backGroungLevel4 = BitmapFactory.decodeResource(getResources(), R.drawable.background4);
     //   paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        win = BitmapFactory.decodeResource(getResources(), R.drawable.won);
        lost = BitmapFactory.decodeResource(getResources(), R.drawable.lost);
        playPlease = BitmapFactory.decodeResource(getResources(), R.drawable.playplease);
        pausePlease = BitmapFactory.decodeResource(getResources(), R.drawable.pauseplease);
        smilyFace = BitmapFactory.decodeResource(getResources(), R.drawable.smile);
        levelOne = BitmapFactory.decodeResource(getResources(), R.drawable.level1);
        levelTwo = BitmapFactory.decodeResource(getResources(), R.drawable.level2);
        levelThree = BitmapFactory.decodeResource(getResources(), R.drawable.level3);
        levelFour = BitmapFactory.decodeResource(getResources(), R.drawable.level4);
        paddle_x = 0;
        paddle_y = 650;
        paddleWidth = 380;
        paddleHeight = 50;
        ball_x = paddle_x+190;
        ball_y = 450;
        message_x = 100;
        message_y = 550;


        BitmapFactory.Options optionsMessage = new BitmapFactory.Options();
        optionsMessage.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(getResources(), R.drawable.level1, optionsMessage);
        messageWidth = optionsMessage.outWidth;
        messageHeight = optionsMessage.outHeight;

        BitmapFactory.Options optionsSmile = new BitmapFactory.Options();
        optionsSmile.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(getResources(), R.drawable.smile, optionsSmile);
        smileWidth = optionsSmile.outWidth;
        smileHeight = optionsSmile.outHeight;

        smile_x = message_x - (smileWidth/2);
        smile_y = message_y - (smileHeight/2);

        smileXToStart = smile_x;
        smileYToStart = smile_y;

        BitmapFactory.Options optionsPause = new BitmapFactory.Options();
        optionsPause.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(getResources(), R.drawable.pauseplease, optionsPause);
        pauseWidth = optionsPause.outWidth;
        pauseHeight = optionsPause.outHeight;


        prepPaintBrushes();
        myWall1 = new WallOfBricks(5, 3, 5, 120);
        myWall2 = new WallOfBricks(6, 4, 5, 80);
        myWall3 = new WallOfBricks(7, 5, 5, 80);
        myWall4 = new WallOfBricks(8, 6, 5, 60);

    }

    @Override
    public void run() {

        while(CanDraw) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            canvas = surfaceHolder.lockCanvas();

            screenWidth = canvas.getWidth();
            screenHeight = canvas.getHeight();

            pause_x = screenWidth - pauseWidth - 60;
            pause_y = screenHeight - pauseHeight - 60;

            switch (level) {
                case 1:
                    myWall1.squareWidth = (screenWidth / (myWall1.numOfBricks));
                    myWall1.updateMetrics();
                    break;
                case 2:
                    myWall2.squareWidth = (screenWidth / (myWall2.numOfBricks));
                    myWall2.updateMetrics();
                    break;
                case 3:
                    myWall3.squareWidth = (screenWidth / (myWall3.numOfBricks));
                    myWall3.updateMetrics();
                    break;
                default:
                    myWall4.squareWidth = (screenWidth / (myWall4.numOfBricks));
                    myWall4.updateMetrics();
                    break;
            }

            if (levelMessage) {

                switch (level) {
                    case 1:
                        canvas.drawBitmap(backGroundDark, 0, 0, null);
                        canvas.drawBitmap(levelOne, message_x, message_y, null);
                        break;
                    case 2:
                        canvas.drawBitmap(backGroungLevel2, 0, 0, null);
                        canvas.drawBitmap(levelTwo, message_x, message_y, null);
                        break;
                    case 3:
                        canvas.drawBitmap(backGroungLevel3, 0, 0, null);
                        canvas.drawBitmap(levelThree, message_x, message_y, null);
                        break;
                    default:
                        canvas.drawBitmap(backGroungLevel4, 0, 0, null);
                        canvas.drawBitmap(levelFour, message_x, message_y, null);
                        break;
                }

                canvas.drawBitmap(smilyFace, smile_x, smile_y, null);
                smileMove(smileXToStart, smileYToStart, 10);
                String debug10 = "LEVEL MESSAGE: " + levelMessage;
                Log.d(TAG, debug10);
            } else {

                motionPaddle(1);
                motionBall(4 + level);

                switch (level) {
                    case 1:
                        canvas.drawBitmap(backGroundDark, 0, 0, null);
                        myWall1.drawWallOfBricks();
                        if (paused) {
                            canvas.drawBitmap(playPlease, pause_x, pause_y, null);
                        } else {
                            canvas.drawBitmap(pausePlease, pause_x, pause_y, null);
                        }
                        break;
                    case 2:
                        canvas.drawBitmap(backGroungLevel2, 0, 0, null);
                        myWall2.drawWallOfBricks();
                        if (paused) {
                            canvas.drawBitmap(playPlease, pause_x, pause_y, null);
                        } else {
                            canvas.drawBitmap(pausePlease, pause_x, pause_y, null);
                        }
                        break;
                    case 3:
                        canvas.drawBitmap(backGroungLevel3, 0, 0, null);
                        myWall3.drawWallOfBricks();
                        if (paused) {
                            canvas.drawBitmap(playPlease, pause_x, pause_y, null);
                        } else {
                            canvas.drawBitmap(pausePlease, pause_x, pause_y, null);
                        }
                        break;
                    default:
                        canvas.drawBitmap(backGroungLevel4, 0, 0, null);
                        myWall4.drawWallOfBricks();
                        if (paused) {
                            canvas.drawBitmap(playPlease, pause_x, pause_y, null);
                        } else {
                            canvas.drawBitmap(pausePlease, pause_x, pause_y, null);
                        }
                        break;
                }

             //   canvas.drawBitmap(paddle, paddle_x, paddle_y, null);
                drawPaddle();

                canvas.drawBitmap(ball, ball_x, ball_y, null);

                switch (level) {
                    case 1:
                        bricksLeftForLevel = myWall1.bricksLeft;
                        break;
                    case 2:
                        bricksLeftForLevel = myWall2.bricksLeft;
                        break;
                    case 3:
                        bricksLeftForLevel = myWall3.bricksLeft;
                        break;
                    default:
                        bricksLeftForLevel = myWall4.bricksLeft;
                        break;
                }

                if (bricksLeftForLevel == 0) {
                    winLevel = true;
                    canvas.drawBitmap(win, message_x, message_y, null);
                    canvas.drawBitmap(smilyFace, smile_x, smile_y, null);
                    if (winMessage) {
                        smileMove(smileXToStart, smileYToStart, 10);

                    } else {
                        lostLevel = false;
                        winLevel = false;
                        winMessage = true;
                        levelMessage = true;
                        restart = false;
                        paused = false;
                        level = level+1;
                        paddle_x = 0;
                        paddle_y = 650;
                        ball_x = paddle_x+190;
                        ball_y = 450;
                    }
                }

                if (lostLevel) {
                    canvas.drawBitmap(lost, message_x, message_y, null);
                    if (restart) {
                        lostLevel = false;
                        winLevel = false;
                        winMessage = true;
                        levelMessage = true;
                        restart = false;
                        paused = false;
                        paddle_x = 0;
                        paddle_y = 650;
                        ball_x = paddle_x+190;
                        ball_y = 450;

                        switch (level) {
                            case 1:
                                myWall1 = new WallOfBricks(5, 3, 5, 120);
                                break;
                            case 2:
                                myWall2 = new WallOfBricks(6, 4, 5, 80);
                                break;
                            case 3:
                                myWall3 = new WallOfBricks(7, 5, 5, 80);
                                break;
                            default:
                                myWall4 = new WallOfBricks(8, 6, 5, 60);
                                break;
                        }

                    }
                }

            }

            //canvas.drawCircle(650, 630, 25, red_paintbrush_fill);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        CanDraw = false;
        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void resume() {
        CanDraw = true;
        thread = new Thread(this);
        thread.start();
    }

    public void paddleUpdate(MotionEvent event) {
        userTouched = true;
        float x = event.getX();
        float y = event.getY();

        if (lostLevel) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                // if user touches in message "restart" area
                if ((x >= message_x) && (x <= (message_x + messageWidth)) &&
                        (y >= message_y) && (y <= (message_y + messageHeight))) {
                    restart = true;
                }
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ((x >= pause_x) && (x <= (pause_x + pauseWidth)) &&
                            (y >= pause_y) && (y <= (pause_y + pauseHeight))) {
                        if (paused) {
                            paused = false;
                            resume();
                        } else {
                            paused = true;
                            pause();
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "ACTION_MOVE was called");
                    if ((x >= paddle_x) && (x <= (paddle_x + 380))) {

                        float difference = 0;
                        if (lastTouchedX != -100) {
                            //    difference = (x - paddle_x);
                            //} else {
                            difference = (x - lastTouchedX);
                        }
                        paddle_x = (paddle_x + difference);
                        lastTouchedX = x;
                    } /*else if (pauseRequest) {
                    pauseRequest = false;
                } else {
                    pauseRequest = true;
                } */
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "ACTION_UP was called");
                    //     if ((!pauseRequest) && (!paused)) {
                    userTouched = false;
                    lastTouchedX = -100; // just a flag
                /* } else if (pauseRequest) {
                    paused = true;
                    pause();
                } else {
                    paused = false;
                    resume();
                }*/

            }
        }
    }

    private void prepPaintBrushes() {
        red_paintbrush_fill = new Paint();
        red_paintbrush_fill.setColor(Color.MAGENTA); // NOTE THE COLOR
        red_paintbrush_fill.setStyle(Paint.Style.FILL);

        cian_paintbrush_fill = new Paint();
        cian_paintbrush_fill.setColor(Color.CYAN);
        cian_paintbrush_fill.setStyle(Paint.Style.FILL);

        blue_paintbrush_fill = new Paint();
        //float[] hsv_ohra = {60, 89, 75};
        blue_paintbrush_fill.setColor(Color.YELLOW);
        blue_paintbrush_fill.setStyle(Paint.Style.FILL);

        green_paintbrush_fill = new Paint();
        green_paintbrush_fill.setColor(Color.GREEN);
        green_paintbrush_fill.setStyle(Paint.Style.FILL);

        red_paintbrush_stroke = new Paint();
        red_paintbrush_stroke.setColor(Color.RED);
        red_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        red_paintbrush_stroke.setStrokeWidth(10);

        blue_paintbrush_stroke = new Paint();                   // NOTE THE COLOR
        blue_paintbrush_stroke.setColor(Color.DKGRAY);
        blue_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        blue_paintbrush_stroke.setStrokeWidth(5);

        green_paintbrush_stroke = new Paint();
        green_paintbrush_stroke.setColor(Color.GREEN);
        green_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        green_paintbrush_stroke.setStrokeWidth(10);
    }

    public void drawSquare(float x1, float y1, float x2, float y2, Paint brush) {
        square = new Path();
        square.moveTo(x1, y1);
        square.lineTo(x2, y1);
        // square.moveTo(x2, y1);
        square.lineTo(x2, y2);
        // square.moveTo(x2, y2);
        square.lineTo(x1, y2);
        //  square.moveTo(x1, y2);
        square.lineTo(x1, y1);
        //  square.moveTo(x1, y1);
        this.canvas.drawPath(square, brush);
    }

    private void motionPaddle (int speed) {
        if ((!userTouched) && (bricksLeftForLevel != 0)) {

        /*    BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), R.drawable.paddle, options);
            paddleWidth = options.outWidth;
            paddleHeight = options.outHeight; */
            paddleStopRight = (canvas.getWidth());

            if ((paddle_x + paddleWidth) >= paddleStopRight) {
                dir = 'l';
            } else if (paddle_x <= 0) {
                dir = 'r';
            }
            if (dir == 'l') {
                paddle_x = paddle_x - speed;
            } else {
                paddle_x = paddle_x + speed;
            }
        }
    }

    private void motionBall (int speed) {
        if (bricksLeftForLevel != 0) {

            BitmapFactory.Options optionsBall = new BitmapFactory.Options();
            optionsBall.inJustDecodeBounds = false;
            BitmapFactory.decodeResource(getResources(), R.drawable.ball, optionsBall);
            ballWidth = (optionsBall.outWidth);
            ballHeight = optionsBall.outHeight;

            ballStopRight = (canvas.getWidth());
            ball_rad = ball_x + (ballWidth / 2);

            switch (level) {
                case 1:
                    gapForLevel = myWall1.gap;
                    numOfRowsForLevel = myWall1.numOfRows;
                    squareHeightForLevel = myWall1.squareHeight;
                    break;
                case 2:
                    gapForLevel = myWall2.gap;
                    numOfRowsForLevel = myWall2.numOfRows;
                    squareHeightForLevel = myWall2.squareHeight;
                    break;
                case 3:
                    gapForLevel = myWall3.gap;
                    numOfRowsForLevel = myWall3.numOfRows;
                    squareHeightForLevel = myWall3.squareHeight;
                    break;
                default:
                    gapForLevel = myWall4.gap;
                    numOfRowsForLevel = myWall4.numOfRows;
                    squareHeightForLevel = myWall4.squareHeight;
                    break;
            }

            int lowBoundWall = ((gapForLevel * numOfRowsForLevel) + (squareHeightForLevel * numOfRowsForLevel)); // max Y coordinate where bricks start
            if (ball_y <= lowBoundWall) {
                //String debug1 = "Start collision detection: " + lowBoundWall;
                //Log.d(TAG, debug1);

                switch (level) {
                    case 1:
                        myWall1.brickInAWall(ball_x, ball_y);
                        break;
                    case 2:
                        myWall2.brickInAWall(ball_x, ball_y);
                        break;
                    case 3:
                        myWall3.brickInAWall(ball_x, ball_y);
                        break;
                    default:
                        myWall4.brickInAWall(ball_x, ball_y);
                        break;
                }

                //String debug1 = "I have just deleted brick: " + stateInd + " and my ball's direction is: " + ballDir;
                //Log.d(TAG, debug1);

            } else if ((ball_y + ballHeight) >= (paddle_y)) {
                if (!
                        ((ball_x <= (paddle_x + paddleWidth + 20)) &&
                                ((ball_x + ballWidth) >= (paddle_x - 20)))) { // 20 is a "safe" frame
                    lostLevel = true;
                }
                if (!lostLevel) {
                    ballDir = 'u';
                    if ((ball_x >= paddle_x) && (ball_x <= (paddle_x + (paddleWidth/2)))) {
                        angle = -((paddle_x + (paddleWidth/2)) - ball_x) / 5;
                    } else if ((ball_x > (paddle_x + (paddleWidth/2))) && (ball_x <= (paddle_x + paddleWidth))) {
                        angle = (ball_x - (paddle_x + (paddleWidth/2))) / 5;
                    }
                }
            }
            if (ball_x <= 0) {
                angle = -angle;
                ball_x = ball_x + angle;
            }

            if ((ball_x) >= (paddleStopRight - ballWidth)) {
                angle = -angle;
                ball_x = ball_x + angle;
            }

            if (ball_y <= 0) {
                ballDir = 'd';
            }

            if (ballDir == 'd') {
                ball_y = ball_y + speed;
                ball_x = ball_x + (angle/2);
            } else {
                ball_y = ball_y - speed;
                ball_x = ball_x + (angle/2);
            }
        }
    }

    public Paint randomBrush() {
        Random r = new Random();
        int i1 = r.nextInt(5 - 1) + 1;
        switch (i1) {
            case 1:
                return red_paintbrush_fill;
            case 2:
                return blue_paintbrush_fill;
            case 3:
                return cian_paintbrush_fill;
            default:
                return green_paintbrush_fill;
        }
    }




}
