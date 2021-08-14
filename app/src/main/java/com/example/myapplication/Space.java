package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class Space {

    private int width;
    private int height;
    private int maxRange = 300;
    private int screenRange = 10;
    private int speed = 5;
    private int starsCount = 400;
    private int starWidth;
    private int starHeight;
    private float kxEnd = 0.5f;
    private float kyEnd = 0.5f;
    private float kx = 0.5f;
    private float ky = 0.5f;
    private float dX;
    private float dY;
    private float absX;
    private float absY;

    private Random rnd;
    private Paint starPaint;
    private Paint bgPaint;

    private Star[] stars;

    public Space(){
        setSizes(600, 800);

        starPaint = new Paint();
        starPaint.setColor(Color.WHITE);

        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);

        rnd = new Random();

        stars = new Star[starsCount];
        initStars();
    }

    public void update(){
        if (kx <= (kxEnd + absX) && kx >= (kxEnd - absX)){
            dX = 0;
        }
        if (ky <= (kyEnd + absY) && ky >= (kyEnd - absY)) {
            dY = 0;
        }
        ky += dY;
        kx += dX;

        for (int i = 0; i < starsCount; i++) {
            stars[i].update();
        }
    }

    public void render(Canvas canvas) {
        canvas.drawPaint(bgPaint);

        for (int i = 0; i < starsCount; i++) {
            stars[i].render(canvas);
        }
    }

    public void setSizes(int width, int height){
        this.width = width;
        this.height = height;

        starWidth = width * maxRange / screenRange / 2;
        starHeight = height * maxRange / screenRange / 2;
    }

    private void initStars(){
        for (int i = 0; i < starsCount; i++){
            stars[i] = new Star();
        }
    }

    public void setKxEnd(float x) {
        this.kxEnd = x / width;
        dX = (kxEnd - kx) / SpaceWallpaperService.SpaceEngine.FPS;
        absX = dX >= 0 ? dX : -dX;

    }

    public void setKyEnd(float y) {
        this.kyEnd = y / height;
        dY = (kyEnd - ky) / SpaceWallpaperService.SpaceEngine.FPS;
        absY = dY >= 0 ? dY : -dY;
    }

    class Star{
        public int x;
        public int y;
        public int z;
        public int screenX;
        public int screenY;
        public int screenX0;
        public int screenY0;

        public Star(){
            x = starWidth / 2 - rnd.nextInt(starWidth);
            y = starHeight / 2 - rnd.nextInt(starHeight);
            z = screenRange + rnd.nextInt(maxRange - screenRange);
        }

        public void update(){
            z -= speed;

            if (z == 0) {
                z = 1;
            }
            screenX0 = screenX;
            screenY0 = screenY;

            screenX = (int) (width * kx) + screenRange * x / z;
            screenY = (int) (height * ky) + screenRange * y / z;

            if (screenX < 0 || screenY < 0 || screenX > width || screenY > height || z < screenRange){
                x = starWidth / 2 - rnd.nextInt(starWidth);
                y = starHeight / 2 - rnd.nextInt(starHeight);
                z = maxRange + rnd.nextInt(screenRange);

                screenX = (int) (width * kx) + screenRange * x / z;
                screenY = (int) (height * ky) + screenRange * y / z;
                screenX0 = screenX;
                screenY0 = screenY;
            }


        }


        public void render(Canvas canvas){

            if (speed > 5) {
                canvas.drawLine(screenX0, screenY0, screenX, screenY, starPaint);
            }
            canvas.drawCircle(screenX, screenY, 3, starPaint);
        }



    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
