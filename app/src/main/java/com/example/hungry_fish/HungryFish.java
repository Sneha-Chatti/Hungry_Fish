package com.example.hungry_fish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class HungryFish extends SurfaceView implements SurfaceHolder.Callback {
    GameThread thread;
    private final int NUM_OF_LIVES = 3;
    static int scorePts;
    int timer, fishW, fishH, fishX, fishY, bgrScroll, screenW, screenH, yellowFoodSpeed = 10, greenFoodSpeed = 15, redFoodSpeed = 12, numOfLivesConsumed, level = 1, round, threshold = 500;
    Bitmap bgr[] = new Bitmap[5];
    Bitmap bgrReverse[] = new Bitmap[5];
    Bitmap life[] = new Bitmap[2];
    Bitmap fish[] = new Bitmap[2];
    Bitmap yellowWorm, greenWorm, bomb, capsule, medicine;
    int yellowFoodOffset[] = new int[2];
    int greenFoodOffset[] = new int[2];
    int redFoodOffset[] = new int[2];
    int capsuleOffest[] = new int[2];
    int medicineOffest[] = new int[2];

    boolean fishFingerMove, reverseBackroundFirst, fishAtStart, fishAtBotton, fishAtLeft, fishAtRight, displayCapsule, displayMedicine;

    Paint score = new Paint();
    MediaPlayer gulp = MediaPlayer.create(getContext(), R.raw.food_sound);
    MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.background_music);

    public HungryFish(Context context) {
        super(context);
        scorePts = 0;
        timer = 0;
        for(int i = 0; i < 5; i++)
            bgr[i] = BitmapFactory.decodeResource(getResources(),R.drawable.background+i); //Load a background.
        fish[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hungry_fish); //Load fish.
        yellowWorm = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_worm1);
        greenWorm = BitmapFactory.decodeResource(getResources(), R.drawable.green_worm);
        bomb = BitmapFactory.decodeResource(getResources(), R.drawable.bacteria);
        capsule = BitmapFactory.decodeResource(getResources(), R.drawable.capsule);
        medicine = BitmapFactory.decodeResource(getResources(), R.drawable.medicine);

        score.setTextSize(50);
        score.setColor(Color.BLACK);
        score.setTypeface(Typeface.DEFAULT_BOLD);
        score.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);

        fishW = fish[0].getWidth();
        fishH = fish[0].getHeight();

        //Create a flag for the onDraw method to alternate background with its mirror image.
        reverseBackroundFirst = false;

        //Initialise animation variables.
        bgrScroll = 0;  //Background scroll position

        //Set thread
        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //This event-method provides the real dimensions of this custom view.
        screenW = w;
        screenH = h;

        for(int i = 0; i < 5; i++)
            bgr[i] = Bitmap.createScaledBitmap(bgr[i], w, h, true); //Scale background to fit the screen.
        life[0] = Bitmap.createScaledBitmap(life[0], w/12, h/12, true);
        fish[0] = Bitmap.createScaledBitmap(fish[0], w/6, h/4, true);
        yellowWorm = Bitmap.createScaledBitmap(yellowWorm, w/11, h/9, true);
        greenWorm = Bitmap.createScaledBitmap(greenWorm, w/11, h/9, true);
        bomb = Bitmap.createScaledBitmap(bomb, w/13, h/8, true);
        capsule = Bitmap.createScaledBitmap(capsule, w/13, h/8, true);
        medicine = Bitmap.createScaledBitmap(medicine, w/13, h/8, true);

        fishW = fish[0].getWidth();
        fishH = fish[0].getHeight();

        //Create a mirror image of the background (horizontal flip) - for a more circular background.
        Matrix matrix = new Matrix();  //Like a frame or mould for an image.
        matrix.setScale(-1, 1); //Horizontal mirror effect.

        for(int i = 0; i < 5; i++)
            bgrReverse[i] = Bitmap.createBitmap(bgr[i], 0, 0, screenW, screenH, matrix, true); //Create a new mirrored bitmap by applying the matrix.

        score.setTextSize(screenW / 17);

        fishX = (int) 0;
        fishY = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(canvas!=null) {
            drawScrollingBackground(canvas);

            canvas.drawText("Level " + level + "   Score: " + scorePts, screenW / 20, screenH / 12, score);

            drawLife(canvas);

            yellowFoodOffset = drawFood(canvas, yellowFoodOffset, yellowWorm, yellowFoodSpeed, "food");
            greenFoodOffset = drawFood(canvas, greenFoodOffset, greenWorm, greenFoodSpeed, "food");
            redFoodOffset = drawFood(canvas, redFoodOffset, bomb, redFoodSpeed, "bacteria");

            if(capsuleOffest[0] <= yellowFoodSpeed)
                displayCapsule = false;
            if(timer > 1000 && ((timer%1000) == 0))
                displayCapsule = true;

            if(medicineOffest[0] <= greenFoodSpeed)
                displayMedicine = false;
            if(timer > 3000 && ((timer%3000) == 0))
                displayMedicine = true;

            if(displayMedicine)
                medicineOffest = drawFood(canvas, medicineOffest, medicine, greenFoodSpeed, "medicine");
            else if(displayCapsule)
                capsuleOffest = drawFood(canvas, capsuleOffest, capsule, yellowFoodSpeed, "capsule");

            drawMovingFish(canvas);
        }

    }

    public void drawScrollingBackground(Canvas canvas) {
        Rect fromRect1 = new Rect(0, 0, screenW - bgrScroll, screenH);
        Rect toRect1 = new Rect(bgrScroll, 0, screenW, screenH);

        Rect fromRect2 = new Rect(screenW - bgrScroll, 0, screenW, screenH);
        Rect toRect2 = new Rect(0, 0, bgrScroll, screenH);

        if (!reverseBackroundFirst) {
            canvas.drawBitmap(bgr[round], fromRect1, toRect1, null);
            canvas.drawBitmap(bgrReverse[round], fromRect2, toRect2, null);
        }
        else{
            canvas.drawBitmap(bgr[round], fromRect2, toRect2, null);
            canvas.drawBitmap(bgrReverse[round], fromRect1, toRect1, null);
        }

        bgrScroll -= 1;
        //Next value for the background's position.
        if (bgrScroll >= screenW) {
            bgrScroll = 0;
            reverseBackroundFirst = !reverseBackroundFirst;
        } else if (bgrScroll <= 0) {
            bgrScroll = screenW;
            reverseBackroundFirst = !reverseBackroundFirst;
        }
    }

    public void drawLife(Canvas canvas) {
        for(int i = 0; i < NUM_OF_LIVES - numOfLivesConsumed; i++)
            canvas.drawBitmap(life[0], (screenW - (screenW / 12) * 3) + (i * screenW / 12), 0, null);
    }

    public void drawMovingFish(Canvas canvas) {
        if (!fishFingerMove) {
            if(fishY <= 0){
                fishY = 0;
                fishAtStart = true;
                fishAtBotton = false;
            } else if(fishY >= (screenH - fishH))
            {
                fishY = (screenH - fishH);
                fishAtBotton = true;
                fishAtStart = false;
            }

            if(fishX == 0)
            {
                fishAtLeft = true;
                fishAtRight = false;
            } else if(fishX >= (screenW - fishW)) {
                fishAtLeft = false;
                fishAtRight = true;
            }

            if(fishAtStart) {
                fishY = fishY + 1;
            }
            else if(fishAtBotton) {
                fishY = fishY - 1;
            }
            if(fishAtLeft)
                fishX++;
            else if(fishAtRight)
                fishX = 0;
        }

        canvas.save(); //Save the position of the canvas matrix.
        canvas.drawBitmap(fish[0], fishX, fishY, null); //Draw the fish by applying the canvas rotated matrix.
        canvas.restore(); //Rotate the canvas matrix back to its saved position - only the fish bitmap was rotated not all canvas.
    }

    public int[] drawFood(Canvas canvas, int[] offset, Bitmap food, int speed, String type) {
        offset[0] = offset[0] - speed;
        if(hitBallChecker(offset[0], offset[1])) {
            if(type.equalsIgnoreCase("bacteria")) {
                numOfLivesConsumed++;
                if(numOfLivesConsumed >= 3) {
                    Thread th = new Thread(new Runnable() {
                        public void run() {
                            Intent mainIntent = new Intent(getContext(), GameOverActivity.class);
                            getContext().startActivity(mainIntent);
                        }
                    });
                    th.start();
                    th.interrupt();
                }
            }
            else if(type.equalsIgnoreCase("capsule")){
                displayCapsule = false;
                if(numOfLivesConsumed > 0)
                    numOfLivesConsumed--;
            }
            else if(type.equalsIgnoreCase("medicine")){
                displayMedicine = false;
                numOfLivesConsumed = 0;
            }
            else
                scorePts = scorePts + speed;

            if(scorePts >= threshold) {
                level++;
                if(round < 5)
                    round++;
                else
                    round = 0;

                yellowFoodSpeed +=2;
                greenFoodSpeed +=2;
                redFoodSpeed +=2;
                threshold += 500;
                fishX = 0; fishY = 0;
                fishAtStart = true;
                fishAtBotton = false;
            }
            offset[0] = -100;
        }
        if(offset[0] < 0) {
            offset[0] = screenW + (speed + 1);
            offset[1] = (int) Math.floor(Math.random() * screenH);
        }

        canvas.drawBitmap(food, offset[0], offset[1], null);
        return new int[]{offset[0], offset[1]};
    }

    public boolean hitBallChecker(int x, int y) {
        if(x <= (fishX + fishW) && x >= fishX && y <= (fishY + fishH) && y >= fishY) {
            if(SplashActivity.playSound)
                gulp.start();
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                fishX = (int) ev.getX() - fishW /2;
                fishY = (int) ev.getY() - fishH /2;

                fishFingerMove = true;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                fishX = (int) ev.getX() - fishW /2;
                fishY = (int) ev.getY() - fishH /2;

                break;
            }

            case MotionEvent.ACTION_UP:
                fishFingerMove = false;
                break;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        mediaPlayer.stop();
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private HungryFish gameView;
        private boolean run = false;

        public GameThread(SurfaceHolder surfaceHolder, HungryFish gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        public void setRunning(boolean run) {
            this.run = run;
        }

        public SurfaceHolder getSurfaceHolder() {
            return surfaceHolder;
        }

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            Thread th = new Thread(new Runnable() {
                Canvas c;
                @Override
                public void run() {
                    if(SplashActivity.playMusic) {
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                    }
                    while (run) {
                        c = null;
                        try {
                            c = surfaceHolder.lockCanvas(null);
                            synchronized (surfaceHolder) {
                            //call methods to draw and process next fame
                                gameView.onDraw(c);
                            }
                        } finally {
                            if (c != null) {
                                surfaceHolder.unlockCanvasAndPost(c);
                                timer++;
                            }
                        }
                    }
                }
            });
            th.start();
            th.interrupt();
        }
    }
}
